package advancedweb.project.welfareservice.application.usecase;

import advancedweb.project.welfareservice.application.dto.request.AskReq;
import advancedweb.project.welfareservice.application.dto.request.ChatBotReq;
import advancedweb.project.welfareservice.application.dto.response.ChatBotRes;
import advancedweb.project.welfareservice.domain.entity.Welfare;
import advancedweb.project.welfareservice.domain.service.WelfareService;
import advancedweb.project.welfareservice.infra.client.AiFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AskChatbotUseCase {

    // DI
    private final WelfareService welfareService;
    private final AiFeignClient aiFeignClient;

    // Method
    public ChatBotRes ask(String welfareNo, String question) {
        Welfare welfare = welfareService.read(welfareNo);
        return aiFeignClient.chat(ChatBotReq.create(welfare, question));
    }
}
