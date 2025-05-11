package com.example.demo.api;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

	private final Job importWordsJob;
	private final JobLauncher jobLauncher;

	public HomeController(
			JobLauncher jobLauncher,
			@Qualifier("csvChunkJob") Job importWordsJob) {
		this.jobLauncher = jobLauncher;
		this.importWordsJob = importWordsJob;
	}

	@GetMapping("/import")
	public ResponseEntity<String> importCsv() {
		// ① 固定パスを指定
		String filePath = "src/main/resources/file/input.csv";
		// ② 毎回ユニークになるよう JobParameters を付与
		JobParameters params = new JobParametersBuilder()
				.addString("filePath", filePath)
				.addLong("time", System.currentTimeMillis())
				.toJobParameters();

		try {
			// ③ JobLauncher でジョブを起動
			JobExecution execution = jobLauncher.run(importWordsJob, params);
			return ResponseEntity.ok("バッチ完了: " + execution.getStatus());
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
				| JobParametersInvalidException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("バッチ起動エラー: " + e.getMessage());
		}
	}
}