package com.example.demo.api.controller;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

	@GetMapping("/import")
	public ResponseEntity<String> importCsv() throws Exception {

		String filePath = "src/main/resources/file/input.csv";
		JobParameters params = new JobParametersBuilder().addString("filePath", filePath)
				.addLong("time", System.currentTimeMillis()).toJobParameters();
		return ResponseEntity.ok("バッチ完了");
	}
}
