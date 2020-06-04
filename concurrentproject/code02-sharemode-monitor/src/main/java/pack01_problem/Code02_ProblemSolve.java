package pack01_problem;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName Code02_ProblemSolve
 * @Author JK
 * @Date 2020/6/2 17:56
 * @Description 使用 synchronized 关键字解决多线程访问共享资源，因CPU时间片机制造成指令交错 产生的线程安全问题（如：数据覆盖问题）
 */
@Slf4j(topic = "jk.Code02_ProblemSolve")
public class Code02_ProblemSolve {

    private static int count = 0;

    // 锁对象
    private static Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                synchronized (lock) {
                    count++;
                }
            }
        }, "怀风");

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                synchronized (lock) {
                    count--;
                }
            }
        }, "jk");

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        log.debug("次数:{}", count);

    }
}
