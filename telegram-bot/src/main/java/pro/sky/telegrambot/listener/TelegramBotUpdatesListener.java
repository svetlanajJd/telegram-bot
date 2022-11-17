package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import liquibase.pro.packaged.id;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;
import pro.sky.telegrambot.service.NotificationTaskService;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    public TelegramBotUpdatesListener(NotificationTaskRepository notificationTaskRepository, NotificationTaskService notificationTaskService, TelegramBot telegramBot) {
        this.notificationTaskRepository = notificationTaskRepository;
        this.notificationTaskService = notificationTaskService;
        this.telegramBot = telegramBot;
    }

    private final NotificationTaskRepository notificationTaskRepository;
    private final Pattern patternMessage = Pattern.compile("([0-9\\.\\:\\s]{16})(\\s)([\\W+]+)");

    private final NotificationTaskService notificationTaskService;

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final TelegramBot telegramBot;
    private Integer id;



    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            Message msg = update.message();
            Long chatId = msg.chat().id();

            Matcher matcherMessage = patternMessage.matcher(msg.text());
            if (msg.text().equals("/start")) {
                String messageText = "Hello";
                SendMessage message = new SendMessage(chatId, messageText);
                telegramBot.execute(message);
            } else if (matcherMessage.matches()) {
                String date = matcherMessage.group(1);
                LocalDateTime dateTime = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
                String item = matcherMessage.group(3);
                NotificationTask notificationTask = new NotificationTask(chatId, dateTime, item);
                notificationTaskRepository.save(notificationTask);

            }
        });
        run();
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    @Scheduled(cron = "0 0/1 * * * *")
    public void run() {
        List<NotificationTask> messageFind = notificationTaskService.resultFound(LocalDateTime.now());
        for (NotificationTask notificationTask : messageFind) {
            SendMessage message = new SendMessage(notificationTask.getChatId(), notificationTask.getMessage());
            telegramBot.execute(message);
        }
    }
}