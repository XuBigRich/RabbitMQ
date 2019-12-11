package top.piao888.RabbitMQ.Work.send;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import top.piao888.RabbitMQ.Utill.ConnectionUtil;
/**
 * 
 * @author ADMIN
 *	一个生产者、2个消费者。
 * 	一个消息只能被一个消费者获取。
 */
public class Send {

    private final static String QUEUE_NAME = "test_queue_work";

    public static void main(String[] argv) throws Exception {
        // 获取到连接以及mq通道
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        channel.basicQos(1);
        // 声明队列 第二个参数 为是否持久化  只能持久化队列 不可以持久化消息
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        for (int i = 0; i < 100; i++) {
            // 消息内容
            String message = "" + i;
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");

            Thread.sleep(10);
        }

        channel.close();
        connection.close();
    }
}
