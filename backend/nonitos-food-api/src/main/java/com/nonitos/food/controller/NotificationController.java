package com.nonitos.food.controller;

import com.nonitos.food.dto.ApiResponse;
import com.nonitos.food.dto.notification.NotificationResponse;
import com.nonitos.food.model.User;
import com.nonitos.food.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for notification management.
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * Gets all notifications for the authenticated user.
     *
     * @param user the authenticated user
     * @return list of notifications
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getNotifications(
            @AuthenticationPrincipal User user
    ) {
        List<NotificationResponse> notifications = notificationService.getUserNotifications(user.getId());
        return ResponseEntity.ok(ApiResponse.success(notifications));
    }

    /**
     * Gets unread notifications for the authenticated user.
     *
     * @param user the authenticated user
     * @return list of unread notifications
     */
    @GetMapping("/unread")
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getUnreadNotifications(
            @AuthenticationPrincipal User user
    ) {
        List<NotificationResponse> notifications = notificationService.getUnreadNotifications(user.getId());
        return ResponseEntity.ok(ApiResponse.success(notifications));
    }

    /**
     * Gets unread notification count.
     *
     * @param user the authenticated user
     * @return unread count
     */
    @GetMapping("/unread/count")
    public ResponseEntity<ApiResponse<Long>> getUnreadCount(
            @AuthenticationPrincipal User user
    ) {
        long count = notificationService.getUnreadCount(user.getId());
        return ResponseEntity.ok(ApiResponse.success(count));
    }

    /**
     * Marks a notification as read.
     *
     * @param user the authenticated user
     * @param id the notification ID
     * @return success response
     */
    @PutMapping("/{id}/read")
    public ResponseEntity<ApiResponse<Void>> markAsRead(
            @AuthenticationPrincipal User user,
            @PathVariable Long id
    ) {
        notificationService.markAsRead(id, user.getId());
        return ResponseEntity.ok(ApiResponse.success("Notification marked as read", null));
    }

    /**
     * Marks all notifications as read.
     *
     * @param user the authenticated user
     * @return success response
     */
    @PutMapping("/read-all")
    public ResponseEntity<ApiResponse<Void>> markAllAsRead(
            @AuthenticationPrincipal User user
    ) {
        notificationService.markAllAsRead(user.getId());
        return ResponseEntity.ok(ApiResponse.success("All notifications marked as read", null));
    }
}
