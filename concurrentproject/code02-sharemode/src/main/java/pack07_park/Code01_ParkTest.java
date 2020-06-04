package pack07_park;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;

/**
 * @ClassName Code01_ParkTest
 * @Author JK
 * @Date 2020/6/4 14:13
 * @Description park 和 unpark
 * <p>
 * 与 Object 的 wait & notify 相比
 * 1.wait，notify 和 notifyAll 必须配合 Object Monitor 一起使用，而 park，unpark 不必
 * 2.park & unpark 是以线程为单位来【阻塞】和【唤醒】线程，而 notify 只能随机唤醒一个等待线程，notifyAll是唤醒所有等待线程，就不那么【精确】
 * 3.park & unpark 可以先 unpark，而 wait & notify 不能先 notify
 * <p>
 * 注意：如果在某个线程执行 park() 之前对该线程先执行了 unpark()，则在 park() 方法执行时也不会阻塞线程
 */
@Slf4j(topic = "jk.Code01_ParkTest")
public class Code01_ParkTest {
    public static void main(String[] args) throws InterruptedException {
        Thread jk = new Thread(() -> {
            log.debug("jk线程开始执行");
            log.debug("jk线程第一次尝试暂停...");
            LockSupport.park();
            log.debug("jk线程恢复第一次暂停...");

            log.debug("jk线程第二次尝试暂停...");
            LockSupport.park();
            log.debug("jk线程恢复第二次暂停...");
        }, "jk");

        jk.start();

        // 使用 unpark() 来恢复线程，如果再次遇到了 park() 方法仍会暂停
        LockSupport.unpark(jk);

        // 使用 interrupt() 来恢复线程，如果再次遇到了 park() 方法不会再暂停！！
        // jk.interrupt();
    }
}
