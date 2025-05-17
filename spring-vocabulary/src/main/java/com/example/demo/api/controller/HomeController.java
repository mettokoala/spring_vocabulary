package com.example.demo.api.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

	private final Job csvChunkJob;
	private final JobLauncher jobLauncher;

	public HomeController(Job csvChunkJob, JobLauncher jobLauncher) {
		this.csvChunkJob = csvChunkJob;
		this.jobLauncher = jobLauncher;
	}

	@GetMapping("/import")
	public ResponseEntity<String> importCsv() throws Exception {

		String filePath = "src/main/resources/file/input.csv";
		JobParameters params = new JobParametersBuilder().addString("filePath", filePath)
				.addLong("time", System.currentTimeMillis()).toJobParameters();

		JobExecution execution = jobLauncher.run(csvChunkJob, params);
		return ResponseEntity.ok("バッチ完了:" + execution.getStatus());
	}
}
