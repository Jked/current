package pack10_sequentialcontrol_pattern;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author 怀风
 * @date 2020/6/4 20:34
 * @intro 案例二：三个线程交替打印 a b c
 * ReentrantLock 版本
 */
public class Code04_Case2_ReentrantLock {
    public static void main(String[] args) {
        AwaitSignal awaitSignal = new AwaitSignal(5, "a");
        Condition a = awaitSignal.newCondition();
        Condition b = awaitSignal.newCondition();
        Condition c = awaitSignal.newCondition();
        new Thread(() -> {
            awaitSignal.print("a", "b", a, b);
        }, "t1").start();
        new Thread(() -> {
            awaitSignal.print("b", "c", b, c);
        }, "t2").start();
        new Thread(() -> {
            awaitSignal.print("c", "a", c, a);
        }, "t3").start();
    }
}

class AwaitSignal extends ReentrantLock {

    private int loopNumber;

    private String initWord;

    public AwaitSignal(int loopNumber, String initWord) {
        this.loopNumber = loopNumber;
        this.initWord = initWord;
    }

    public void print(String word, String nextWord, Condition current, Condition next) {
        for (int i = 0; i < loopNumber; i++) {
            this.lock();
            try {
                while (!word.equals(initWord)) {
                    try {
                        current.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(word);
                initWord = nextWord;
                next.signal();
            } finally {
                this.unlock();
            }
        }
    }
}