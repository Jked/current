#《join的原理》
* 之前说到JDK中join 和 future 使用到了保护性暂停模式
* 和保护性暂停模式不同的是，join是等待线程结束，而前面使用的保护性暂停模式是等待结果返回

##源码如下
```java
// 空参join底层会调用有时限的 join
public final void join() throws InterruptedException {
    join(0);
}
```
join(long millis)方法源码如下：
```java
public final synchronized void join(long millis)
throws InterruptedException {
    // 记录开始时间
    long base = System.currentTimeMillis();
    // 定义已经经历的时间
    long now = 0;

    if (millis < 0) {
        throw new IllegalArgumentException("timeout value is negative");
    }

    // 如果等待时间是0，调用 wait(0); 方法表示无时限的等待
    if (millis == 0) {
        while (isAlive()) {
            wait(0);
        }
    } else {
        while (isAlive()) {
            // 计算还需要等待的时间
            long delay = millis - now;
            if (delay <= 0) {
                break;
            }
            // 调用有时限的 wait() 方法
            wait(delay);
            now = System.currentTimeMillis() - base;
        }
    }
}
```