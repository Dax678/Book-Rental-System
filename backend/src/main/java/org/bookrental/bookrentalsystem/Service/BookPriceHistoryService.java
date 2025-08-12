package org.bookrental.bookrentalsystem.Service;

import org.bookrental.bookrentalsystem.Data.Entity.BookPriceHistory;
import org.bookrental.bookrentalsystem.Data.Mapper.BookPriceHistoryMapper;
import org.bookrental.bookrentalsystem.Data.Response.BookPriceHistoryResponse;
import org.bookrental.bookrentalsystem.Repository.BookPriceHistoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookPriceHistoryService {
    private final BookPriceHistoryRepository bookPriceHistoryRepository;
    private final BookPriceHistoryMapper bookPriceHistoryMapper;

    public BookPriceHistoryService(BookPriceHistoryRepository bookPriceHistoryRepository, BookPriceHistoryMapper bookPriceHistoryMapper) {
        this.bookPriceHistoryRepository = bookPriceHistoryRepository;
        this.bookPriceHistoryMapper = bookPriceHistoryMapper;
    }

    public List<BookPriceHistoryResponse> getAllBookPriceHistory(Pageable pageable) {
        Page<BookPriceHistory> promotionList = bookPriceHistoryRepository.findAll(pageable);
        return promotionList.stream().map(bookPriceHistoryMapper::toBookPromotionResponse).toList();
    }
}
