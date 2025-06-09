package advancedweb.project.welfareservice.ui.controller;

import advancedweb.project.welfareservice.application.dto.response.DownloadFileRes;
import advancedweb.project.welfareservice.application.dto.response.WelfareDetailRes;
import advancedweb.project.welfareservice.application.dto.response.WelfareSummaryRes;
import advancedweb.project.welfareservice.application.usecase.FileStorageUseCase;
import advancedweb.project.welfareservice.application.usecase.WelfareManagementUseCase;
import advancedweb.project.welfareservice.domain.entity.enums.Area;
import advancedweb.project.welfareservice.domain.entity.enums.Target;
import advancedweb.project.welfareservice.global.annotation.CheckAuthorization;
import advancedweb.project.welfareservice.global.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/welfare")
public class WelfareController {

    private final WelfareManagementUseCase welfareManagementUseCase;
    private final FileStorageUseCase fileStorageUseCase;

    /**
     *  복지 서비스 검색 API
     *  지역 및 장애 구분으로 필터링한 뒤 ai-engine-service로 검색어와 데이터 전송
     *  응답을 받고 해당 PK 값들만 디자인 템플릿에 맞게 응답
     *  필터링 기준은 Enum으로 두 항목을 선언하여 RequestParam으로 필터링
     */
    @GetMapping
    @CheckAuthorization
    public BaseResponse<List<WelfareSummaryRes>> searchWelfare(@RequestParam(required = false) Area area,
                                                               @RequestParam(required = false) Target target,
                                                               @RequestParam String question) {
        return BaseResponse.onSuccess(welfareManagementUseCase.search(area, target, question));
    }

    /**
     *  복지 상세 조회 API
     *  PK를 기준으로 상세 조회
     *  조회시 해당 복지의 조회 수 증가 (동시성 이슈)
     */
    @GetMapping("/{welfareNo}")
    @CheckAuthorization
    public BaseResponse<WelfareDetailRes> readWelfare(@PathVariable String welfareNo) {
        return BaseResponse.onSuccess(welfareManagementUseCase.read(welfareNo));
    }

    /**
     *  상세 조회 페이지 내에서 원본 파일 다운로드 API
     *  welfare PK 기준으로 파일을 찾아서 URI 전송
     */
    @PostMapping("/download/{welfareNo}")
    @CheckAuthorization
    public BaseResponse<DownloadFileRes> downloadWelfareFile(@PathVariable String welfareNo) {
        return BaseResponse.onSuccess(fileStorageUseCase.download(welfareNo));
    }

    @GetMapping("/popular")
    @CheckAuthorization
    public BaseResponse<List<WelfareSummaryRes>> readPopularWelfare() {
        return BaseResponse.onSuccess(welfareManagementUseCase.readPopularWelfare());
    }
}
