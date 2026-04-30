package edu.ucol.taller3jee.config;

import edu.ucol.taller3jee.batch.TemaInput;
import edu.ucol.taller3jee.batch.TemaProcessor;
import edu.ucol.taller3jee.batch.TemaReader;
import edu.ucol.taller3jee.batch.TemaWriter;
import edu.ucol.taller3jee.entity.Tema;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfiguration {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final TemaReader temaReader;
    private final TemaProcessor temaProcessor;
    private final TemaWriter temaWriter;

    public BatchConfiguration(JobRepository jobRepository,
                              PlatformTransactionManager platformTransactionManager,
                              TemaReader temaReader,
                              TemaProcessor temaProcessor,
                              TemaWriter temaWriter) {
        this.jobRepository = jobRepository;
        this.platformTransactionManager = platformTransactionManager;
        this.temaReader = temaReader;
        this.temaProcessor = temaProcessor;
        this.temaWriter = temaWriter;
    }

    @Bean
    public Step loadCourseContentStep() {
        return new StepBuilder("loadCourseContentStep", jobRepository)
                .<TemaInput, Tema>chunk(10, platformTransactionManager)
                .reader(temaReader)
                .processor(temaProcessor)
                .writer(temaWriter)
                .faultTolerant()
                .skipLimit(5)
                .skip(Exception.class)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public Job loadCourseContentJob() {
        return new JobBuilder("loadCourseContentJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(loadCourseContentStep())
                .build();
    }
}
