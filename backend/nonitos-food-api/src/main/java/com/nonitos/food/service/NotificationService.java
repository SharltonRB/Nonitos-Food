package com.nonitos.food.service;

import com.nonitos.food.dto.notification.NotificationResponse;
import com.nonitos.food.exception.ResourceNotFoundException;
import com.nonitos.food.model.Notification;
import com.nonitos.food.model.NotificationTemplate;
import com.nonitos.food.model.User;
import com.nonitos.food.repository.NotificationRepository;
import com.nonitos.food.repository.NotificationTemplateRepository;
import com.nonitos.food.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service for managing notifications.
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationTemplateRepository notificationTemplateRepository;
    private final UserRepository userRepository;

    /**
     * Sends a notification to a user.
     *
     * @param userId the user ID
     * @param type the notification type
     * @param variables template variables
     * @param relatedEntityId related entity ID
     * @param relatedEntityType related entity type
     */
    @Transactional
    public void sendNotification(
            Long userId,
            Notification.NotificationType type,
            Map<String, String> variables,
            Long relatedEntityId,
            String relatedEntityType
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        NotificationTemplate template = notificationTemplateRepository.findByType(type)
                .orElseThrow(() -> new ResourceNotFoundException("Template not found for type: " + type));

        String title = replaceVariables(template.getTitleTemplate(), variables);
        String message = replaceVariables(template.getMessageTemplate(), variables);

        Notification notification = Notification.builder()
                .user(user)
                .type(type)
                .title(title)
                .message(message)
                .isRead(false)
                .relatedEntityId(relatedEntityId)
                .relatedEntityType(relatedEntityType)
                .build();

        notificationRepository.save(notification);

        // Mock email sending
        mockSendEmail(user.getEmail(), title, message);

        log.info("Notification sent to user {}: {}", userId, type);
    }

    /**
     * Gets all notifications for a user.
     *
     * @param userId the user ID
     * @return list of notifications
     */
    @Transactional(readOnly = true)
    public List<NotificationResponse> getUserNotifications(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::buildNotificationResponse)
                .collect(Collectors.toList());
    }

    /**
     * Gets unread notifications for a user.
     *
     * @param userId the user ID
     * @return list of unread notifications
     */
    @Transactional(readOnly = true)
    public List<NotificationResponse> getUnreadNotifications(Long userId) {
        return notificationRepository.findByUserIdAndIsReadOrderByCreatedAtDesc(userId, false)
                .stream()
                .map(this::buildNotificationResponse)
                .collect(Collectors.toList());
    }

    /**
     * Gets unread notification count for a user.
     *
     * @param userId the user ID
     * @return count of unread notifications
     */
    @Transactional(readOnly = true)
    public long getUnreadCount(Long userId) {
        return notificationRepository.countByUserIdAndIsRead(userId, false);
    }

    /**
     * Marks a notification as read.
     *
     * @param notificationId the notification ID
     * @param userId the user ID
     */
    @Transactional
    public void markAsRead(Long notificationId, Long userId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));

        if (!notification.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Notification not found");
        }

        if (!notification.getIsRead()) {
            notification.setIsRead(true);
            notification.setReadAt(LocalDateTime.now());
            notificationRepository.save(notification);
            log.info("Notification {} marked as read", notificationId);
        }
    }

    /**
     * Marks all notifications as read for a user.
     *
     * @param userId the user ID
     */
    @Transactional
    public void markAllAsRead(Long userId) {
        List<Notification> unreadNotifications = notificationRepository
                .findByUserIdAndIsReadOrderByCreatedAtDesc(userId, false);

        LocalDateTime now = LocalDateTime.now();
        unreadNotifications.forEach(notification -> {
            notification.setIsRead(true);
            notification.setReadAt(now);
        });

        notificationRepository.saveAll(unreadNotifications);
        log.info("Marked {} notifications as read for user {}", unreadNotifications.size(), userId);
    }

    private String replaceVariables(String template, Map<String, String> variables) {
        String result = template;
        if (variables != null) {
            for (Map.Entry<String, String> entry : variables.entrySet()) {
                result = result.replace("{{" + entry.getKey() + "}}", entry.getValue());
            }
        }
        return result;
    }

    private void mockSendEmail(String to, String subject, String body) {
        // Mock email sending - in production, use actual email service
        log.info("=== MOCK EMAIL ===");
        log.info("To: {}", to);
        log.info("Subject: {}", subject);
        log.info("Body: {}", body);
        log.info("==================");
    }

    private NotificationResponse buildNotificationResponse(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .type(notification.getType())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .isRead(notification.getIsRead())
                .relatedEntityId(notification.getRelatedEntityId())
                .relatedEntityType(notification.getRelatedEntityType())
                .readAt(notification.getReadAt())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
