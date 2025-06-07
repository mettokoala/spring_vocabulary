package com.example.demo.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

	@PutMapping("/{id}/status")
	public ResponseEntity<Void> updateWordStatus(@PathVariable Long id, @RequestBody Word updatedStatus) {
		if (wordService.updateStatus(id, updatedStatus.isStatus())) {
			// 204 No Content を返してボディは空
			return ResponseEntity.noContent().build();
		} else {
			// 404 Not Found
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteWord(@PathVariable Long id) {
		return wordService.deleteWord(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
	}

	@PostMapping
	public Word createWord(@RequestBody Word word) {
		return wordService.saveWord(word);
	}
}
