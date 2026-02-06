package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.demo.entities.Book;
import com.example.demo.entities.User;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    List<Book> findByUser(User user);
    @Query("SELECT b FROM Book b WHERE LOWER(b.name) LIKE %:name%")
    List<Book> searchByName(@Param("name") String name);
}
