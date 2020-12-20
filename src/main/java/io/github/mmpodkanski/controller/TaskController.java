package io.github.mmpodkanski.controller;

import io.github.mmpodkanski.model.Task;
import io.github.mmpodkanski.model.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;



@RestController
class TaskController {
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    private final TaskRepository repository;

    //    @Autowired(wstrzyknij tutaj)
    TaskController(final TaskRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/tasks")
    ResponseEntity<Task> createTask(@Valid @RequestBody Task toCreate) {
        Task result = repository.save(toCreate);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @GetMapping(value = "/tasks", params = {"!sort", "!page", "!size"})
    ResponseEntity<List<Task>> readAllTasks() {
        logger.warn("Exposing all the tasks!");
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/tasks")
    ResponseEntity<List<Task>> readAllTasks(Pageable page) {
        logger.info("Custom pageable");
        return ResponseEntity.ok(repository.findAll(page).getContent()); //200
    }

    @GetMapping("/tasks/{id}")
    ResponseEntity<Task> readTask(@PathVariable int id) {
        return repository.findById(id)
                .map(ResponseEntity::ok) //task -> ResponseEntity.ok(task)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/tasks/{id}")
    ResponseEntity<Task> updateTask(@PathVariable int id, @Valid @RequestBody Task toUpdate) {
        if(!repository.existsById(id)) {
            return ResponseEntity.notFound().build(); //404
        }
        toUpdate.setId(id);
        repository.save(toUpdate);
        return ResponseEntity.noContent().build(); //204
    }


}
