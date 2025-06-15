package com.example.demo.api.service;

import java.util.List;

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
}
