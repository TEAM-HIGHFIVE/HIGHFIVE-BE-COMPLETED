package advancedweb.project.welfareservice.application.dto.response;

import advancedweb.project.welfareservice.domain.entity.Welfare;
import advancedweb.project.welfareservice.domain.entity.enums.Area;
import advancedweb.project.welfareservice.domain.entity.enums.Target;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public record WelfareDetailRes(
        String welfareNo,
        String title,       // 제목
        LocalDateTime updatedAt,    // 갱신일
        Set<String> areas,    // 지역 태그
        Set<String> targets,    // 지원 대상 태그
        String criteria,    // 선정 기준
        String content,     // 서비스 내용
        String applyMethod,     // 신청 방법
        String tel,     // 전화 문의
        String referenceLink,    // 관련 웹사이트
        String reference    // 근거 법령 및 자료
) {
    public static WelfareDetailRes create(Welfare welfare) {
        return new WelfareDetailRes(
                welfare.getWelfareNo(),
                welfare.getSummary().getName(),
                LocalDateTime.now(),
                welfare.getSummary().getAreas().stream()
                        .map(Area::getDesc)
                        .collect(Collectors.toSet()),
                welfare.getSummary().getTargets().stream()
                        .map(Target::getDesc)
                        .collect(Collectors.toSet()),
                welfare.getDetail().getCriteria(),
                welfare.getDetail().getContent(),
                welfare.getDetail().getApplyMethod(),
                welfare.getDetail().getTel(),
                welfare.getDetail().getReferenceLink(),
                welfare.getDetail().getReference()
        );
    }
}
