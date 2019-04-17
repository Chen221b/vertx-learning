package service;

import entity.Todo;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLConnection;

import java.util.List;
import java.util.Optional;

public class JDBCTodoService implements TodoService {

    private static final String SQL_CREATE = "CREATE TABLE IF NOT EXISTS `todo` (\n" +
            "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
            "  `title` varchar(255) DEFAULT NULL,\n" +
            "  `completed` tinyint(1) DEFAULT NULL,\n" +
            "  `order` int(11) DEFAULT NULL,\n" +
            "  `url` varchar(255) DEFAULT NULL,\n" +
            "  PRIMARY KEY (`id`) )";
    private static final String SQL_INSERT = "INSERT INTO `todo` " +
            "(`id`, `title`, `completed`, `order`, `url`) VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_QUERY = "SELECT * FROM todo WHERE id = ?";
    private static final String SQL_QUERY_ALL = "SELECT * FROM todo";
    private static final String SQL_UPDATE = "UPDATE `todo`\n" +
            "SET `id` = ?,\n" +
            "`title` = ?,\n" +
            "`completed` = ?,\n" +
            "`order` = ?,\n" +
            "`url` = ?\n" +
            "WHERE `id` = ?;";
    private static final String SQL_DELETE = "DELETE FROM `todo` WHERE `id` = ?";
    private static final String SQL_DELETE_ALL = "DELETE FROM `todo`";

    private static final String URL = "jdbc:mysql://172.17.0.2:3306/damon?useSSL=false";
    private static final String DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";
    private static final String USER = "root";
    private static final String PASSWORD = "password";

    private final JDBCClient client;

    public JDBCTodoService(Vertx vertx) {
        JsonObject config = new JsonObject();
        config.put("url", URL);
        config.put("driver_class", DRIVER_CLASS);
        config.put("user", USER);
        config.put("password", PASSWORD);
        this.client = JDBCClient.createShared(vertx, config);
    }

    @Override
    public Future<Boolean> initDataSource() {
        Future<Boolean> result = Future.future();
        client.getConnection(res -> {
            if(res.succeeded()) {
                SQLConnection conn = res.result();
                conn.execute(SQL_CREATE, create -> {
                    if (create.succeeded()) {
                        result.complete(true);
                    } else {
                        result.fail(create.cause());
                    }
                    conn.close();
                });
            }
        });
        return result;
    }

    @Override
    public Future<Optional<Todo>> getTodo(String id) {
        Future<Optional<Todo>> result = Future.future();
        client.getConnection(res -> {
            if(res.succeeded()) {
                SQLConnection connection = res.result();
                JsonArray params = new JsonArray();
                params.add(id);
                connection.queryWithParams(SQL_QUERY, params, get -> {

                    //数据库查询失败
                    if(get.failed()) {
                        result.fail(get.cause());
                    } else {
                        ResultSet rs = get.result();
                        List<JsonObject> list = rs.getRows();

                        //查询结果为空，返回Optional.empty()
                        if(list == null || list.size() == 0) {
                            result.complete(Optional.empty());
                        } else {
                            result.complete(Optional.of(new Todo(list.get(0))));
                        }
                    }
                });
            }
        });
        return result;
    }

    @Override
    public Future<Boolean> insertTodo(Todo todo) {
        return null;
    }

    @Override
    public Future<Todo> updateTodo(Todo todo) {
        return null;
    }

    @Override
    public Future<Boolean> deleteTodo(String id) {
        return null;
    }
}
