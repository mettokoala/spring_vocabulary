package com.example.demo.batch.chunk;

import java.nio.charset.StandardCharsets;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
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

	/** ジョブパラメータから CSV ファイルパスを取得 */
	@Value("#{jobParameters['filePath']}")
	private String filePath;

	/**
	 * Bean のプロパティ注入完了後に呼ばれる
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		log.info("-----CSVデータ登録開始 filePath={}-----", filePath);

		// ここで CSV リソースやエンコーディング等を設定
		setResource(new FileSystemResource(filePath));
		setLinesToSkip(1);
		setEncoding(StandardCharsets.UTF_8.name());

		// CSV のマッピング設定
		DefaultLineMapper<Word> lineMapper = new DefaultLineMapper<>();
		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		tokenizer.setNames("question", "answer");

		BeanWrapperFieldSetMapper<Word> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
		fieldSetMapper.setTargetType(Word.class);

		lineMapper.setLineTokenizer(tokenizer);
		lineMapper.setFieldSetMapper(fieldSetMapper);
		setLineMapper(lineMapper);

		// 親クラス（FlatFileItemReader）の初期化処理
		super.afterPropertiesSet();
	}
}
