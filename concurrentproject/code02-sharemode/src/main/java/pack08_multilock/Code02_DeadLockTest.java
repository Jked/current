package pack08_multilock;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName Code02_DeadLockTest
 * @Author JK
 * @Date 2020/6/4 15:20
 * @Description 线程活跃性——死锁
 */
@Slf4j(topic = "jk.Code02_DeadLockTest")
public class Code02_DeadLockTest {

    public static void main(String[] args) {
        Object lockA = new Object();
        Object lockB = new Object();

        new Thread(() -> {
            synchronized (lockA) {
                try {
                    log.debug("获得锁A");
                    Thread.sleep(1000);
                    synchronized (lockB) {
                        log.debug("获得锁B");
                        log.debug("t1线程执行操作！");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "t1").start();

        new Thread(() -> {
            synchronized (lockB) {
                try {
                    log.debug("获得锁B");
                    Thread.sleep(500);
                    synchronized (lockA) {
                        log.debug("获得锁A");
                        log.debug("t2线程执行操作！");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "t2").start();
    }
}
