package pack09_reentrantlock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName Code03_LockTimeout
 * @Author JK
 * @Date 2020/6/4 16:25
 * @Description ReentrantLock 锁的特性 —— 获取锁超时
 * 可以通过设置超时时间，在时间限制内尝试获取锁，超时则主动放弃锁的等待，返回false
 * 如果在超时时间内获取到了锁，返回 true
 * <p>
 * 方法：
 * (1) tryLock()：执行到该方法时，去尝试获取锁一次，成功返回 true；失败返回 false
 * (2) tryLock(long timeout, TimeUnit unit)：在 timeout 时间内尝试获取锁，在等待时间内可以被 interrupt() 打断
 * <p>
 * 使用 tryLock() 可以解决死锁问题，比如：哲学家就餐问题
 */
@Slf4j(topic = "jk.Code03_LockTimeout")
public class Code03_LockTimeout {

    private static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {

//        Thread jk = new Thread(() -> {
//            log.debug("尝试获取锁");
//            if (!lock.tryLock()) {
//                log.debug("获取锁失败");
//                return;
//            }
//            try {
//                log.debug("获取锁成功");
//            } finally {
//                lock.unlock();
//            }
//        }, "jk");
//
//        log.debug("获取锁");
//        lock.lock();
//        jk.start();


        Thread jk = new Thread(() -> {
            log.debug("尝试获取锁");
            try {
                if (!lock.tryLock(2, TimeUnit.SECONDS)) {
                    log.debug("获取锁失败");
                    return;
                }
            }
            // 等待时间内被打断，抛出异常
            catch (InterruptedException e) {
                log.debug("获取锁失败");
                e.printStackTrace();
                return;
            }

            try {
                log.debug("获取锁成功");
            } finally {
                lock.unlock();
            }
        }, "jk");

        log.debug("获取锁");
        lock.lock();
        jk.start();

        // 主线程1秒后释放锁
        Thread.sleep(1000);
        log.debug("释放锁");
        lock.unlock();

    }
}
