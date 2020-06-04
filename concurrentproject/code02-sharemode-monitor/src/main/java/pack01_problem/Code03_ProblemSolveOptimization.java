package pack01_problem;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName Code03_ProblemSolveOptimization
 * @Author JK
 * @Date 2020/6/2 17:56
 * @Description 用面向对象的思想进行优化
 */
@Slf4j(topic = "jk.Code03_ProblemSolveOptimization")
public class Code03_ProblemSolveOptimization {

    public static void main(String[] args) throws InterruptedException {

        Room room = new Room();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                room.increment();
            }
        }, "怀风");

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
               room.decrement();
            }
        }, "jk");

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        log.debug("次数:{}", room.getCount());

    }
}

class Room{

    private int count = 0;

    public void increment(){
        synchronized (this){
            count++;
        }
    }

    public void decrement(){
        synchronized (this){
            count--;
        }
    }

    // 获取count的值，也需要加锁同步；防止一个线程++了一半发生了上下文切换，此时如果我们获取，就不是正确的值了！因为有一次还没++完；
    // 所以获取方法也需要获取锁才行！
    public int getCount(){
        synchronized (this){
            return count;
        }
    }
}
