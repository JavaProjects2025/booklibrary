package com.example.booklibrary.config;

import com.example.booklibrary.model.Book;
import com.example.booklibrary.repository.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;

@Configuration
public class MongoConfig {

    @Bean
    public CommandLineRunner initData(BookRepository bookRepository) {
        return args -> {
            bookRepository.deleteAll();

            Book book1 = new Book();
            book1.setTitle("Clean Code");
            book1.setAuthor("Robert C. Martin");
            book1.setIsbn("978-0132350884");
            book1.setPublishedDate(LocalDate.of(2008, 8, 1));

            Book book2 = new Book();
            book2.setTitle("Effective Java");
            book2.setAuthor("Joshua Bloch");
            book2.setIsbn("978-0134685991");
            book2.setPublishedDate(LocalDate.of(2018, 1, 6));

            bookRepository.saveAll(List.of(book1, book2));
        };
    }
}
