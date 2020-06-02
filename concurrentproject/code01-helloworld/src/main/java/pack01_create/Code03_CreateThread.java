package pack01_create;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @ClassName pack01_create.Code03_CreateThread
 * @Author JK
 * @Date 2020/6/2 10:45
 * @Description 创建线程的第三种方式：FutureTask
 * FutureTask 其实继承了 Runnable 接口，所以同样可以作为任务传给 Thread
 * 并且，FutureTask 对象的构造必须接收一个对象（没有无参构造）：
 * 要么是 Runnable 类型，要么是 Callable 类型；Callable 接口中的 call()方法有返回值，并且可以抛出异常
 *
 * 注意：其他线程可以通过 task.get() 方法来获取执行结果，在结果没出来之前会被阻塞
 */

@Slf4j(topic = "jk.Code03_CreateThread")
public class Code03_CreateThread {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        // 泛型中描述 Callable 返回结果的类型
        FutureTask<Integer> task = new FutureTask<>(() -> {
            log.debug("FutureTask配合Callable正在执行...");
            int i = 1 + 1;
            Thread.sleep(2000);
            return i;
        });

        // 传入 task 并执行
        Thread run3 = new Thread(task, "jk-run3");
        run3.start();


        // 主线程中可以通过 task.get() 的方式获取到子线程的执行结果
        // 主线程执行到此处，如果此时子线程还没有执行结束，主线程会在这里阻塞，一直等到结果返回
        Integer i = task.get();
        log.debug("future-task执行结果：{}", i);

        log.debug("主线程等待结束，继续执行");
    }
}
