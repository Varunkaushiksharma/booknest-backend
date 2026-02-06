package com.example.demo.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entities.Book;
import com.example.demo.repositories.BookRepository;
import com.example.demo.service.BookService;

import org.springframework.ui.Model;

@RestController
@RequestMapping("/api")
public class Home {

  @Autowired
  private BookRepository bookRepository;
  @Autowired
  private BookService bookService;

  // Home page – show only a few books (4)
  @GetMapping("/home")
  public List<Book> showHomePage() {
     return bookRepository.findAll()
                         .stream()
                         .limit(4)
                         .toList();
  }

    @GetMapping("/home/books")
  public List<Book> getAllBooks() {
    return bookRepository.findAll();
  }

  //   @GetMapping("/api/books/{id}")
  // public Book getBookById(@PathVariable int id) {
  //   return bookService.getBookById(id);
  // }


  @GetMapping("/books/read/{id}")
  public Book readBook(@PathVariable int id, Model model) {
    return bookService.getBookById(id); // Fetch book from DB
   // Loads read.html
  }

  @GetMapping("/account")
  public String accountPage(Model model, Principal principal) {
    model.addAttribute("username", principal.getName());
    return "account"; // account.html in templates/
  }

  @GetMapping("/library")
  public String library() {
    return "library";
  }

}
