package pro.sky.telegrambot;

import java.time.LocalDateTime;

public class NotificationTask {

    public void setId(Long id) {
        this.id = id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    private Long id;
    private String message;
    private LocalDateTime date;

    public Long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public NotificationTask(Long id, LocalDateTime date, String message) {
        this.id = id;
        this.message = message;
        this.date = date;
    }
}
