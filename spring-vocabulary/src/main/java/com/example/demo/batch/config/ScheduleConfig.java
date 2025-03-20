package com.example.demo.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class ScheduleConfig {

	private final JobLauncher jobLauncher;
	private final Job resetTaskletJob;

	public ScheduleConfig(JobLauncher jobLauncher, Job resetTaskletJob) {
		this.jobLauncher = jobLauncher;
		this.resetTaskletJob = resetTaskletJob;
	}

	@Scheduled(cron = "*/5 * * * * *")
	public void ScheduleReset() throws Exception {
		JobParameters jobparameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis())
				.toJobParameters();
		jobLauncher.run(resetTaskletJob, jobparameters);
	}
}
