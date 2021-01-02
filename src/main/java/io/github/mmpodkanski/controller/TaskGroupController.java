package io.github.mmpodkanski.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.mmpodkanski.logic.TaskGroupService;
import io.github.mmpodkanski.model.Task;
import io.github.mmpodkanski.model.TaskRepository;
import io.github.mmpodkanski.model.projection.GroupReadModel;
import io.github.mmpodkanski.model.projection.GroupWriteModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/groups")
class TaskGroupController {
    private final Logger logger = LoggerFactory.getLogger(TaskGroupController.class);
    private final TaskGroupService service;
    private final TaskRepository repository;

    TaskGroupController(TaskGroupService service, TaskRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @PostMapping
    ResponseEntity<GroupReadModel> createGroup(@RequestBody GroupWriteModel toCreate) {
        logger.warn("Creating a new group");
        GroupReadModel result = service.createGroup(toCreate);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @GetMapping
    ResponseEntity<List<GroupReadModel>> readAllGroups() {
        logger.warn("Exposing all the groups");
        return ResponseEntity.ok(service.readAll());
    }

    @GetMapping("/{id}")
    ResponseEntity<List<Task>> readAllTasksFromGroup(@PathVariable int id) {
        return ResponseEntity.ok(repository.findAllByGroup_Id(id));
    }

    @PatchMapping("/{id}")
    ResponseEntity<?> toggleGroup(@PathVariable int id) {
        service.toggleGroup(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(IllegalStateException.class)
    ResponseEntity<String> handleIllegalState(IllegalStateException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
