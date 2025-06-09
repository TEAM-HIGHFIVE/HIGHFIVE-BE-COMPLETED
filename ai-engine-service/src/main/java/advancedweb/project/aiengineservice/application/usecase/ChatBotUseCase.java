package advancedweb.project.aiengineservice.application.usecase;

import advancedweb.project.aiengineservice.application.dto.request.ChatBotRequest;
import advancedweb.project.aiengineservice.application.dto.response.ChatBotResponse;
import advancedweb.project.aiengineservice.domain.service.GeminiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * ChatBotRequest를 넘겨 Gemini 답변을 생성
 */
@Service
@RequiredArgsConstructor
public class ChatBotUseCase {
    private final GeminiService geminiService;

    public ChatBotResponse chat(ChatBotRequest req){
        return new ChatBotResponse(geminiService.chatReply(req));
    }
}
