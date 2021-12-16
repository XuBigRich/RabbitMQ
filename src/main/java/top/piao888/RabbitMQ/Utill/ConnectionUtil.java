package top.piao888.RabbitMQ.Utill;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class ConnectionUtil {
	public static Connection getConnection() throws Exception{
		ConnectionFactory factory=new ConnectionFactory();
//		factory.setHost("47.104.92.214");
//		factory.setHost("127.0.0.1");
		factory.setHost("172.16.19.137");
		//端口
		factory.setPort(5672);
		factory.setUsername("scrm_admin");
		factory.setPassword("admin123");
//		factory.setVirtualHost("vhost_first");
		//通过工程获取连接
		Connection connection=factory.newConnection();
		return connection;
	}
	
}
