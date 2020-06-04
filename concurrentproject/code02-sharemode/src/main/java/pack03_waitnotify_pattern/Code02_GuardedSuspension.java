package pack03_waitnotify_pattern;

import lombok.extern.slf4j.Slf4j;

/**
 * @author 怀风
 * @date 2020/6/3 17:08
 * @intro 保护性暂停 模式
 * 用于 一个线程 需要接收 另一个线程 产生的结果，因为需要等待结果，所以是同步的一种设计模式
 * JDK中的 join 和 Future 就是使用的这样模式
 * <p>
 * 如何理解 保护性暂停？
 *      暂停就是指的 wait() ，当条件不满足时，通过 wait() 进行等待，直到条件满足，这就是 保护性暂停
 *
 * 特点：
 *      生成线程 和 消费线程 是一一对应的
 * <p>
 * 注意：
 * 只适用于一个结果的生产和消费；
 * 如果有结果源源不断的从一个线程到另一个线程，应该使用 生产者 / 消费者 模式
 *
 * 和join相比，优势：
 * （1）join必须等待一个线程彻底执行结束，另一个线程才可以运行；
 *      而此例中，生产线程生产出产品后，唤醒消费者，但是生产线程还是可以继续向下执行其他代码
 * （2）使用 join，两个线程的交互的变量必须是全局的，而此例可以通过局部变量，通过方法返回的形式获取到另一个线程的执行结果！
 */
@Slf4j(topic = "jk.Code02_GuardedSuspension")
public class Code02_GuardedSuspension {

    public static void main(String[] args) throws InterruptedException {

        GuardedObject gurade = new GuardedObject();

        // 消费线程
        new Thread(() -> {
            log.debug("获取到产品：{}", gurade.getResponse());
        }, "consumer").start();


        Thread.sleep(1000);
        // 生产线程
        new Thread(() -> {
            gurade.setResponse("精品卫衣");
        }, "producer").start();

    }
}

/**
 * 用于传递结果的 中间对象
 */
@Slf4j(topic = "jk.GuardedObject")
class GuardedObject {

    private Object response;


    public Object getResponse() {
        synchronized (this) {
            while (response == null) {
                log.debug("获取为空，进入等待");
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        log.debug("获取到了！");
        return response;
    }


    public void setResponse(Object response) {
        synchronized (this) {
            this.response = response;
            log.debug("开始生产:{}", response);
            this.notifyAll();
        }
    }
}
