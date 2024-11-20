package org.example.expert.domain.todo.service;

import org.example.expert.client.WeatherClient;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.todo.dto.request.TodoSaveRequest;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.dto.response.TodoSaveResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @Mock
    private WeatherClient weatherClient;

    @InjectMocks
    private TodoService todoService;

    private User mockUser;
    private Todo mockTodo;

    @BeforeEach
    void setUp() {
        mockUser = new User("test@example.com", "password", UserRole.USER);
        mockTodo = new Todo("Title", "Contents", "Sunny", mockUser);
    }


    @Test
    void saveTodo_성공() {
        // given
        AuthUser authUser = new AuthUser(1L, "test@example.com", UserRole.USER);
        TodoSaveRequest request = new TodoSaveRequest("Title", "Contents");
        when(weatherClient.getTodayWeather()).thenReturn("Sunny");
        when(todoRepository.save(any(Todo.class))).thenReturn(mockTodo);

        // when
        TodoSaveResponse response = todoService.saveTodo(authUser, request);

        // then
        assertThat(response.getId()).isEqualTo(mockTodo.getId());
        assertThat(response.getTitle()).isEqualTo(mockTodo.getTitle());
        assertThat(response.getWeather()).isEqualTo("Sunny");
        assertThat(mockTodo.getManagers()).hasSize(1);
        verify(todoRepository, times(1)).save(any(Todo.class));
    }

    @Test
    void getTodos_성공() {
        // given
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Todo> todoPage = new PageImpl<>(Collections.singletonList(mockTodo), pageRequest, 1);
        when(todoRepository.findAllByOrderByModifiedAtDesc(pageRequest)).thenReturn(todoPage);

        // when
        Page<TodoResponse> responsePage = todoService.getTodos(1, 10);

        // then
        assertThat(responsePage.getTotalElements()).isEqualTo(1);
        assertThat(responsePage.getContent().get(0).getTitle()).isEqualTo(mockTodo.getTitle());
        verify(todoRepository, times(1)).findAllByOrderByModifiedAtDesc(pageRequest);
    }

    @Test
    void searchTodos_성공() {
        // given
        String weather = "Sunny";
        LocalDateTime startDate = LocalDateTime.now().minusDays(7);
        LocalDateTime endDate = LocalDateTime.now();
        when(todoRepository.findByConditions(weather, startDate, endDate))
                .thenReturn(Collections.singletonList(mockTodo));

        // when
        List<TodoResponse> responses = todoService.searchTodos(weather, startDate, endDate);

        // then
        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getWeather()).isEqualTo(weather);
        verify(todoRepository, times(1)).findByConditions(weather, startDate, endDate);
    }

    @Test
    void getTodo_존재하지않는_Todo_예외발생() {
        // given
        long todoId = 1L;
        when(todoRepository.findByIdWithUser(todoId)).thenReturn(Optional.empty());

        // when & then
        try {
            todoService.getTodo(todoId);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(RuntimeException.class);
            assertThat(e.getMessage()).isEqualTo("Todo not found");
        }
    }
}