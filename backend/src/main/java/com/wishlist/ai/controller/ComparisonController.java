package com.wishlist.ai.controller;

import com.wishlist.ai.dto.compare.ComparisonResponse;
import com.wishlist.ai.service.ComparisonService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/compare")
@RequiredArgsConstructor
public class ComparisonController {

    private final ComparisonService comparisonService;

    @GetMapping("/{productId}")
    public ComparisonResponse compare(@PathVariable String productId) {
        return comparisonService.compare(productId);
    }

    @GetMapping
    public List<ComparisonResponse> compareMany(@RequestParam List<String> productIds) {
        return comparisonService.compareMany(productIds);
    }
}
