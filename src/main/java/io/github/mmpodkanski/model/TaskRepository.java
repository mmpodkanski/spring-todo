package io.github.mmpodkanski.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    List<Task> findAll();

    Page<Task> findAll(Pageable page);

    Optional<Task> findById(Integer i);

    boolean existsById(Integer id);

    boolean existsByDoneIsFalseAndGroup_Id(Integer groupId);

    List<Task> findByDone(@Param("state") boolean done);

    List<Task> findAllByGroup_Id(int idGroup);

    List<Task> findTasksByDoneFalseAndDeadlineIsLessThanOrDeadlineIsNullAndDoneFalse(LocalDateTime date);

    Task save(Task entity);

}
