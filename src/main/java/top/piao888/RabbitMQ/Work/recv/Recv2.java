package top.piao888.RabbitMQ.Work.recv;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.QueueingConsumer;

import top.piao888.RabbitMQ.Utill.ConnectionUtil;

public class Recv2 {

    private final static String QUEUE_NAME = "test_queue_work";

    public static void main(String[] argv) throws Exception {

        // 获取到连接以及mq通道
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        // 声明队列
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        // 此处的1 代表 同一时刻服务器只会发一条消息给消费者
        		//如果不定义则进行公平分发（公平分发就是 每个消费者分到的消息 是一样的）
        // 在该 消费者发送确认消息之前 消息队列 不会发送消息给该消费者
        channel.basicQos(1);

        // 定义队列的消费者
        QueueingConsumer consumer = new QueueingConsumer(channel);
        // 监听队列，false表示手动返回完成状态，true表示自动
        channel.basicConsume(QUEUE_NAME, false, consumer);

        // 获取消息
        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String message = new String(delivery.getBody());
            System.out.println(" [x] Received '" + message + "'");
            // 休眠1秒
            Thread.sleep(1000999);
            //下面这行注释掉表示使用自动确认模式
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        }
    }
}
