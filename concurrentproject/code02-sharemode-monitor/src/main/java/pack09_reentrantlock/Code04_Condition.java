package pack09_reentrantlock;

import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName Code04_Condition
 * @Author JK
 * @Date 2020/6/4 17:09
 * @Description ReentrantLock 锁的特性 —— 条件变量
 * 不同于 Synchronized 只有一个 WaitSet，ReentrantLock 中支持多个 休息室（Condition）
 * <p>
 * 使用 ReentrantLock 的 Condition 优化之前的一个实例
 */
@Slf4j(topic = "jk.Code04_Condition")
public class Code04_Condition {

    private static boolean hasCigarette = false;
    private static boolean hasTakeout = false;

    private static ReentrantLock room = new ReentrantLock();
    private static Condition cigaretteCondition = room.newCondition();
    private static Condition takeoutCondition = room.newCondition();

    public static void main(String[] args) throws InterruptedException {

        new Thread(() -> {
            room.lock();
            try {
                log.debug("进入房间");
                // 通过 while 循环防止虚假唤醒，因为下面是用 notifyAll() 唤醒所有线程
                while (!hasCigarette) {
                    log.debug("没烟，干不了活");
                    try {
                        cigaretteCondition.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.debug("有烟了，可以干活...");
            } finally {
                room.unlock();
            }
        }, "jk").start();

        new Thread(() -> {
            room.lock();
            try {
                log.debug("进入房间");
                // 通过 while 循环防止虚假唤醒，因为下面是用 notifyAll() 唤醒所有线程
                while (!hasTakeout) {
                    log.debug("没外卖，干不了活");
                    try {
                        takeoutCondition.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.debug("有饭吃了，可以干活...");
            } finally {
                room.unlock();
            }
        }, "怀风").start();

        Thread.sleep(2000);
        new Thread(() -> {
            room.lock();
            try {
                log.debug("送外卖的来了！");
                hasTakeout = true;
                takeoutCondition.signal();
            } finally {
                room.unlock();
            }
        }, "送外卖的").start();

        Thread.sleep(1000);
        new Thread(() -> {
            room.lock();
            try {
                log.debug("送烟的来了！");
                hasCigarette = true;
                cigaretteCondition.signal();
            } finally {
                room.unlock();
            }
        }, "送烟的").start();
    }
}