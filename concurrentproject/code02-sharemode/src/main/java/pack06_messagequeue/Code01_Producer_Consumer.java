package pack06_messagequeue;

import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @ClassName Code01_Producer_Consumer
 * @Author JK
 * @Date 2020/6/4 11:06
 * @Description TODO
 */
@Slf4j(topic = "jk.Code01_Producer_Consumer")
public class Code01_Producer_Consumer {

    public static void main(String[] args) throws InterruptedException {
        log.debug("开始！");
        MessageQueue queue = new MessageQueue(3);

        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    Message take = queue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, "消费者" + i).start();
        }

        new Thread(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    Thread.sleep(400);
                    queue.put(new Message(i, "消息" + i));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "生产者").start();
    }
}

// 消息队列
@Slf4j(topic = "jk.MessageQueue")
class MessageQueue {
    // 队列：使用 双向链表
    private Queue<Message> queue = new LinkedList<>();

    // 队列容量
    private int capacity;

    public MessageQueue(int capacity) {
        this.capacity = capacity;
    }

    // 获取消息
    public Message take() {
        synchronized (queue) {
            while (queue.isEmpty()) {
                try {
                    log.debug("队列为空，消费者等待消息");
                    queue.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Message message = queue.poll();
            log.debug("消费者消费了：{}", message);
            queue.notifyAll();
            return message;
        }
    }

    // 存放消息
    public void put(Message message) {
        synchronized (queue) {
            while (capacity == queue.size()) {
                try {
                    log.debug("队列已满，生产者等待");
                    queue.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            queue.offer(message);
            log.debug("生产者生产了：{}", message);
            queue.notifyAll();
        }
    }

}


// 用于在消息队列中存放的消息对象
class Message {
    private Integer id;
    private Object value;

    public Message(Integer id, Object value) {
        this.id = id;
        this.value = value;
    }

    public Integer getId() {
        return id;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", value=" + value +
                '}';
    }
}