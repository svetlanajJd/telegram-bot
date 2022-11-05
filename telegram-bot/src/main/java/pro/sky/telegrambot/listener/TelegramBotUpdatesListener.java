package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {
    private final NotificationTaskRepository notificationTaskRepository;

    public TelegramBotUpdatesListener(NotificationTaskRepository notificationTaskRepository) {
        this.notificationTaskRepository = notificationTaskRepository;
    }

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Autowired
    private TelegramBot telegramBot;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            String messageText = "Hello";
            Message msg = update.message();
            Long chatId = msg.chat().id();
            SendMessage message = new SendMessage(chatId, messageText);


            if (update.message().text().equals("/start")) {
                telegramBot.execute(message);
            }

            Pattern patternMessage = Pattern.compile("([0-9\\.\\:\\s]{16})(\\s)([\\W+]+)");
            try {
                Matcher matcherMessage = patternMessage.matcher("01.01.2022 20:00 Сделать домашнюю работу");
                if (matcherMessage.matches()) {
                    String date = matcherMessage.group(1);
                    LocalDateTime dateTime = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
                    String item = matcherMessage.group(3);
                    NotificationTask notificationTask = new NotificationTask(chatId, dateTime, item);
                    notificationTaskRepository.save(notificationTask);
                }
            } catch (NullPointerException ignored) {
            }

        });

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }


    @Scheduled(cron = "0 0/1 * * * *")
    public int run(List<Update> updates) {
        updates.forEach(update -> {
            Message msg = update.message();
            Long chatId = msg.chat().id();
            SendMessage message = new SendMessage(chatId, notificationTaskRepository.processFound().getMessage());
            telegramBot.execute(message);
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

}