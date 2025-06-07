package com.example.demo.api.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.api.service.WordService;
import com.example.demo.domain.entity.Word;

@RestController
@RequestMapping("/words")
public class WordController {

	private final WordService wordService;

	public WordController(WordService wordService) {
		this.wordService = wordService;
	}

	@GetMapping
	public List<Word> getAllWords() {
		return wordService.findAllWords();
	}
}
