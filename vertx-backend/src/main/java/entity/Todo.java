package entity;

import io.vertx.core.json.JsonObject;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 提供5种构造函数：
 *  1.空构造
 *  2.全参数构造
 *  3.拷贝构造
 *  4.Json构造
 *  5.String构造
 *
 * 重写3个Object方法：
 *  1.toString
 *  2.equals
 *  3.hashCode
 */
public class Todo {

    private static final AtomicInteger acc = new AtomicInteger(0);

    private int id;
    private String title;
    private Boolean completed;
    private Integer order;
    private String url;

    public Todo() {
    }

    public Todo(int id, String title, boolean completed, Integer order, String url) {
        this.id = id;
        this.title = title;
        this.completed = completed;
        this.order = order;
        this.url = url;
    }

    public Todo (Todo other) {
        this.id = other.id;
        this.title = other.title;
        this.completed = other.completed;
        this.order = other.order;
        this.url = other.url;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public Todo(JsonObject json) {
        TodoConverter.fromJson(json, this);
    }

    public Todo(String str) {
        TodoConverter.fromJson(new JsonObject(str), this);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
