package bootsample.service;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bootsample.MqttConfigData;

@Service
public class MqttService {

	@Autowired
	private MqttConfigData mqttConfigData;

	private MqttClient mqttClient;
	
	public MqttClient connect() {
		if (mqttClient != null) {
			int attempt = 0;
			while (!mqttClient.isConnected() && attempt < 3) {
				try {
					mqttClient.reconnect();
					break;
				} catch (MqttException e) {
					attempt = attempt + 1;
					try {
						Thread.sleep(100 * attempt);
					} catch (InterruptedException e1) {
						// ignored
					}
				}
			}
			return mqttClient;
		} else {
			return createNewConnection();
		}
	}

	public MqttClient createNewConnection() {
		String broker = "tcp://" + mqttConfigData.getBrokerHost() + ":" + mqttConfigData.getBrokerPort();
		MemoryPersistence persistence = new MemoryPersistence();
		try {
			mqttClient = new MqttClient(broker, mqttConfigData.getClientName(), persistence);
			if (!mqttClient.isConnected()) {
				MqttConnectOptions connOpts = new MqttConnectOptions();
				if (mqttConfigData.getBrokerUsername() != null) {
					if (!mqttConfigData.getBrokerUsername().isEmpty()) {
						connOpts.setUserName(mqttConfigData.getBrokerUsername());
					}
				}
				if (mqttConfigData.getBrokerPassword() != null) {
					if (!mqttConfigData.getBrokerPassword().isEmpty()) {
						connOpts.setPassword(mqttConfigData.getBrokerPassword().toCharArray());
					}
				}
				connOpts.setMaxInflight(100);
				connOpts.setCleanSession(false);
				connOpts.setAutomaticReconnect(true);
				mqttClient.connect(connOpts);
			}
			return mqttClient;
		} catch (RuntimeException e) {
			throw e;
		} catch (MqttException me) {
			return null;
		}
	}

	public boolean sendMessage(String topic, String content) {
		try {
			int qos = mqttConfigData.getQos();
			MqttMessage message = new MqttMessage(content.getBytes());
			message.setQos(qos);
			mqttClient.publish(topic, message);
			return true;
		} catch (MqttException me) {
			return false;
		}
	}

	public void subscribe(String topic, MqttCallback callback) {
		System.out.print("MQTT Config : " + mqttConfigData.toString());
		int qos = mqttConfigData.getQos();
		try {
			System.out.print("Topic: " + topic);
			mqttClient.subscribe(topic, qos);
			mqttClient.setCallback(callback);
			System.out.print("Subscribed to " + topic);
		} catch (MqttException me) {
			System.out.print("reason " + me.getReasonCode());
			System.out.print("msg " + me.getMessage());
			System.out.print("loc " + me.getLocalizedMessage());
			System.out.print("cause " + me.getCause());
			System.out.print("excep " + me);
		}
	}

	public MqttClient getMqttClient() {
		return mqttClient;
	}

	public void setMqttClient(MqttClient mqttClient) {
		this.mqttClient = mqttClient;
	}

}