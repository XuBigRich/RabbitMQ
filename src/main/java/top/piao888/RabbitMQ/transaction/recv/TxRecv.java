package top.piao888.RabbitMQ.transaction.recv;

import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import top.piao888.RabbitMQ.Utill.ConnectionUtil;

public class TxRecv {
	private final static String QUEUE_NAME = "test_queue_tx";

	public static void main(String[] args) throws Exception {
		Connection connection = ConnectionUtil.getConnection();
		Channel channel = connection.createChannel();
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {
				String message=new String(body,"utf-8");
				System.out.println(message);
			}
		};
		channel.basicConsume(QUEUE_NAME, true,defaultConsumer);
	}
}
