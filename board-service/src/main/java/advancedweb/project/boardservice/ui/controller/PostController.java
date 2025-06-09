package advancedweb.project.boardservice.ui.controller;

import advancedweb.project.boardservice.application.dto.request.WritePostReq;
import advancedweb.project.boardservice.application.dto.response.PostDetailRes;
import advancedweb.project.boardservice.application.dto.response.PostSummaryRes;
import advancedweb.project.boardservice.application.usecase.PostManagementUseCase;
import advancedweb.project.boardservice.global.annotation.CheckAuthorization;
import advancedweb.project.boardservice.global.annotation.CurrentUser;
import advancedweb.project.boardservice.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class PostController {

    private final PostManagementUseCase postManagementUseCase;

    /**
     *  게시물 목록 조회 API
     *  게시물 썸네일 목록 조회, 페이지 사이즈는 10으로 고정
     */
    @GetMapping
    @CheckAuthorization
    public BaseResponse<Page<PostSummaryRes>> readAllPosts(Integer page) {
        return BaseResponse.onSuccess(postManagementUseCase.readAll(page));
    }

    /**
     *  메인 페이지에서 보여지는 최근 게시물 5개 조회 API
     *  최신 순 정렬
     */
    @GetMapping("/recent")
    @CheckAuthorization
    public BaseResponse<List<PostSummaryRes>> readRecentPost() {
        return BaseResponse.onSuccess(postManagementUseCase.readRecentPost());
    }

    /**
     *  게시물 상세 조회 API
     *  게시물 및 댓글 조회, 만약 자신이 작성한 게시물을 조회했다면 Response에 isMine 값 응답
     */
    @GetMapping("/{postNo}")
    @CheckAuthorization
    public BaseResponse<PostDetailRes> readPost(@PathVariable String postNo,
                                                @CurrentUser @Parameter(hidden = true) String userNo) {
        return BaseResponse.onSuccess(postManagementUseCase.read(postNo, userNo));
    }

    /**
     *  게시물 작성 API
     *  댓글과 함께 조회
     */
    @PostMapping
    @CheckAuthorization
    public BaseResponse<Void> writePost(@CurrentUser @Parameter(hidden = true) String userNo,
                                        @RequestBody @Valid WritePostReq request) {
        postManagementUseCase.write(userNo, request);
        return BaseResponse.onSuccess();
    }

    /**
     *  게시물 삭제 API
     *  내가 작성한 게시물만 삭제 가능
     */
    @DeleteMapping("/{postNo}")
    @CheckAuthorization
    public BaseResponse<Void> deletePost(@PathVariable String postNo,
                                         @CurrentUser @Parameter(hidden = true) String userNo) {
        postManagementUseCase.delete(postNo, userNo);
        return BaseResponse.onSuccess();
    }
}
