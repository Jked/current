package pack02_transfer;

import lombok.extern.slf4j.Slf4j;

/**
 * @author 怀风
 * @date 2020/6/3 11:19
 * @intro 转账问题
 */
@Slf4j(topic = "jk.Code01_AccountTest")
public class Code01_AccountTest {
    public static void main(String[] args) throws InterruptedException {
        Account account1 = new Account(1000);
        Account account2 = new Account(1000);

        Thread jk = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                account1.transfer(account2, 10);
            }
        }, "jk");

        Thread huaifeng = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                account2.transfer(account1, 10);
            }
        }, "huaifeng");

        jk.start();
        huaifeng.start();

        jk.join();
        huaifeng.join();

        log.info("总余额：{}", account1.getMoney() + account2.getMoney());
    }
}

/**
 * 账户类
 */
class Account {
    // 账户余额
    private int money;

    public Account(int money) {
        this.money = money;
    }

    public int getMoney() {
        return money;
    }

    private void setMoney(int setMoney) {
        this.money = setMoney;
    }

    /**
     * 下面的代码需要进行互斥操作
     * 此处的共享资源是：本对象中的money 以及 要转账的target对象中的 money
     * 共享变量处在两个不同的对象中，所以不能锁 this 对象；
     * 应该使用两个对象共同的锁：此处使用 Account.class
     *
     * @param target
     * @param transMoney
     */
    public void transfer(Account target, int transMoney) {
        synchronized (Account.class) {
            if (this.money >= transMoney) {
                this.setMoney(money - transMoney);
                target.setMoney(target.getMoney() + transMoney);
            }
        }
    }
}
