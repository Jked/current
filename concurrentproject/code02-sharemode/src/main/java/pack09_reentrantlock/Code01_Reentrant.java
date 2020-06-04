package pack09_reentrantlock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName Code01_Reentrant
 * @Author JK
 * @Date 2020/6/4 16:06
 * @Description ReentrantLock 锁的特性 —— 可重入
 *
 * 可重入：一个线程获取到某个锁之后，可以对该锁进行多次加锁
 * 不可重入：一个线程获取到某个锁之后，再次对该对象进行加锁会阻塞
 */
@Slf4j(topic = "jk.Code01_Reentrant")
public class Code01_Reentrant {

    private static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        lock.lock();
        try {
            log.debug("获取锁1");
            m1();
        }finally {
            lock.unlock();
        }
    }

    public static void m1(){
        lock.lock();
        try {
            log.debug("获取锁2");
            m2();
        }finally {
            lock.unlock();
        }
    }

    public static void m2(){
        lock.lock();
        try {
            log.debug("获取锁3");
        }finally {
            lock.unlock();
        }
    }
}
