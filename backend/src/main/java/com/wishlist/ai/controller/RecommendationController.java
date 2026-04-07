package com.wishlist.ai.controller;

import com.wishlist.ai.dto.recommendation.RecommendationResponse;
import com.wishlist.ai.security.AppUserDetails;
import com.wishlist.ai.service.RecommendationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @GetMapping
    public List<RecommendationResponse> recommend(@AuthenticationPrincipal AppUserDetails user) {
        return recommendationService.recommendForUser(user.getId());
    }

    @GetMapping("/trending")
    public List<RecommendationResponse> trending() {
        return recommendationService.trending();
    }
}
