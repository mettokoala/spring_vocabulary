package com.example.demo.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.example.demo.batch.tasklet.ResetTasklet;
import com.example.demo.domain.entity.Word;

@Configuration
public class BatchConfig {

	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManager;
	private final ResetTasklet resetTasklet;
	private final ItemReader<Word> csvItemReader;
	private final ItemProcessor<Word, Word> csvItemProcessor;
	private final ItemWriter<Word> csvItemWriter;

	public BatchConfig(JobRepository jobRepository, PlatformTransactionManager transactionManager,
			ResetTasklet resetTasklet, ItemReader<Word> csvItemReader, ItemProcessor<Word, Word> csvItemProcessor,
			ItemWriter<Word> csvItemWriter) {
		this.jobRepository = jobRepository;
		this.transactionManager = transactionManager;
		this.resetTasklet = resetTasklet;
		this.csvItemReader = csvItemReader;
		this.csvItemProcessor = csvItemProcessor;
		this.csvItemWriter = csvItemWriter;
	}

	@Bean
	public Job resetTaskletJob() {
		return new JobBuilder("resetTaskletJob", jobRepository).start(resetTaskletStep()).build();
	}

	@Bean
	public Step resetTaskletStep() {
		return new StepBuilder("resetTaskletStep", jobRepository).tasklet(resetTasklet, transactionManager).build();
	}

	@Bean
	public Step csvToDatabaseStep() {
		return new StepBuilder("csvToDatabaseStep", jobRepository)
				.<Word, Word> chunk(10, transactionManager)
				.reader(csvItemReader)
				.processor(csvItemProcessor)
				.writer(csvItemWriter)
				.build();
	}

	@Bean
	public Job importWordsJob() {
		return new JobBuilder("importWordsJob", jobRepository)
				.start(csvToDatabaseStep())
				.build();
	}
}
