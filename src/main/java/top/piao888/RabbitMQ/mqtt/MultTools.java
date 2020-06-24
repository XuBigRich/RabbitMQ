package top.piao888.RabbitMQ.mqtt;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

public class MultTools implements Runnable {
	public ServerMQTT serverMQTT;
	public String message;
	public MultTools(ServerMQTT serverMQTT, String message) {
		this.serverMQTT = serverMQTT;
		this.message = message;
	}
	public void run() {
		serverMQTT.setMessage(new MqttMessage());
		serverMQTT.getMessage().setQos(1);	
		serverMQTT.getMessage().setPayload(message.getBytes());
		serverMQTT.getMessage().setRetained(true);
		try {
			while(true) {
				serverMQTT.publish(ServerMQTT.topic11, serverMQTT.getMessage());
			}
		} catch (MqttPersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args) throws MqttException {
		ServerMQTT serverMQTT1=new ServerMQTT();
		String message1 = "{\"DevNum\":1,\"SN_\":\"1234\",\"Datetime\":1588993563,\"RealTime-Data\":{\"Ua\":220.1,\"Ub\":0,\"Uc\":0,\"Ia\":0.04,\"Ib\":0.04,\"Ic\":0.05,\"In\":0.08,\"P\":185.0,\"Q\":0,\"PF\":1,\"Wh\":20.67,\"Varh\":2.77,\"Ta\":6503.6,\"Tb\":6503.6,\"Tc\":6503.6,\"FWh\":19.08,\"BWh\":1954}} ";
		String message2 = "{\"DevNum\":2,\"SN_\":\"1234\",\"Datetime\":1588993563,\"RealTime-Data\":{\"Ua\":220.1,\"Ub\":0,\"Uc\":0,\"Ia\":0.04,\"Ib\":0.04,\"Ic\":0.05,\"In\":0.08,\"P\":50,\"Q\":0,\"PF\":1,\"Wh\":20.67,\"Varh\":2.77,\"Ta\":6503.6,\"Tb\":6503.6,\"Tc\":6503.6,\"FWh\":19.08,\"BWh\":1954}} ";
		MultTools multTools1=new MultTools(serverMQTT1, message1);
		MultTools multTools2=new MultTools(serverMQTT1, message2);
		MultTools multTools3=new MultTools(serverMQTT1, message1);
		MultTools multTools4=new MultTools(serverMQTT1, message2);
		MultTools multTools5=new MultTools(serverMQTT1, message1);
		MultTools multTools6=new MultTools(serverMQTT1, message2);
		MultTools multTools7=new MultTools(serverMQTT1, message1);
		MultTools multTools8=new MultTools(serverMQTT1, message2);
		Thread t1=new Thread(multTools1);
		t1.start();
		Thread t2=new Thread(multTools2);
		t2.start();
		Thread t3=new Thread(multTools3);
		t3.start();
		Thread t4=new Thread(multTools4);
		t4.start();
		Thread t5=new Thread(multTools5);
		t5.start();
		Thread t6=new Thread(multTools6);
		t6.start();
		Thread t7=new Thread(multTools7);
		t7.start();
		Thread t8=new Thread(multTools8);
		t8.start();
		
	}

}
