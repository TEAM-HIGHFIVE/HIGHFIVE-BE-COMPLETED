package advancedweb.project.boardservice.ui.controller;

import advancedweb.project.boardservice.application.dto.request.WriteCmtReq;
import advancedweb.project.boardservice.application.usecase.CommentManagementUseCase;
import advancedweb.project.boardservice.global.annotation.CheckAuthorization;
import advancedweb.project.boardservice.global.annotation.CurrentUser;
import advancedweb.project.boardservice.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class CommentController {

    private final CommentManagementUseCase commentManagementUseCase;

    /**
     *  댓글 작성 API
     */
    @PostMapping("/{postNo}")
    @CheckAuthorization
    public BaseResponse<Void> write(@PathVariable String postNo,
                                    @RequestBody @Valid WriteCmtReq request,
                                    @CurrentUser @Parameter(hidden = true) String userNo) {
        commentManagementUseCase.write(postNo, request, userNo);
        return BaseResponse.onSuccess();
    }

    /**
     *  댓글 삭제 API
     */
    @DeleteMapping("/{commentNo}")
    @CheckAuthorization
    public BaseResponse<Void> delete(@PathVariable String commentNo,
                                     @CurrentUser @Parameter(hidden = true) String userNo) {
        commentManagementUseCase.delete(commentNo, userNo);
        return BaseResponse.onSuccess();
    }
}
