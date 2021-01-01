package io.github.mmpodkanski.controller;

import io.github.mmpodkanski.model.Task;
import io.github.mmpodkanski.model.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskControllerTestE2ETest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TaskRepository repo;

    @Test
    @DisplayName("should return all tasks")
    void httpGet_returnsAllTasks() {
        // given
        int initial = repo.findAll().size();
        repo.save(new Task("foo", LocalDateTime.now()));
        repo.save(new Task("bar", LocalDateTime.now()));

        // when
        Task[] result = restTemplate.getForObject("http://localhost:" + port + "/tasks", Task[].class);

        // then
        assertThat(result).hasSize(initial + 2);
    }

    @Test
    @DisplayName("should return task from id")
    void httpGet_returnsGivenTask() {
        // given
        Task task = repo.save(new Task("foo", LocalDateTime.now()));
        int idTask = task.getId();

        // when
        Task result = restTemplate.getForObject("http://localhost:" + port + "/tasks/" + idTask, Task.class);

        // then
        assertThat(result)
                .hasFieldOrPropertyWithValue("id", idTask)
                .isInstanceOf(Task.class);
    }

    @Test
    @DisplayName("should create task")
    void httpPost_createTask() {
        // given
        int initial = repo.findAll().size();
        Task toCreate = new Task("example", LocalDateTime.now());
        restTemplate.postForObject("http://localhost:" + port + "/tasks", toCreate, Task.class);
        initial++;

        // when
        Task result = restTemplate.getForObject("http://localhost:" + port + "/tasks/" + initial, Task.class);

        // then
        assertThat(result.getId()).isEqualTo(initial);
    }

    @Test
    @DisplayName("should update created task")
    void httpPut_updateTask() {
        // given
        Task beforeUpdate = new Task("beforeUpdate", LocalDateTime.now());
        Task toUpdate = new Task("afterUpdate", LocalDateTime.now());
        int idTask = repo.save(beforeUpdate).getId();
        restTemplate.put("http://localhost:" + port + "/tasks/" + idTask, toUpdate);

        // when
        Task result = restTemplate.getForObject("http://localhost:" + port + "/tasks/" + idTask, Task.class);

        // then
        assertThat(result.getDescription()).isEqualTo(toUpdate.getDescription());
    }
}