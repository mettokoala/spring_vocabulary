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
		List<Word> all = wordRepository.findAll();
		return all.stream().filter(word -> !word.isStatus()).toList();
	}

	public boolean updateStatusOk(Long id) {
		Optional<Word> word = wordRepository.findById(id);
		if (word.isPresent()) {
			Word updateWord = word.get();
			updateWord.setStatus(true);
			updateWord.setNextPresentation(LocalDateTime.now().plusMinutes(1));
			wordRepository.save(updateWord);
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

	public void saveWord(Word word) {
		wordRepository.save(word);
	}
}
