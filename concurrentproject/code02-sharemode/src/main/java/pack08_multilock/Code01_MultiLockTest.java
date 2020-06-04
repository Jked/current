package pack08_multilock;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName Code01_MultiLockTest
 * @Author JK
 * @Date 2020/6/4 15:02
 * @Description 多把锁
 * 1.前面通过 synchronized(lock) 实现了多线程对共享资源的 互斥 操作
 * 2.但是，如果有多个线程操作共享资源中的不同数据，直接锁 整个共享资源 ，并发度太低
 * 3.可以锁住不同对象，细化锁粒度，增强并发度
 * 4.注意：如果一个线程需要同时获取多把锁，可能造成死锁
 */
@Slf4j(topic = "jk.Code01_MultiLockTest")
public class Code01_MultiLockTest {

    public static void main(String[] args) {
        Room room = new Room();
        Thread jk = new Thread(() -> {
            room.sleep();
        }, "jk");
        Thread huaifeng = new Thread(() -> {
            room.study();
        }, "huaifeng");

        jk.start();
        huaifeng.start();
    }
}

/**
 * 共享资源
 */
@Slf4j(topic = "jk.Room")
class Room {
    // 卧室
    private final Object bedRoom = new Object();
    // 书房
    private final Object studyRoom = new Object();

    public void study() {
        synchronized (studyRoom) {
            try {
                log.debug("学习中...");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void sleep() {
        synchronized (bedRoom) {
            try {
                log.debug("睡觉中...");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
