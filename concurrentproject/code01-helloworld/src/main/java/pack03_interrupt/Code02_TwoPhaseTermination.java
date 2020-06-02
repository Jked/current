package pack03_interrupt;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName Code02_TwoPhaseTermination
 * @Author JK
 * @Date 2020/6/2 14:58
 * @Description 两阶段终止模式
 * 方法：就是通过 interrupt() 方法优雅的停止某个线程；通过判断 打断标记，来给线程一个“料理后事”的机会
 * 场景：模拟一个系统监控器，来监控各种程序的执行；还有 停止监控 的方法。
 *
 *
 * 该模式在后续 volatile 中会继续优化！
 */
@Slf4j(topic = "jk.Code02_TwoPhaseTermination")
public class Code02_TwoPhaseTermination {

    public static void main(String[] args) throws InterruptedException {
        Monitor monitor = new Monitor();
        monitor.start();

        Thread.sleep(4000);

        monitor.stop();
    }
}

@Slf4j(topic = "jk.Monitor")
class Monitor {
    // 监控线程
    private Thread monitor;

    // 开启监控
    public void start() {
        monitor = new Thread(() -> {
            while (true) {
                Thread current = Thread.currentThread();
                if (current.isInterrupted()) {
                    log.debug("料理后事...，退出监控");
                    break;
                }
                try {
                    log.debug("执行监控...");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    // 如果是 sleep() 过程中被打断，就会抛出 InterruptedException 异常并将打断标记置为false；
                    // 所以此处要重新将 打断标记 置为 true
                    current.interrupt();
                }
            }
        }, "monitor");

        monitor.start();
    }

    // 关闭监控
    public void stop() {
        monitor.interrupt();
    }
}