package com.example.booklibrary.repository;

import com.example.booklibrary.model.Book;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookRepository extends MongoRepository<Book, String> {
    Page<Book> findAll(Pageable pageable);
}