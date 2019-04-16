package service;

import entity.Todo;
import io.vertx.core.Future;

import java.util.Optional;

public interface TodoService {
    Future<Boolean> initDataSource();

    Future<Optional<Todo>> getTodo(String id);

    Future<Boolean> insertTodo(Todo todo);

    Future<Todo> updateTodo(Todo todo);

    Future<Boolean> deleteTodo(String id);
}
