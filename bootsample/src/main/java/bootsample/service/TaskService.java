package bootsample.service;

import java.util.List;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import bootsample.dao.TaskRepository;
import bootsample.model.Task;

@Service
public class TaskService {

	@Autowired
	private TaskRepository taskRepo;
	
	@Autowired
	private MqttService mqttService;
	
	@Autowired
	private MqttCallback mqttCallback;
	
	public List<Task> findAll() {
		List<Task> tasks = (List<Task>) taskRepo.findAll(); 
		return tasks;
	}
	
	public Task findOne(int i) {
		return taskRepo.findOne(i);
	}
	
	public void save(Task task) {
		taskRepo.save(task);
	}
	
	public void delete(int id) {
		taskRepo.delete(id);
	}

	public Task findTask(int id){
		return taskRepo.findOne(id);
	}
	
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		
		if (mqttService.getMqttClient() == null) {
			mqttService.connect();
		}
		if (!mqttService.getMqttClient().isConnected()) {
			mqttService.connect();
		}
		subscribe();
		
	}
	
	public String testMqtt( ) {
		if (mqttService.getMqttClient() == null) {
			mqttService.connect();
		}
		if (!mqttService.getMqttClient().isConnected()) {
			mqttService.connect();
		}
		mqttService.sendMessage("/mqtt/springboot/",
				"Hananto Budi Prasetio");
		subscribe();
		return "Good Job!";
	}
	
	private void subscribe() {
		System.out.print("Subscribing to test topics...");
		mqttService.subscribe("/mqtt/arrived", mqttCallback);
	}
	
	public String messageArrived(String topic, MqttMessage message) {
		System.out.print(message.toString());
		return message.toString();
	}

//	public String piramidaBintang() {
//		String aba = "";
//		System.out.println("Nomer 1");
//		int a = 5;
//		for (int i = 1; i <= a; i++) {
//			for (int j = 4; j >= i; j--) {
//				System.out.print(' ');
//			}
//			for (int k = 1; k <= i; k++) {
//				System.out.print('*');
//			}
//			for (int l = 1; l <= i; l++) {
//				System.out.print('*');
//			}
//			System.out.println();
//		}
//		System.out.println("Nomer 2");
//		//nomer 2
////		int a = 5;
//		for (int i = 1; i <= a; i++) {
//			for (int j = 4; j >= i; j--) {
//				System.out.print(' ');
//			}
//			for (int k = 1; k <= i; k++) {
//				System.out.print('*');
//			}
//			System.out.println();
//		}
//		System.out.println("Nomer 3");
//		//nomer 3
//		for (int i = 0; i <= a; i++) {
//			for (int j = 0; j < i; j++) {
//				System.out.print('*');
//			}
//			System.out.println();
//		}
//		for (int i = a; i > 0; i--) {
//			for (int j = 0; j < i; j++) {
//				System.out.print('*');
//			}
//			System.out.println();
//		}
//		int c = 5;
//		for (int i = 0; i <= c; i++) {
//			for (int j = 1; j <= a-i; j++) {
//				System.out.print(' ');
//			}
//			for (int k = 1; k <= i; k++) {
//				System.out.print('*');
//			}
//			System.out.println();
//		}
//		for (int i = c; i >= 1; i--) {
//			for (int j = 1; j <= c-i; j++) {
//				System.out.print(' ');
//			}
//			for (int k = 1; k <= i; k++) {
//				System.out.print('*');
//			}
//			System.out.println();
//		}
//			
//		return aba;
//	}
	
}