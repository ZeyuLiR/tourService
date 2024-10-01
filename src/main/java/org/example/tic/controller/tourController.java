package org.example.tic.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class tourController {
    @Autowired
    private JdbcTemplate jdbcTemplate;


    @GetMapping("/api/tours/search")
    public ResponseEntity<?> searchTours(@RequestParam String keyword, @RequestHeader(value = "userId", required = false) String userId) {

        System.out.println(keyword+"         "+userId);
        String sql = "SELECT t.id AS tourId, t.tourName, t.description, t.tourDate, ubt.id AS userBookTourId " +
                "FROM tour t " +
                "LEFT JOIN userBookTour ubt ON t.id = ubt.tourId AND ubt.userId = ? " +
                "WHERE t.tourName LIKE ?";

        List<Map<String, Object>> tours = jdbcTemplate.queryForList(sql, userId, "%" + keyword + "%");
        String jsonStr = JSON.toJSONString(tours);
        System.out.println(jsonStr);
        return ResponseEntity.ok(jsonStr);
    }

    @PostMapping("/api/tours/book")
    public ResponseEntity<?> bookTours(@RequestBody Map<String, Object> jsonObj,@RequestHeader(value = "userId", required = false) String userId) {

        int tourId = (Integer) jsonObj.get("tourId");
        String insertSql = "INSERT INTO userBookTour (userId, tourId) VALUES (?, ?)";
        jdbcTemplate.update(insertSql, userId, tourId);
        JSONObject obj = new JSONObject();
        obj.put("message", "success");
        return ResponseEntity.ok(obj);
    }

    @GetMapping("/api/tours/booked")
    public ResponseEntity<?> getBookedTours(@RequestHeader(value = "userId", required = false) String userId) {
        String sql = "SELECT t.id AS tourId, t.tourName, t.description, t.tourDate, ubt.id AS userBookTourId " +
                "FROM tour t " +
                "INNER JOIN userBookTour ubt ON t.id = ubt.tourId " +
                "WHERE ubt.userId = ?";

        List<Map<String, Object>> tours = jdbcTemplate.queryForList(sql, userId);
        String jsonStr = JSON.toJSONString(tours);
        return ResponseEntity.ok(jsonStr);
    }

}
