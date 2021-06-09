package bootsample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:mqtt.properties")
@PropertySource(value = "file:conf/mqtt.properties", ignoreResourceNotFound = true)
public class MqttConfig {

	@Autowired
	private Environment env;
	
	@Bean
	public MqttConfigData mqttConfigData() {
		MqttConfigData configData = new MqttConfigData();
		configData.setBrokerHost(env.getProperty("broker_host"));
		configData.setBrokerPort(env.getProperty("broker_port"));
		if (env.getProperty("broker_username") != null){
			if (!env.getProperty("broker_username").isEmpty()){
				configData.setBrokerUsername(env.getProperty("broker_username"));
			}
		}
		if (env.getProperty("broker_password") != null){
			if (!env.getProperty("broker_password").isEmpty()){				
				configData.setBrokerPassword(env.getProperty("broker_password"));
			}
		}
		configData.setQos(Integer.parseInt(env.getProperty("qos")));
		configData.setClientName(env.getProperty("client_name"));
		return configData;
	}
	
}