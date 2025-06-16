package advancedweb.project.datacollector.batch;

import advancedweb.project.datacollector.domain.entity.Welfare;
import advancedweb.project.datacollector.domain.repository.WelfareRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WelfareBatchWriter implements ItemWriter<Welfare> {

    private final WelfareRepository welfareRepository;

    @Override
    public void write(Chunk<? extends Welfare> chunk) throws Exception {
        System.out.println("chunk = " + chunk.getItems());
        List<? extends Welfare> items = chunk.getItems().stream()
                .filter(welfare -> welfare.getDetail() != null)
                .toList();
        welfareRepository.saveAll(items);
    }
}
