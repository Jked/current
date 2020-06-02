package pack03_interrupt;

import lombok.extern.slf4j.Slf4j;

/**
 * @author JK
 * @class Code01_InterruptTest
 * @date 2020/6/2 14:11
 * @description 测试使用 interrupt() 方法打断线程
 * 1.如果被打断线程正在 sleep，wait，join 会导致被打断的线程抛出 InterruptedException，并清除 打断标记 （即：重置为false）；
 * 2.如果打断的正在运行的线程，则会设置 打断标记为 true，但不会终止线程；需要手动判断打断标记来手动退出（优雅停止线程）
 * 3.park 的线程被打断，也会设置 打断标记
 */
@Slf4j(topic = "jk.Code01_InterruptTest")
public class Code01_InterruptTest {

    public static void main(String[] args) throws InterruptedException {

        Thread jk = new Thread(() -> {
            try {
                log.debug("enter sleep...");
                Thread.sleep(2000);  // wait()、 join()
            } catch (InterruptedException e) {
                e.printStackTrace();
                log.debug("weak up!");
            }
        }, "jk");
        jk.start();

        // 主线程睡眠一秒
        Thread.sleep(1000);
        // 打断 jk 线程
        jk.interrupt();

        // 调用 isInterrupted() 方法查看打断标记，会打印 false，因为阻塞状态下打断线程会清除 打断标记（false）
        log.debug("jk线程的打断标记：{}", jk.isInterrupted());
    }

}
