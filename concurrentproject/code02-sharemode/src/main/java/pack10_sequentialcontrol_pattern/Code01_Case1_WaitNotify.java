package pack10_sequentialcontrol_pattern;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @ClassName Code01_Case1_WaitNotify
 * @Author JK
 * @Date 2020/6/4 17:45
 * @Description 两个线程，一个打印 1  ，另一个打印 2；
 * 控制打印顺序，要求：先打印2 ，再打印1
 * <p>
 * wait - notify 版本
 */
@Slf4j(topic = "jk.Code01_Case1_WaitNotify")
public class Code01_Case1_WaitNotify {
    private static Object lock = new Object();
    private static boolean flag;

    public static void main(String[] args) {

        Thread t1 = new Thread(() -> {
            synchronized (lock) {
                while (!flag) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.debug("1");
            }
        }, "t1");

        Thread t2 = new Thread(() -> {
            synchronized (lock) {
                log.debug("2");
                flag = true;
                lock.notify();
            }
        }, "t2");

        t1.start();
        t2.start();
    }
}
