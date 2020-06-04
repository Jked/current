package pack03_interrupt_pattern;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.LockSupport;

/**
 * @ClassName Code03_InterruptPark
 * @Author JK
 * @Date 2020/6/2 15:18
 * @Description park() 是 LockSupport 工具类下的一个方法
 * 作用：让线程暂停，可以通过  interrupt() 来打断 park() 的暂停，并将打断标记设为 true
 * <p>
 * LockSupport.park() 方法只能执行一次，一旦打断并恢复线程执行后，再调用一次 LockSupport.park() 也不会让线程暂停
 * 因为 打断标记已经是 true 了
 *
 * 要想要第二次打断继续生效，需要将打断标记设为 false，比如使用静态方法：interrupted()
 */
@Slf4j(topic = "jk.Code03_InterruptPark")
public class Code03_InterruptPark {

    public static void main(String[] args) throws InterruptedException {
        Thread jk = new Thread(() -> {
            log.debug("进入park暂停");
            LockSupport.park();
            log.debug("打断park暂停，继续执行，打断状态：{}",Thread.interrupted());
//            log.debug("打断park暂停，继续执行，打断状态：{}",Thread.currentThread().isInterrupted());

            log.debug("尝试第二次打断");
            LockSupport.park();
            log.debug("第二次打断失败....");
        }, "jk");

        jk.start();

        Thread.sleep(1000);
        jk.interrupt();
    }
}
