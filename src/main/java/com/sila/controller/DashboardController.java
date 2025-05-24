package com.sila.controller;

import com.sila.repository.CategoryRepository;
import com.sila.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@Tag(name = "Dashboard")
@RequiredArgsConstructor
public class DashboardController {
    final UserService userService;
    final CategoryRepository categoryRepository;

    @GetMapping
    public ResponseEntity<Object> getAdminDashboard() {
        Map<String, Object> res = new HashMap<>();
        res.put("total_users",  userService.all());
        res.put("total_orders", 0);
        res.put("total_categories",categoryRepository.count());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    private Map<String, Object> createCard(String id, String title, long value) {
        Map<String, Object> card = new HashMap<>();
        card.put("id", id);
        card.put("title", title);
        card.put("value", value);
        return card;
    }
}
