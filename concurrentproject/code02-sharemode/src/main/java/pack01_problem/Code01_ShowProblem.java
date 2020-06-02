package pack01_problem;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName Code01_ShowProblem
 * @Author JK
 * @Date 2020/6/2 17:15
 * @Description 显示问题：由于 分时间片执行导致的线程 安全问题
 * 两个线程，一个加，一个减，最后的结果不是 0
 *
 * 对于静态变量的自增、自减操作，对应的字节码指令如下：（会对应4个指令）
 *
 * 自增：
 *      getstatic i // 获取静态变量i的值
 *      iconst_1 // 准备常量1
 *      iadd // 自增
 *      putstatic i // 将修改后的值存入静态变量i
 *
 * 自减：
 *      getstatic i // 获取静态变量i的值
 *      iconst_1 // 准备常量1
 *      isub // 自减
 *      putstatic i // 将修改后的值存入静态变量i
 */
@Slf4j(topic = "jk.Code01_ShowProblem")
public class Code01_ShowProblem {

    private static int count = 0;

    public static void main(String[] args) throws InterruptedException {

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                count++;
            }
        }, "怀风");

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                count--;
            }
        }, "jk");

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        log.debug("次数:{}", count);
    }

}
