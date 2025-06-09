package advancedweb.project.aiengineservice.ui.controller;

import advancedweb.project.aiengineservice.application.dto.request.AiRecommendRequest;
import advancedweb.project.aiengineservice.application.dto.response.AiRecommendResponse;
import advancedweb.project.aiengineservice.application.usecase.RecommendWelfareUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * gemini 추천 복지 서비스 2차 필터링 컨트롤러
 */

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class GeminiRecommendController {
    //DI
    private final RecommendWelfareUseCase recommendWelfareUseCase;

    /**
     * POST /api/ai/recommend
     * {
     *   "rawUserInput": "...",
     *   "welfarePKs": [101,207,312],
     *   "supportTarget": "...",
     *   "welfareTitle": "...",
     *   "selectionCriteria": "...",
     *   "applyMethod": "..."
     * }
     *
     * → { "recommendedPKs": [207,312] }
     */
    /**
     * @param req
     * @return
     */
    @PostMapping("/recommend")
    public ResponseEntity<AiRecommendResponse> recommend(
            @RequestBody AiRecommendRequest req
    ){
        return ResponseEntity.ok(recommendWelfareUseCase.recommend(req));
    }
}
