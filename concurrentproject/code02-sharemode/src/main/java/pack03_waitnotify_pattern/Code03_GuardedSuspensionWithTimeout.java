package pack03_waitnotify_pattern;

import lombok.extern.slf4j.Slf4j;

/**
 * @author 怀风
 * @date 2020/6/3 17:08
 * @intro 保护性暂停 模式
 * 用于 一个线程 需要接收 另一个线程 产生的结果，因为需要等待结果，所以是同步的一种设计模式
 * JDK中的 join 和 Future 就是使用的这样模式
 * <p>
 * 优化：增加等待时间！
 * 思路：记录开始等待的时刻，每次循环一轮（可能会有虚假唤醒）记录目前为止已经等待的时间；
 *      通过总共需要等待的时间（timeout）减去已经等待的时间，得出还需等待的时间！
 *
 */
@Slf4j(topic = "jk.Code02_GuardedSuspension")
public class Code03_GuardedSuspensionWithTimeout {

    public static void main(String[] args) throws InterruptedException {

        GuardedObjectWithTimeout guarded = new GuardedObjectWithTimeout();

        // 消费线程
        new Thread(() -> {
            log.debug("begin");
            log.debug("获取到产品：{}", guarded.getResponse(3000));
        }, "consumer").start();


        Thread.sleep(2000);
        // 生产线程
        new Thread(() -> {
            guarded.setResponse("精品卫衣");
//            gurade.setResponse(null);  // 测试虚假唤醒，查看等待时间是否足够
        }, "producer").start();

    }
}

/**
 * 用于传递结果的 中间对象
 * 优化：增加等待超时时间
 */
@Slf4j(topic = "jk.GuardedObject")
class GuardedObjectWithTimeout {

    private Object response;

    /**
     * 增加等待超时时间
     *
     * @param timeout 毫秒
     * @return
     */
    public Object getResponse(long timeout) {
        synchronized (this) {
            long begin = System.currentTimeMillis();
            long passedTime = 0;
            while (response == null) {
                // 还需要等待的时间
                long waitTime = timeout - passedTime;
                if (waitTime <= 0) {
                    break;
                }
                try {
                    this.wait(waitTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 截止到本次循环，已经经历的时间
                passedTime = System.currentTimeMillis() - begin;
            }
        }
        if (null == response) {
            log.debug("等待超时，放弃等待！");
        } else {
            log.debug("获取到了！");
        }
        return response;
    }


    public void setResponse(Object response) {
        synchronized (this) {
            this.response = response;
            this.notifyAll();
        }
    }
}
