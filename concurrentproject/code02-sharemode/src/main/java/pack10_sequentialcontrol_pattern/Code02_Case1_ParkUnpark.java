package pack10_sequentialcontrol_pattern;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.LockSupport;

/**
 * @ClassName Code02_Case1_ParkUnpark
 * @Author JK
 * @Date 2020/6/4 18:00
 * @Description 控制打印顺序，要求：先打印2 ，再打印1
 * <p>
 * park - unpark 版本
 */
@Slf4j(topic = "jk.Code02_Case1_ParkUnpark")
public class Code02_Case1_ParkUnpark {

    public static void main(String[] args) {

        Thread t1 = new Thread(() -> {
            LockSupport.park();
            log.debug("1");
        }, "t1");

        Thread t2 = new Thread(() -> {
            log.debug("2");
            LockSupport.unpark(t1);
        }, "t2");

        t1.start();
        t2.start();
    }
}
