package pack10_sequentialcontrol_pattern;

import java.util.concurrent.locks.LockSupport;

/**
 * @author 怀风
 * @date 2020/6/4 21:00
 * @intro 案例二：三个线程交替打印 a b c
 * park - unpark 版本
 */
public class Code05_Case2_ParkUnpark {

    private static Thread t1 = null;
    private static Thread t2 = null;
    private static Thread t3 = null;

    public static void main(String[] args) {

        ParkUnpark parkUnpark = new ParkUnpark(5);

        t1 = new Thread(() -> {
            parkUnpark.print("a", t2);
        }, "t1");

        t2 = new Thread(() -> {
            parkUnpark.print("b", t3);
        }, "t1");

        t3 = new Thread(() -> {
            parkUnpark.print("c", t1);
        }, "t1");

        t1.start();
        t2.start();
        t3.start();

        LockSupport.unpark(t1);
    }
}

class ParkUnpark {

    private int loopNumber;

    public ParkUnpark(int loopNumber) {
        this.loopNumber = loopNumber;
    }

    public void print(String word, Thread nextThread) {
        for (int i = 0; i < loopNumber; i++) {
            LockSupport.park();
            System.out.println(word);
            LockSupport.unpark(nextThread);
        }
    }
}