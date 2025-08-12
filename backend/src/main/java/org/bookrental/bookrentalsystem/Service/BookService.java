package org.bookrental.bookrentalsystem.Service;

import com.github.dockerjava.api.exception.NotFoundException;
import org.bookrental.bookrentalsystem.Config.Exception.BookNotFoundException;
import org.bookrental.bookrentalsystem.Data.Entity.Book;
import org.bookrental.bookrentalsystem.Data.Entity.BookInventory;
import org.bookrental.bookrentalsystem.Data.Entity.BookPromotion;
import org.bookrental.bookrentalsystem.Data.Mapper.BookMapper;
import org.bookrental.bookrentalsystem.Data.Request.BookRequest;
import org.bookrental.bookrentalsystem.Data.Response.BookDetailsContext;
import org.bookrental.bookrentalsystem.Data.Response.BookDetailsResponse;
import org.bookrental.bookrentalsystem.Data.Response.BookSimpleContext;
import org.bookrental.bookrentalsystem.Data.Response.BookSimpleResponse;
import org.bookrental.bookrentalsystem.Repository.BookInventoryRepository;
import org.bookrental.bookrentalsystem.Repository.BookPromotionRepository;
import org.bookrental.bookrentalsystem.Repository.BookRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final BookPromotionRepository bookPromotionRepository;
    private final BookInventoryRepository bookInventoryRepository;
    private final BookMapper bookMapper;

    public BookService(BookRepository bookRepository, BookPromotionRepository bookPromotionRepository, BookInventoryRepository bookInventoryRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookPromotionRepository = bookPromotionRepository;
        this.bookInventoryRepository = bookInventoryRepository;
        this.bookMapper = bookMapper;
    }

    public BookDetailsResponse getBookDetailsById(Long bookId) {
        Book book = getBookOrThrow(bookId);

        Optional<BookPromotion> promotion = getBookPromotion(book.getId(), LocalDateTime.now());

        var context = getBookDetailsContext(promotion, book);

        return bookMapper.toBookDetailsResponse(context);
    }

    public BookDetailsResponse createBook(BookRequest request) {
        Book book = bookMapper.toEntity(request);
        Book saved = bookRepository.save(book);

        Optional<BookPromotion> promotion = getBookPromotion(saved.getId(), LocalDateTime.now());
        var context = getBookDetailsContext(promotion, saved);

        return bookMapper.toBookDetailsResponse(context);
    }

    public List<BookSimpleResponse> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable).map(book -> {
            Optional<BookPromotion> promotion = getBookPromotion(book.getId(), LocalDateTime.now());

            var context = getBookSimpleContext(promotion, book);

            return bookMapper.toBookSimpleResponse(context);
        }).toList();
    }

    private Optional<BookPromotion> getBookPromotion(Long bookId, LocalDateTime now) {
        return bookPromotionRepository.findFirstByBookIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(bookId, now, now);
    }

    private Book getBookOrThrow(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found"));
    }

    private BookDetailsContext getBookDetailsContext(Optional<BookPromotion> promotion, Book book) {
        BigDecimal rentPromo = promotion.map(BookPromotion::getRentalPromoPrice).orElse(null);
        BigDecimal buyPromo = promotion.map(BookPromotion::getPurchasePromoPrice).orElse(null);

        Integer rentalStock = getRentalStockCountOrThrow(book.getId());
        Integer purchaseStock = getPurchaseStockCountOrThrow(book.getId());

        return new BookDetailsContext(book, rentPromo, buyPromo, rentalStock, purchaseStock);
    }

    private BookSimpleContext getBookSimpleContext(Optional<BookPromotion> promotion, Book book) {
        BigDecimal rentPromo = promotion.map(BookPromotion::getRentalPromoPrice).orElse(null);
        BigDecimal buyPromo = promotion.map(BookPromotion::getPurchasePromoPrice).orElse(null);

        return new BookSimpleContext(book, rentPromo, buyPromo);
    }

    private Integer getRentalStockCountOrThrow(Long bookId) {
        BookInventory bookInventory = bookInventoryRepository.findByBookId(bookId)
                .orElseThrow(() -> new NotFoundException("Stock Info not found"));

        return bookInventory
                .getRentalStockCount();
    }

    private Integer getPurchaseStockCountOrThrow(Long bookId) {
        BookInventory bookInventory = bookInventoryRepository.findByBookId(bookId)
                .orElseThrow(() -> new NotFoundException("Stock Info not found"));

        return bookInventory
                .getPurchaseStockCount();
    }
}
