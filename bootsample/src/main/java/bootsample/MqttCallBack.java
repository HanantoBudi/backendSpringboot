package bootsample;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bootsample.service.TaskService;

@Service
public class MqttCallBack implements MqttCallback {
	
	@Autowired
	private TaskService taskService;

	@Override
	public void connectionLost(Throwable cause) {
		// TODO Auto-generated method stub
		System.out.print("Connection to mqtt server is lost. " + cause);
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		// TODO Auto-generated method stub
		System.out.print(topic);
		System.out.print(message.toString());
		try {
			if (topic.contains("/mqtt/arrived")) {
				taskService.messageArrived(topic, message);
			}
		} catch (Exception e) {
			throw e;
		}
	}

}
