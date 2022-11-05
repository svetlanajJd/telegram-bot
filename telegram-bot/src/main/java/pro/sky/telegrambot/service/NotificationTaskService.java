package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


@Service
public class NotificationTaskService {
@Autowired
    private TelegramBot telegramBot;
    private Logger logger = LoggerFactory.getLogger(NotificationTaskService.class);
    private final NotificationTaskRepository notificationTaskRepository;
    private NotificationTask notificationTask;

    public NotificationTaskService(NotificationTaskRepository notificationTaskRepository) {
        this.notificationTaskRepository = notificationTaskRepository;

    }

    public NotificationTask processFound() {
        if (notificationTaskRepository.findAll().equals(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))) {
            return this.notificationTask;
        }
        return null;
    }
}
