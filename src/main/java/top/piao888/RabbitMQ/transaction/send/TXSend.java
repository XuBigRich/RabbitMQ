package top.piao888.RabbitMQ.transaction.send;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import top.piao888.RabbitMQ.Utill.ConnectionUtil;

/**
 *  rabbitmq 的事务 模式 产生异常 回滚     
 *  弊端： 会产生大量的 消息 传送给服务器 ，降低消息的吞吐量
 * @author ADMIN
 */
public class TXSend {

	private final static String QUEUE_NAME = "test_queue_tx";

	public static void main(String[] argv) throws Exception {
		// 获取到连接以及mq通道
		Connection connection = ConnectionUtil.getConnection();
		Channel channel = connection.createChannel();
		channel.basicQos(1);
		// 声明队列 第二个参数 为是否持久化 只能持久化队列 不可以持久化消息
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);

		String message = "Hello World";
		try {
			//开启事务模式
			channel.txSelect();
			channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
			int xx=1/0;
			//如果没有异常则提交
			channel.txCommit();
		}catch(Exception e) {
			//存在异常则回滚
			channel.txRollback();
			System.out.println("send message txRollback");
		}
		
		channel.close();
		connection.close();
	}
}
