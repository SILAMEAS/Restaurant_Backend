package com.sila.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/dashboard")
@Tag(name = "Admin Dashboard Controller", description = "Admin operations related to dashboard")
public class AdminDashboardController {

    @GetMapping
    public ResponseEntity<Object> getAdminDashboard() {
        Map<String, Object> response = new HashMap<>();

        List<Map<String, Object>> cards = List.of(
                createCard("total_sales", "Total Sales", 0),
                createCard("total_orders", "Total Orders", 0),
                createCard("total_customers", "Total Customers", 1),
                createCard("total_users", "Total Users", 2),
                createCard("total_payment", "Total Payment", 0),
                createCard("total_products", "Total Products", 1),
                createCard("total_categories", "Total Categories", 5),
                createCard("total_shipping", "Total Shipping", 0)
        );

        List<Map<String, Object>> salesChart = new ArrayList<>();
        List<String> months = List.of("Jan", "Feb", "Mar", "Apr", "May", "Jun",
                "Jul", "Aug", "Sep", "Oct", "Nov", "Dec");
        for (String month : months) {
            Map<String, Object> entry = new HashMap<>();
            entry.put("date", month);
            entry.put("amount", 0);
            salesChart.add(entry);
        }

        response.put("cards", cards);
        response.put("top_selling_products", new ArrayList<>());
        response.put("recent_orders_data", new ArrayList<>());
        response.put("sales_chart", salesChart);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private Map<String, Object> createCard(String id, String title, int value) {
        Map<String, Object> card = new HashMap<>();
        card.put("id", id);
        card.put("title", title);
        card.put("value", value);
        return card;
    }
}
