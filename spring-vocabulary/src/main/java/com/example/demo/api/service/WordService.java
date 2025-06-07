package com.example.demo.api.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.domain.entity.Word;
import com.example.demo.domain.repository.WordRepository;

@Service
public class WordService {

	private final WordRepository wordRepository;

	public WordService(WordRepository wordRepository) {
		this.wordRepository = wordRepository;
	}

	public List<Word> findAllWords() {
		return wordRepository.findAll().stream().filter(word -> !word.isStatus()).toList();
	}

	public boolean updateStatus(Long id, boolean status) {
		Optional<Word> word = wordRepository.findById(id);
		if (word.isPresent()) {
			Word existingWord = word.get();
			existingWord.setStatus(status);
			// 復讐タイミングの設定
			if (status) {
				existingWord.setNextPresentation(LocalDateTime.now().plusMinutes(1));
			}
			wordRepository.save(existingWord);
			return true;
		} else {
			return false;
		}
	}

	public boolean deleteWord(Long id) {
		Optional<Word> word = wordRepository.findById(id);
		if (word.isPresent()) {
			wordRepository.delete(word.get());
			return true;
		} else {
			return false;
		}
	}

	public Word saveWord(Word word) {
		return wordRepository.save(word);
	}
}
