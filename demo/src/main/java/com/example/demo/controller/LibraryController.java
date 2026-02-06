package com.example.demo.controller;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.Book;
import com.example.demo.entities.Library;
import com.example.demo.entities.User;
import com.example.demo.repositories.BookRepository;
import com.example.demo.repositories.LibraryRepository;
import com.example.demo.repositories.UserRepository;

@RestController
@RequestMapping("/api/library")
@CrossOrigin("http://localhost:5173")
public class LibraryController {

  @Autowired
  private LibraryRepository libraryRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private BookRepository bookRepository;

  @PostMapping("/add/{bookId}")
  public ResponseEntity<?> addToLibrary(@PathVariable int bookId, @AuthenticationPrincipal UserDetails userDetails) {
    Optional<User> userOpt = userRepository.findByEmail(userDetails.getUsername());
    if (userOpt.isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");

    Optional<Book> bookOpt = bookRepository.findById(bookId);
    if (bookOpt.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found");

    User user = userOpt.get();
    Book book = bookOpt.get();

    // Check if already added
    if (libraryRepository.existsByUserAndBook(user, book)) {
      return ResponseEntity.badRequest().body("Book already in library");
    }

    Library libraryEntry = new Library(user, book);
    libraryRepository.save(libraryEntry);

    return ResponseEntity.ok("Book added to library");
  }

  @GetMapping("/my")
  public ResponseEntity<?> getMyLibrary(@AuthenticationPrincipal UserDetails userDetails) {
    Optional<User> userOpt = userRepository.findByEmail(userDetails.getUsername());
    if (userOpt.isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");

    List<Library> libraryList = libraryRepository.findByUser(userOpt.get());
    return ResponseEntity.ok(libraryList);
  }
  // ...existing code...

@DeleteMapping("/remove/{bookId}")
public ResponseEntity<?> removeFromLibrary(@PathVariable int bookId, @AuthenticationPrincipal UserDetails userDetails) {
    Optional<User> userOpt = userRepository.findByEmail(userDetails.getUsername());
    if (userOpt.isEmpty()) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");

    Optional<Book> bookOpt = bookRepository.findById(bookId);
    if (bookOpt.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found");

    User user = userOpt.get();
    Book book = bookOpt.get();

    Optional<Library> libraryEntryOpt = libraryRepository.findByUserAndBook(user, book);
    if (libraryEntryOpt.isEmpty()) {
        return ResponseEntity.badRequest().body("Book not in library");
    }

    libraryRepository.delete(libraryEntryOpt.orElseThrow());
    return ResponseEntity.ok("Book removed from library");
}
}
