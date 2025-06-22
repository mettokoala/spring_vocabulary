package com.example.demo.api.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class HomeController {

	private final Job csvChunkJob;
	private final JobLauncher jobLauncher;

	public HomeController(Job csvChunkJob, JobLauncher jobLauncher) {
		this.csvChunkJob = csvChunkJob;
		this.jobLauncher = jobLauncher;
	}

	@PostMapping("/import")
	public ResponseEntity<String> importCsv(@RequestParam("file") MultipartFile file) throws Exception {

		if (file.isEmpty()) {
			return ResponseEntity.badRequest().body("CSVファイルを選択してください");
		}

		String projectRoot = System.getProperty("user.dir");
		Path uploadDir = Paths.get(projectRoot, "upload");
		Files.createDirectories(uploadDir);
		Path targetPath = uploadDir.resolve(file.getOriginalFilename());
		file.transferTo(targetPath);

		JobParameters params = new JobParametersBuilder().addString("filePath", targetPath.toString())
				.addLong("time", System.currentTimeMillis()).toJobParameters();

		JobExecution execution = jobLauncher.run(csvChunkJob, params);
		return ResponseEntity.ok("バッチ完了:" + execution.getStatus());
	}
}
