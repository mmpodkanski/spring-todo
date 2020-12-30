package io.github.mmpodkanski.logic;

import io.github.mmpodkanski.model.TaskGroup;
import io.github.mmpodkanski.model.TaskGroupRepository;
import io.github.mmpodkanski.model.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskGroupServiceTest {

    @Test
    @DisplayName("should throw IllegalStateException when configured to group has undone tasks")
    void toggleGroup_undoneTasksExists_throwsIllegalStateException() {
        // given
        TaskRepository mockTaskRepository = taskRepositoryReturning(true);
        // system under test
        var toTest = new TaskGroupService(null, mockTaskRepository);

        // when
        var exception = catchThrowable(() -> toTest.toggleGroup(0));

        // then
        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("has undone tasks");
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when configuration ok and no groups for a given id")
    void toggleGroup_configurationOk_And_noGroup_throwsIllegalArgumentException() {
        // given
        TaskRepository mockTaskRepository = taskRepositoryReturning(false);
        // and
        var mockTaskGroupRepository = mock(TaskGroupRepository.class);
        when(mockTaskGroupRepository.findById(anyInt())).thenReturn(Optional.empty());
        // system under test
        var toTest = new TaskGroupService(mockTaskGroupRepository, mockTaskRepository);

        // when
        var exception = catchThrowable(() -> toTest.toggleGroup(0));

        //then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id not found");
    }

    @Test
    @DisplayName("should toggle group")
    void toggleGroup_worksAsExpected() {
        // given
        TaskRepository mockTaskRepository = taskRepositoryReturning(false);
        // and
        var taskGroup = new TaskGroup();
        var beforeToggle = taskGroup.isDone();
        //and
        var mockTaskGroupRepository = mock(TaskGroupRepository.class);
        when(mockTaskGroupRepository.findById(anyInt())).thenReturn(Optional.of(taskGroup));
        // system under test
        var toTest = new TaskGroupService(mockTaskGroupRepository, mockTaskRepository);

        //then
        toTest.toggleGroup(0);

        //then
        assertThat(taskGroup.isDone()).isEqualTo(!beforeToggle);
    }

    private TaskRepository taskRepositoryReturning(final boolean result) {
        var mockTaskRepository = mock(TaskRepository.class);
        when(mockTaskRepository.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(result);
        return mockTaskRepository;
    }

}