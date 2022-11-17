package pro.sky.telegrambot;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
@Entity
public class NotificationTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String message;
    private LocalDateTime date;
    private Long chatId;

    public NotificationTask() {
            }

    public Long getChatId() {return chatId;}
    public Integer getId() {return id;}
    public String getMessage() {return message;}
    public LocalDateTime getDate() {return date;}
    public void setMessage(String message) {this.message = message;}
    public void setDate(LocalDateTime date) {this.date = date;}
    public void setChatId(Long chatId) {this.chatId = chatId;}
    public void setId(Integer id) {this.id = id;}
    public NotificationTask(Long chatId, LocalDateTime date, String message) {
//        this.id = id;
        this.chatId = chatId;
        this.message = message;
        this.date = date;
    }
}
