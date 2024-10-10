package com.example.jobbug.domain.reservation.service;

import com.example.jobbug.domain.chat.entity.Message;
import com.example.jobbug.domain.firebase.entity.FirebaseMessage;
import com.example.jobbug.domain.firebase.entity.FirebaseMessageData;
import com.example.jobbug.domain.firebase.service.FirebaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Slf4j
@Service
public class SchedulerService {

    private final FirebaseService firebaseService;

    private final ThreadPoolTaskScheduler taskScheduler;
    private final Map<String, ScheduledFuture<?>> scheduledTasks;

    public SchedulerService(FirebaseService firebaseService) {
        this.taskScheduler = new ThreadPoolTaskScheduler();
        this.taskScheduler.initialize();
        this.scheduledTasks = new ConcurrentHashMap<>();
        this.firebaseService = firebaseService;
    }

    public String scheduleMessage(LocalDateTime dateTime, Message message) {
        String taskId = UUID.randomUUID().toString();
        Date date = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());

        scheduledTasks.put(taskId, taskScheduler.schedule(() -> {
                    log.info("Task Run: {}", taskId);
                    firebaseService.sendFirebaseMessage(message, FirebaseMessageData.builder().build());
                    scheduledTasks.remove(taskId);
                }
                , date));

        return taskId;
    }

    // 취소
    public boolean cancelTask(String taskId) {
        ScheduledFuture<?> scheduledFuture = scheduledTasks.get(taskId);

        if (scheduledFuture != null && !scheduledFuture.isCancelled()) {
            scheduledFuture.cancel(true);
            scheduledTasks.remove(taskId);
            return true;
        }
        return false;
    }
}
