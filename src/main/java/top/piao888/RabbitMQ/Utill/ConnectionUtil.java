package top.piao888.RabbitMQ.Utill;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class ConnectionUtil {
	public static Connection getConnection() throws Exception{
		ConnectionFactory factory=new ConnectionFactory();
		factory.setHost("localhost");
		//端口
		factory.setPort(5672);
		factory.setUsername("BigRich");
		factory.setPassword("123");
		factory.setVirtualHost("vhost_first");
		//通过工程获取连接
		Connection connection=factory.newConnection();
		return connection;
	}
	
}
