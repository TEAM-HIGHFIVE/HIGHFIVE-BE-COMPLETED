package advancedweb.project.datacollector.batch;

import advancedweb.project.datacollector.application.dto.response.api.WelfareItem;
import advancedweb.project.datacollector.domain.entity.Welfare;
import advancedweb.project.datacollector.domain.repository.WelfareRepository;
import advancedweb.project.datacollector.domain.service.WelfareCrawlingService;
import advancedweb.project.datacollector.domain.service.WelfareDataFetchService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class WelfareBatchConfig {

    private final WelfareRepository welfareRepository;
    private final WelfareDataFetchService welfareDataFetchService;
    private final WelfareCrawlingService welfareCrawlingService;

    @Bean
    public Job welfareJob(JobRepository jobRepository, Step initStep, Step welfareStep) {
        return new JobBuilder("welfareJob", jobRepository)
                .start(initStep)
                .next(welfareStep)
                .build();
    }

    @Bean
    public Step welfareStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("welfareStep", jobRepository)
                .<WelfareItem, Welfare>chunk(100, transactionManager)
                .reader(welfareItemReader())
                .processor(WelfareItemProcessor())
                .writer(WelfareItemWriter())
                .build();
    }

    @Bean
    public Step initStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("initStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    welfareRepository.deleteAll();
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public WelfareBatchReader welfareItemReader() {
        return new WelfareBatchReader(welfareDataFetchService);
    }

    @Bean
    public WelfareBatchProcessor WelfareItemProcessor() {
        return new WelfareBatchProcessor(welfareCrawlingService);
    }

    @Bean
    public WelfareBatchWriter WelfareItemWriter() {
        return new WelfareBatchWriter(welfareRepository);
    }
}

