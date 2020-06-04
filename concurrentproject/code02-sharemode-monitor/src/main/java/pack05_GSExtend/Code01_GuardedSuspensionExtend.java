package pack05_GSExtend;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 怀风
 * @date 2020/6/3 21:29
 * @intro 保护性暂停模式 的扩展
 * 现在有多个生产者 和 多个消费者，它们之间的通信需要多个 Guarded Object
 * 扩展：现在通过一个 中间类管理 Guarded Object，来解耦 生产者 和 消费者
 * <p>
 * 试想：如果不通过中间类解耦，则需要手动创建多个 Guarded Object 来维系每组 生产者 和 消费者之间的通信
 */
@Slf4j(topic = "jk.Code01_GuardedSuspensionExtend")
public class Code01_GuardedSuspensionExtend {

    public static void main(String[] args) throws InterruptedException {

        log.debug("消费者开始等待");
        for (int i = 0; i < 3; i++) {
            new Consumer().start();
        }

        Thread.sleep(2000);

        for (Integer id : MiddleClass.getIds()) {
            new Producer(id, "产品" + id).start();
        }

    }

}

/**
 * 消费者
 */
@Slf4j(topic = "jk.Consumer")
class Consumer extends Thread {
    @Override
    public void run() {
        GuardedObjectExtend guardedObject = MiddleClass.generateGuardedObjectExtend();
        Object response = guardedObject.getResponse(5000);
        log.debug("{}号 消费者，获取到的产品是：{}", guardedObject.getId(), response);
    }
}

/**
 * 生产者
 */
@Slf4j(topic = "jk.Producer")
class Producer extends Thread {

    private Object product;
    private Integer id;

    public Producer(Integer id, Object product) {
        this.id = id;
        this.product = product;
    }

    @Override
    public void run() {
        GuardedObjectExtend guardedObject = MiddleClass.getGuardedObjectExtendById(id);
        log.debug("给{}号消费者生产产品，内容为：{}", id, product);
        guardedObject.setResponse(product);
    }
}


/**
 * 中间解耦类，生成并管理 GuardedObjectExtend 对象
 */
class MiddleClass {
    private static final Map<Integer, GuardedObjectExtend> boxes = new ConcurrentHashMap<>();

    private static Integer id = 1;

    private static synchronized int getId() {
        return id++;
    }

    /**
     * 创建 GuardedObjectExtend 对象 并放到 boxes 中管理
     *
     * @return
     */
    public static GuardedObjectExtend generateGuardedObjectExtend() {
        GuardedObjectExtend guardedObjectExtend = new GuardedObjectExtend(getId());
        boxes.put(guardedObjectExtend.getId(), guardedObjectExtend);
        return guardedObjectExtend;
    }

    /**
     * 获取所有 GuardedObjectExtend 的id
     *
     * @return
     */
    public static Set<Integer> getIds() {
        return boxes.keySet();
    }

    /**
     * 根据 id 获取 GuardedObjectExtend 对象
     *
     * @param id
     * @return
     */
    public static GuardedObjectExtend getGuardedObjectExtendById(Integer id) {
        return boxes.remove(id);
    }
}


class GuardedObjectExtend {

    /**
     * 通过 id 标识每一个 Guarded Object
     */
    private final int id;

    private Object response;

    public GuardedObjectExtend(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    /**
     * 获取
     *
     * @param timeout
     * @return
     */
    public Object getResponse(long timeout) {
        synchronized (this) {
            long begin = System.currentTimeMillis();
            long delay = 0;
            while (this.response == null) {
                long waitTime = timeout - delay;
                if (waitTime <= 0) {
                    break;
                }
                try {
                    this.wait(waitTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                delay = System.currentTimeMillis() - begin;
            }
        }
        return this.response;
    }

    /**
     * 生产
     *
     * @param response
     */
    public void setResponse(Object response) {
        synchronized (this) {
            this.response = response;
            notifyAll();
        }
    }

}