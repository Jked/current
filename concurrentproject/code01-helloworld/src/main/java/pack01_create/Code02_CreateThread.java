package pack01_create;

/**
 * @ClassName pack01_create.Code02_CreateThread
 * @Author JK
 * @Date 2020/6/2 10:36
 * @Description 创建线程第二种方式：任务 和 线程分离，使用 Runnable
 */
public class Code02_CreateThread {
    public static void main(String[] args) {

        // 定义任务代码
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("通过runnable设置任务");
            }
        };
        Thread run1 = new Thread(runnable, "jk-run1");
        run1.start();

        // ==========也可以使用lambda表达式=========

        Runnable run = () -> System.out.println("通过lambda设置任务");
        Thread run2 = new Thread(run, "jk-run2");
        run2.start();

    }
}
