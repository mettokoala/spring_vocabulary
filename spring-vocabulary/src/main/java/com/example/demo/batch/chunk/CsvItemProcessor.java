package com.example.demo.batch.chunk;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.example.demo.domain.entity.Word;

import lombok.extern.slf4j.Slf4j;

@Component("CsvItemProcessor")
@StepScope
@Slf4j
public class CsvItemProcessor implements ItemProcessor<Word, Word> {

	/**
	 * 処理: Wordオブジェクトに追加加工を実施
	 * @param item 処理対象のWordオブジェクト
	 * @return 処理後のWordオブジェクト
	 * @throws Exception 処理エラー時の例外
	 */
	@Override
	public Word process(Word item) throws Exception {
		try {
			log.info("Csvファイルの読み込みを実行します。{}", item);
			// Wordオブジェクトのフィールドを加工
			item.setNextPresentation(null); // 次回表示日時をリセット
			item.setStatus(false); // ステータスを未完了に設定
			return item;
		} catch (Exception e) {
			// エラー件数をカウント
			throw e;
		}
	}
}
