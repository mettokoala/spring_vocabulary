package com.example.demo.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.entity.Word;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {

	@Modifying
	@Transactional
	@Query("UPDATE Word w SET w.status = false WHERE w.status = true AND w.nextPresentation <= CURRENT_TIMESTAMP")
	void resertStatus();

}
