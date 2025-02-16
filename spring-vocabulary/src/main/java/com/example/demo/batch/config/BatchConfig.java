package com.example.demo.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.example.demo.batch.tasklet.ResetTasklet;

@Configuration
public class BatchConfig {

	private final JobLauncher jobLauncher;
	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManager;
	private final ResetTasklet resetTasklet;

	public BatchConfig(JobLauncher jobLauncher, JobRepository jobRepository,
			PlatformTransactionManager transactionManager, ResetTasklet resetTasklet) {
		super();
		this.jobLauncher = jobLauncher;
		this.jobRepository = jobRepository;
		this.transactionManager = transactionManager;
		this.resetTasklet = resetTasklet;
	}

	@Bean
	public Job resetTaskletJob() {
		return new JobBuilder("resetTaskletJob", jobRepository).start(resetTaskletStep()).build();
	}

	@Bean
	public Step resetTaskletStep() {
		return new StepBuilder("resetTaskletStep", jobRepository).tasklet(resetTasklet, transactionManager).build();
	}
}
