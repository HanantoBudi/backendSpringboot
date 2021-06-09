package bootsample.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import bootsample.model.Task;
import bootsample.service.TaskService;

@RestController
public class SampleRestController {
	
	@Autowired
	private TaskService taskService;

	@GetMapping("/hello")
	public String getHello() {
		return "Hello Cold World";
	}
	
	@GetMapping("/all")
	public List<Task> allTasks() {
		return taskService.findAll();
	}
	
	@GetMapping("/save")
	public String saveTask(@RequestParam String name, @RequestParam String desc) {
		Task task = new Task(name, desc, new Date(), false);
		taskService.save(task);
		return "Task Saved!";
	}
	
	@GetMapping("/delete")
	public String deleteTask(@RequestParam int id) {
		taskService.delete(id);
		return "Task Deleted!";
	}
	
}