package pack03_waitnotify;

import lombok.extern.slf4j.Slf4j;

/**
 * @author 怀风
 * @date 2020/6/3 16:53
 * @intro 说明使用 wait - notify 的最佳实践
 */
@Slf4j(topic = "jk.Code01_BestPractice")
public class Code01_BestPractice {

    // 锁对象
    private static final Object room = new Object();

    private static boolean hasCigarette = false;
    private static boolean hasTakeout = false;

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            synchronized (room) {
                log.debug("进入房间");
                // 通过 while 循环防止虚假唤醒，因为下面是用 notifyAll() 唤醒所有线程
                while (!hasCigarette) {
                    log.debug("没烟，干不了活");
                    try {
                        room.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.debug("有烟了，可以干活...");
            }
        }, "jk").start();

        new Thread(() -> {
            synchronized (room) {
                log.debug("进入房间");
                // 通过 while 循环防止虚假唤醒，因为下面是用 notifyAll() 唤醒所有线程
                while (!hasTakeout) {
                    log.debug("没外卖，干不了活");
                    try {
                        room.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.debug("有饭吃了，可以干活...");
            }
        }, "怀风").start();

        Thread.sleep(2000);
        new Thread(()->{
            synchronized (room){
                log.debug("送外卖的来了！");
                hasTakeout = true;
                // 使用 notifyAll() 来唤醒所有等待的线程
                room.notifyAll();
            }
        },"送外卖的").start();

        Thread.sleep(2000);
        new Thread(()->{
            synchronized (room){
                log.debug("送烟的来了！");
                hasCigarette = true;
                // 使用 notifyAll() 来唤醒所有等待的线程
                room.notifyAll();
            }
        },"送烟的").start();

    }
}
