package com.nonitos.food.service;

import com.nonitos.food.dto.notification.NotificationResponse;
import com.nonitos.food.exception.ResourceNotFoundException;
import com.nonitos.food.model.Notification;
import com.nonitos.food.model.NotificationTemplate;
import com.nonitos.food.model.User;
import com.nonitos.food.repository.NotificationRepository;
import com.nonitos.food.repository.NotificationTemplateRepository;
import com.nonitos.food.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private NotificationTemplateRepository notificationTemplateRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private NotificationService notificationService;

    private User testUser;
    private NotificationTemplate testTemplate;
    private Notification testNotification;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setFullName("Test User");
        setId(testUser, 1L);

        testTemplate = new NotificationTemplate();
        testTemplate.setCode("ORDER_CREATED");
        testTemplate.setType(Notification.NotificationType.ORDER_CREATED);
        testTemplate.setTitleTemplate("Order Created");
        testTemplate.setMessageTemplate("Your order {{orderCode}} has been created");
        testTemplate.setIsActive(true);
        setId(testTemplate, 1L);

        testNotification = new Notification();
        testNotification.setUser(testUser);
        testNotification.setType(Notification.NotificationType.ORDER_CREATED);
        testNotification.setTitle("Order Created");
        testNotification.setMessage("Your order TEST123 has been created");
        testNotification.setIsRead(false);
        setId(testNotification, 1L);
    }

    @Test
    void sendNotification_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(notificationTemplateRepository.findByType(Notification.NotificationType.ORDER_CREATED))
                .thenReturn(Optional.of(testTemplate));
        when(notificationRepository.save(any(Notification.class))).thenReturn(testNotification);

        notificationService.sendNotification(
                1L,
                Notification.NotificationType.ORDER_CREATED,
                Map.of("orderCode", "TEST123"),
                1L,
                "Order"
        );

        verify(notificationRepository).save(any(Notification.class));
    }

    @Test
    void sendNotification_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                notificationService.sendNotification(
                        1L,
                        Notification.NotificationType.ORDER_CREATED,
                        Map.of(),
                        1L,
                        "Order"
                )
        );
    }

    @Test
    void getUserNotifications_Success() {
        when(notificationRepository.findByUserIdOrderByCreatedAtDesc(1L))
                .thenReturn(List.of(testNotification));

        List<NotificationResponse> notifications = notificationService.getUserNotifications(1L);

        assertNotNull(notifications);
        assertEquals(1, notifications.size());
    }

    @Test
    void getUnreadNotifications_Success() {
        when(notificationRepository.findByUserIdAndIsReadOrderByCreatedAtDesc(1L, false))
                .thenReturn(List.of(testNotification));

        List<NotificationResponse> notifications = notificationService.getUnreadNotifications(1L);

        assertNotNull(notifications);
        assertEquals(1, notifications.size());
    }

    @Test
    void getUnreadCount_Success() {
        when(notificationRepository.countByUserIdAndIsRead(1L, false)).thenReturn(5L);

        long count = notificationService.getUnreadCount(1L);

        assertEquals(5L, count);
    }

    @Test
    void markAsRead_Success() {
        when(notificationRepository.findById(1L)).thenReturn(Optional.of(testNotification));

        notificationService.markAsRead(1L, 1L);

        verify(notificationRepository).save(any(Notification.class));
    }

    @Test
    void markAllAsRead_Success() {
        when(notificationRepository.findByUserIdAndIsReadOrderByCreatedAtDesc(1L, false))
                .thenReturn(List.of(testNotification));

        notificationService.markAllAsRead(1L);

        verify(notificationRepository).saveAll(anyList());
    }

    private void setId(Object entity, Long id) {
        try {
            var idField = entity.getClass().getSuperclass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(entity, id);
        } catch (Exception e) {
            // Ignore
        }
    }
}
