package top.piao888.RabbitMQ.Subscribe.topic.recv;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import top.piao888.RabbitMQ.Utill.ConnectionUtil;

/*注意：消息发送到没有队列绑定的交换机时，消息将丢失，因为，交换机没有存储消息的能力，消息只能存在在队列中。*/
public class Recv {

	private final static String QUEUE_NAME = "test_queue_work5";

	private final static String EXCHANGE_NAME = "test_exchange_topic";

	public static void main(String[] argv) throws Exception {

		// 获取到连接以及mq通道
		Connection connection = ConnectionUtil.getConnection();
		final Channel channel = connection.createChannel();
		// 声明交换机,但是消费者无需声明交换机,可直接用队列绑定已有的交换机,来进行接收路有消息
		channel.exchangeDeclare(EXCHANGE_NAME, "topic");
		// 声明队列
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);

		// 绑定队列到交换机
		channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "Rich.Good.delete");
		channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "#.rabbit.#");
		// 同一时刻服务器只会发一条消息给消费者
		channel.basicQos(1);

		// 定义队列的消费者
		DefaultConsumer consumer = new DefaultConsumer(channel) {
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {
				String msg;
				try {
					msg = new String(body, "utf-8");
					System.out.println(msg);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} finally {
					channel.basicAck(envelope.getDeliveryTag(), false);
				}
			}
		};
		// 监听队列，手动返回完成
		channel.basicConsume(QUEUE_NAME, false, consumer);
	}
}
