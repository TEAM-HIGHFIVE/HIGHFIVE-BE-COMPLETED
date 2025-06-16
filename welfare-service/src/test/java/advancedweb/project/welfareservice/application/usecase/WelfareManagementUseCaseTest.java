package advancedweb.project.welfareservice.application.usecase;

import advancedweb.project.welfareservice.application.dto.request.RecommendReq;
import advancedweb.project.welfareservice.application.dto.response.RecommendRes;
import advancedweb.project.welfareservice.application.dto.response.WelfareDetailRes;
import advancedweb.project.welfareservice.application.dto.response.WelfareSummaryRes;
import advancedweb.project.welfareservice.domain.entity.Detail;
import advancedweb.project.welfareservice.domain.entity.Summary;
import advancedweb.project.welfareservice.domain.entity.Welfare;
import advancedweb.project.welfareservice.domain.entity.enums.Area;
import advancedweb.project.welfareservice.domain.entity.enums.Target;
import advancedweb.project.welfareservice.domain.service.ViewCountService;
import advancedweb.project.welfareservice.domain.service.WelfareService;
import advancedweb.project.welfareservice.infra.client.AiFeignClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WelfareManagementUseCaseTest {

    private WelfareService welfareService;
    private AiFeignClient aiFeignClient;
    private ViewCountService viewCountService;
    private WelfareManagementUseCase useCase;

    @BeforeEach
    void setUp() {
        welfareService = mock(WelfareService.class);
        aiFeignClient = mock(AiFeignClient.class);
        viewCountService = mock(ViewCountService.class);
        useCase = new WelfareManagementUseCase(welfareService, aiFeignClient, viewCountService);
    }

    @Test
    void 복지_서비스_검색() {
        Welfare w1 = Welfare.builder()
                .welfareNo("684e59a2240b7223da3e25e1")
                .summary(Summary.builder()
                        .name("구리시 무주택 신혼부부 전월세자금 대출이자 지원")
                        .areas(Set.of(Area.GURI))
                        .targets(Set.of()) // 비어 있음
                        .build())
                .detail(Detail.builder()
                        .target("구리시 소재 임차계약을 체결하여 금융권에서 주택 전월세자금 대출을 받은 무주택 신혼부부")
                        .criteria("- 신혼부부 : 혼인신고일로부터 7년 이내 부부(공고일 기준)\n" +
                                "- 부부 모두 구리시 동일주소에 등재가 되어 거주하는 무주택 신혼부부(공고일 기준)\n" +
                                "- 대출잔액 2억원 이하(공고일 기준)\n" +
                                "- 부부합산 기준중위소득 180% 이하인 가구")
                        .content("대출잔액 1%(최대 100만원 이내) 연1회 일시지급")
                        .applyMethod("주소지 동 행정복지센터 방문 및 상담 신청")
                        .tel("구리시 기획예산담당관 지속가능발전팀 031-550-2729")
                        .reference("구리시 무주택 신혼부부 전월세자금 대출이자 지원 조례")
                        .referenceLink("")
                        .build())
                .readCnt(0)
                .build();

        Welfare w2 = Welfare.builder()
                .welfareNo("684e59a2240b7223da3e25e2")
                .summary(Summary.builder()
                        .name("다자녀가구 행복더하기(지역화폐)")
                        .areas(Set.of(Area.GURI))
                        .targets(Set.of(Target.MULTI_CHILD_FAMILY))
                        .build())
                .detail(Detail.builder()
                        .target("1. 구리시에 거주하는 두자녀 이상 가정 ※ 부 또는 모가 구리시에 거주\n" +
                                "2. 막내 자녀가 만18세인 당해년도까지 지원 ※ 자녀 모두 구리시에 주민등록을 두고 실제로 거주해야 함.")
                        .criteria("1. 구리시에 거주하는 두자녀 이상 가정 ※ 부 또는 모가 구리시에 거주\n" +
                                "2. 막내 자녀가 만18세인 당해년도까지 지원 ※ 자녀 모두 구리시에 주민등록을 두고 실제로 거주해야 함.")
                        .content("위 자격 조건을 모두 충족하게 되면 다자녀가구 당 연 1회 50,000원 지급")
                        .applyMethod("1. 주소지 동 행정복지센터 방문 신청\n" +
                                "2. 구리시 가족복지과(아동정책팀)에서 각 동으로부터 신청 명단을 받은 후 검토 및 지급")
                        .tel("구리시청 가족복지과 031-550-8717")
                        .reference("「구리시 저출산대책 지원에 관한 조례」제18조(다자녀가정우대)")
                        .referenceLink("")
                        .build())
                .readCnt(0)
                .build();

        Welfare w3 = Welfare.builder()
                .welfareNo("684e59a2240b7223da3e25e3")
                .summary(Summary.builder()
                                .name("어린이집 생애최초 입학준비금 지원")
                                .areas(Set.of(Area.GUNPO))
                                .targets(Set.of())
                        .build())
                .detail(Detail.builder()
                        .target("어린이집을 이용하는 만0~5세 아동")
                        .criteria("1. 군포시에 1년 이상 주민등록상 주소를 둔 아동(※ 만1세 미만은 출생등록 시점부터 거주기간)\n" +
                                "2. 관내 소재 어린이집에 최초 입학 시 1회 지원(최대 10만원)")
                        .content("어린이집 입소시 실제 납부한 입학준비금 반환")
                        .applyMethod("1. 신청서 접수 : 각 동 주민센터\n" +
                                "2. 제출서류 : 통장사본, 입학준비금 납부영수증 → 경우에 따라 추가서류 요구할 수 있음\n" +
                                "       ※ 통장 사본은 부 또는 모의 통장, 아동본인 통장, (위탁가정) 위탁부모 통장, (조손가정 등) 실제 부양하고 있는 부양자(서류확인) 통장")
                        .tel("군포시 여성가족과 031-390-0265")
                        .reference("[군포시 보육조례] 제23조제7호")
                        .referenceLink("")
                        .build())
                .readCnt(0)
                .build();

        when(welfareService.filter(Area.GURI, Target.MULTI_CHILD_FAMILY))
                .thenReturn(List.of(w1, w2, w3));

        // recommend()는 w2, w3 추천
        when(aiFeignClient.recommend(any(RecommendReq.class)))
                .thenReturn(new RecommendRes(List.of(
                        "684e59a2240b7223da3e25e2", // w2
                        "684e59a2240b7223da3e25e3"  // w3
                )));

        // when
        List<WelfareSummaryRes> result = useCase.search(
                Area.GURI, Target.MULTI_CHILD_FAMILY, "어린이 혹은 자녀에 관한 복지를 추천받고싶어"
        );

        // then
        assertEquals(2, result.size());
        List<String> resultIds = result.stream()
                .map(WelfareSummaryRes::welfareNo)
                .toList();

        assertTrue(resultIds.contains("684e59a2240b7223da3e25e2")); // w2
        assertTrue(resultIds.contains("684e59a2240b7223da3e25e3")); // w3
        assertFalse(resultIds.contains("684e59a2240b7223da3e25e1")); // w1은 결과에 없어야 함
    }

    @Test
    void 복지_서비스_상세_조회() {
        // given
        Welfare welfare = Welfare.builder()
                .welfareNo("W1")
                .summary(Summary.builder()
                        .name("성남시 노인복지 바우처")
                        .areas(Set.of(Area.SEONGNAM))
                        .targets(Set.of(Target.LOW_INCOME))
                        .build())
                .detail(Detail.builder()
                        .target("성남시에 거주하는 65세 이상 어르신")
                        .criteria("성남시에 주민등록이 되어 있는 65세 이상 노인")
                        .content("월 5만원 상당의 지역화폐 바우처 지급")
                        .applyMethod("주소지 동 행정복지센터 방문 신청")
                        .tel("성남시 노인복지과 031-729-1111")
                        .reference("성남시 노인복지 조례 제10조")
                        .referenceLink("")
                        .build())
                .readCnt(0)
                .build();

        when(welfareService.read("W1")).thenReturn(welfare);

        // when
        WelfareDetailRes result = useCase.read("W1");

        // then
        assertEquals("W1", result.welfareNo());
        verify(viewCountService).increment("W1");
    }


    @Test
    void 자주_찾는_복지_서비스_조회() {
        // given
        when(viewCountService.readTop5()).thenReturn(List.of("W1", "W2"));

        Welfare welfare1 = Welfare.builder()
                .welfareNo("W1")
                .summary(Summary.builder()
                        .name("성남시 청년 월세 지원")
                        .areas(Set.of(Area.SEONGNAM))
                        .targets(Set.of())
                        .build())
                .detail(Detail.builder()
                        .target("성남시에 거주하는 무주택 청년 1인 가구")
                        .criteria("만 19~34세 청년, 소득 중위 150% 이하")
                        .content("월 최대 20만원, 최대 12개월 지원")
                        .applyMethod("성남시 홈페이지 온라인 신청")
                        .tel("031-729-3333")
                        .reference("성남시 청년지원 조례")
                        .referenceLink("")
                        .build())
                .readCnt(120)
                .build();

        Welfare welfare2 = Welfare.builder()
                .welfareNo("W2")
                .summary(Summary.builder()
                        .name("성남시 아동수당 지원")
                        .areas(Set.of(Area.SEONGNAM))
                        .targets(Set.of())
                        .build())
                .detail(Detail.builder()
                        .target("성남시에 거주하는 만 0~7세 아동")
                        .criteria("부모 소득 무관, 주소지 기준")
                        .content("월 10만원 현금 지급")
                        .applyMethod("읍면동 주민센터 신청 또는 복지로 홈페이지")
                        .tel("031-729-4444")
                        .reference("아동수당법")
                        .referenceLink("")
                        .build())
                .readCnt(98)
                .build();

        when(welfareService.read("W1")).thenReturn(welfare1);
        when(welfareService.read("W2")).thenReturn(welfare2);

        // when
        List<WelfareSummaryRes> result = useCase.readPopularWelfare();

        // then
        assertEquals(2, result.size());
        List<String> ids = result.stream().map(WelfareSummaryRes::welfareNo).toList();
        assertTrue(ids.contains("W1"));
        assertTrue(ids.contains("W2"));
    }

}
