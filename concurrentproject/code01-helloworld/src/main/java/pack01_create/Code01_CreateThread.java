package pack01_create;

/**
 * @ClassName pack01_create.Code01_CreateThread
 * @Author JK
 * @Date 2020/6/2 10:26
 * @Description 创建线程方式一
 * 继承Thread类，本例中直接创建了一个匿名内部类对象
 */
public class Code01_CreateThread {

    public static void main(String[] args) {

        Thread thread = new Thread() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + "线程输出打印");
            }
        };
        // 起名字
        thread.setName("jk");

        // 启动线程
        thread.start();
    }

}
