package advancedweb.project.aiengineservice.ui.controller;

import advancedweb.project.aiengineservice.application.dto.request.ChatBotRequest;
import advancedweb.project.aiengineservice.application.dto.response.ChatBotResponse;
import advancedweb.project.aiengineservice.application.usecase.ChatBotUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * welfare-service로부터 ChatBotRequest를 받아서 ChatUsecase에 전달, 결과(ChatBotResponse) 반환
 */
@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class ChatBotController {
    private final ChatBotUseCase chatBotUseCase;

    @PostMapping("/chat")
    public ResponseEntity<ChatBotResponse> chat(@RequestBody ChatBotRequest req){
        return ResponseEntity.ok(chatBotUseCase.chat(req));
    }
}
