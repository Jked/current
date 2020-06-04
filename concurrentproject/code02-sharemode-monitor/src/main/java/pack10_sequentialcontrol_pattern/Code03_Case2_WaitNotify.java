package pack10_sequentialcontrol_pattern;

/**
 * @author 怀风
 * @date 2020/6/4 20:24
 * @intro 案例二：三个线程交替打印 a b c
 * wait - notify 版本
 */
public class Code03_Case2_WaitNotify {

    public static void main(String[] args) {
        SyncWaitNotify syncWaitNotify = new SyncWaitNotify(5, 1);
        new Thread(() -> {
            syncWaitNotify.print("a", 1, 2);
        }, "t1").start();
        new Thread(() -> {
            syncWaitNotify.print("b", 2, 3);
        }, "t2").start();
        new Thread(() -> {
            syncWaitNotify.print("c", 3, 1);
        }, "t3").start();
    }
}

class SyncWaitNotify {
    // 循环次数
    private int loopNumber;

    private int initFlag;

    public SyncWaitNotify(int loopNumber, int initFlag) {
        this.loopNumber = loopNumber;
        this.initFlag = initFlag;
    }

    public void print(String word, int flag, int nextFlag) {
        for (int i = 0; i < loopNumber; i++) {
            synchronized (this) {
                while (initFlag != flag) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(word);
                initFlag = nextFlag;
                this.notifyAll();
            }
        }
    }
}
