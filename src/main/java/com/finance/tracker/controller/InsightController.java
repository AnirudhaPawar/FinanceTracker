package com.finance.tracker.controller;

import com.finance.tracker.service.InsightService;
import com.finance.tracker.util.CurrentUserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/insights")
@RequiredArgsConstructor
public class InsightController {

    private final InsightService insightService;
    private final CurrentUserUtil userUtil;

    @GetMapping
    public List<String> getInsights() {
        Long userId = userUtil.getCurrentUser().getId();
        return insightService.generateInsights(userId);
    }
}
