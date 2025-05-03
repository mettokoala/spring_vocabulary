package com.example.demo.batch.chunk;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import com.example.demo.domain.entity.Word;
import com.example.demo.domain.repository.WordRepository;

import lombok.extern.slf4j.Slf4j;

@Component
@StepScope
@Slf4j
public class CsvItemWriter implements ItemWriter<Word> {
	private final WordRepository wordRepository;

	public CsvItemWriter(WordRepository wordRepository) {
		this.wordRepository = wordRepository;
	}

	/**
	 * 書き込み処理: Chunkのデータをデータベースに保存
	 * @param chunk 処理対象のデータ (Wordオブジェクトのリスト)
	 * @throws Exception 書き込みエラー時の例外
	 */
	@Override
	public void write(Chunk<? extends Word> chunk) throws Exception {
		try {
			log.info("登録データ＝{}", chunk);

			// チャンク内の各データを保存
			for (Word word : chunk) {
				wordRepository.save(word);
			}
		} catch (Exception e) {
			throw e;
		}
		log.info("-----CSVデータ登録完了-----");
	}
}
