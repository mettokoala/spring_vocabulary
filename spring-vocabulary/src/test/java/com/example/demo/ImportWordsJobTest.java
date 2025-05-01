package com.example.demo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBatchTest
@SpringBootTest
public class ImportWordsJobTest {

	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	@Autowired
	private JobRepositoryTestUtils jobRepositoryTestUtils;

	@Autowired
	private Job importWordsJob;

	@BeforeEach
	void setUp() {
		// 自動定義された JobLauncherTestUtils に対してジョブをセット
		jobLauncherTestUtils.setJob(importWordsJob);
	}

	@AfterEach
	public void cleanUp() {
		jobRepositoryTestUtils.removeJobExecutions();
	}

	@Test
	public void testImportWordsJob() throws Exception {
		JobParameters params = new JobParametersBuilder()
				.addString("filePath", "src/main/resources/file/input.csv")
				.toJobParameters();
		JobExecution exec = jobLauncherTestUtils.launchJob(params);
		assertEquals(BatchStatus.COMPLETED, exec.getStatus());
	}
}