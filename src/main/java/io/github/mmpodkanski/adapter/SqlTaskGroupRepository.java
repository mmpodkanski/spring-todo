package io.github.mmpodkanski.adapter;

import io.github.mmpodkanski.model.Project;
import io.github.mmpodkanski.model.Task;
import io.github.mmpodkanski.model.TaskGroup;
import io.github.mmpodkanski.model.TaskGroupRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
interface SqlTaskGroupRepository extends TaskGroupRepository, JpaRepository<TaskGroup, Integer> {
    @Override
    @Query("select distinct g from TaskGroup g join fetch g.tasks")
    List<TaskGroup> findAll();

    @Override
    boolean existsByDoneIsFalseAndProject_Id(Integer projectId);

}
