package pack09_reentrantlock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName Code02_Interruptibly
 * @Author JK
 * @Date 2020/6/4 16:12
 * @Description ReentrantLock 锁的特性 —— 获取锁可打断
 * 在获取锁时，如果没有获取到锁会陷入阻塞状态；其他线程可以通过调用 阻塞线程的 interrupt() 方法来打断阻塞，放弃获取锁
 * 方法：lockInterruptibly()
 * 通过 thread.intterpt() 方法打断
 * <p>
 * 使用 synchronized 和 lock()方法是不可被打断的
 */
@Slf4j(topic = "jk.Code02_Interruptibly")
public class Code02_Interruptibly {

    private static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {

        Thread jk = new Thread(() -> {
            log.debug("尝试获取锁");
            try {
                lock.lockInterruptibly();
            } catch (InterruptedException e) {
                log.debug("被打断，获取锁失败");
                e.printStackTrace();
                return;
            }
            try {
                log.debug("成功获取到锁");
            } finally {
                lock.unlock();
            }
        }, "jk");

        // 主线程先获得锁，再启动 jk 线程
        lock.lock();
        jk.start();

        // 1秒后打断 jk 线程的等待
        Thread.sleep(1000);
        jk.interrupt();
    }
}
