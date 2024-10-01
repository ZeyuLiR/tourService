package org.example.tic.controller;

import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class tourControllerTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void searchTours() {
        int userId = 1;
        String keyword = "mountain";
        String sql = "SELECT t.id AS tourId, t.tourName, t.description, t.tourDate, ubt.id AS userBookTourId " +
                "FROM tour t " +
                "LEFT JOIN userBookTour ubt ON t.id = ubt.tourId AND ubt.userId = ? " +
                "WHERE t.tourName LIKE ?";

        List<Map<String, Object>> tours = jdbcTemplate.queryForList(sql, userId, "%" + keyword + "%");
        String jsonStr = JSON.toJSONString(tours);
        System.out.println(jsonStr);
    }

    @Test
    void getBookedTours() {
        int userId = 1;
        String sql = "SELECT t.id AS tourId, t.tourName, t.description, t.tourDate, ubt.id AS userBookTourId " +
                "FROM tour t " +
                "INNER JOIN userBookTour ubt ON t.id = ubt.tourId " +
                "WHERE ubt.userId = ?";

        List<Map<String, Object>> tours = jdbcTemplate.queryForList(sql, userId);
        String jsonStr = JSON.toJSONString(tours);
        System.out.println(jsonStr);
    }
}