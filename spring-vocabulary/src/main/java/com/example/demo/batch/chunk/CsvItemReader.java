package com.example.demo.batch.chunk;

import java.nio.charset.StandardCharsets;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import com.example.demo.domain.entity.Word;

import lombok.extern.slf4j.Slf4j;

@Component
@StepScope
@Slf4j
public class CsvItemReader extends FlatFileItemReader<Word> implements InitializingBean {

	@Value("#{jobParameters['filePath']}")
	private String filePath;

	@Override
	public void afterPropertiesSet() throws Exception {
		log.info("-----CSVデータ登録開始 filePath={}-----", filePath);

		setResource(new FileSystemResource(filePath));
		setLinesToSkip(1);
		setEncoding(StandardCharsets.UTF_8.name());

		super.afterPropertiesSet();
	}

}
