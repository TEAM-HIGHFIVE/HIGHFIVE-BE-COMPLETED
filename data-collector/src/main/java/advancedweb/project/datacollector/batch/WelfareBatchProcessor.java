package advancedweb.project.datacollector.batch;

import advancedweb.project.datacollector.application.dto.response.api.WelfareItem;
import advancedweb.project.datacollector.application.dto.response.crawling.CrawlingResponse;
import advancedweb.project.datacollector.domain.entity.Detail;
import advancedweb.project.datacollector.domain.entity.Summary;
import advancedweb.project.datacollector.domain.entity.Welfare;
import advancedweb.project.datacollector.domain.entity.enums.Area;
import advancedweb.project.datacollector.domain.entity.enums.Target;
import advancedweb.project.datacollector.domain.service.WelfareCrawlingService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class WelfareBatchProcessor implements ItemProcessor<WelfareItem, Welfare> {

    private final WelfareCrawlingService welfareCrawlingService;

    @Override
    public Welfare process(WelfareItem item) throws Exception {
        CrawlingResponse crawling = welfareCrawlingService.crawl(item.serviceLink());

        Set<Target> targets = item.supportTarget() != null ?
                Set.of(item.supportTarget().split(",\\s*")).stream()
                        .map(Target::from)
                        .collect(Collectors.toSet()) :
                Set.of();

        Set<Area> areas = (item.sigunName() != null && !item.sigunName().isBlank() && !item.sigunName().equals("-"))
                ? Set.of(Area.from(item.sigunName()))
                : Set.of();

        return Welfare.builder()
                .summary(
                        Summary.builder()
                                .name(item.serviceName() != null ? item.serviceName() : null)
                                .areas(areas)
                                .targets(targets)
                                .build()
                )
                .detail(
                        Optional.ofNullable(crawling)
                                .map(c -> Detail.builder()
                                        .target(c.targetDetail())
                                        .criteria(c.criteria())
                                        .content(c.content())
                                        .applyMethod(c.applyMethod())
                                        .tel(c.tel())
                                        .referenceLink(c.referenceLink())
                                        .reference(c.reference())
                                        .build())
                                .orElse(null)
                )
                .readCnt(0)
                .build();
    }
}
