package org.bookrental.bookrentalsystem.Service;

import jakarta.validation.Valid;
import org.bookrental.bookrentalsystem.Controller.PromotionStatus;
import org.bookrental.bookrentalsystem.Data.Entity.BookPromotion;
import org.bookrental.bookrentalsystem.Data.Mapper.BookPromotionMapper;
import org.bookrental.bookrentalsystem.Data.Request.BookPromotionRequest;
import org.bookrental.bookrentalsystem.Data.Response.BookPromotionResponse;
import org.bookrental.bookrentalsystem.Repository.BookPromotionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookPromotionService {
    private final BookPromotionRepository bookPromotionRepository;
    private final BookPromotionMapper bookPromotionMapper;

    public BookPromotionService(BookPromotionRepository bookPromotionRepository, BookPromotionMapper bookPromotionMapper) {
        this.bookPromotionRepository = bookPromotionRepository;
        this.bookPromotionMapper = bookPromotionMapper;
    }

    public List<BookPromotionResponse> getBookPromotionsByStatus(Pageable pageable, PromotionStatus status) {
        Page<BookPromotion> promotionList = getBookPromotionsByStatusOrThrow(pageable, status);

        return promotionList.stream().map(bookPromotionMapper::toBookPromotionResponse).toList();
    }

    private Page<BookPromotion> getBookPromotionsByStatusOrThrow(Pageable pageable, PromotionStatus status) {
        LocalDateTime now = LocalDateTime.now();
        return switch (status) {
            case ALL -> bookPromotionRepository.findAll(pageable);
            case ACTIVE ->
                    bookPromotionRepository.findAllByStartDateLessThanEqualAndEndDateGreaterThanEqual(now, now, pageable);
            case INACTIVE -> bookPromotionRepository.findAllByEndDateBeforeOrStartDateAfter(now, now, pageable);
        };
    }

    public BookPromotionResponse createBookPromotion(@Valid BookPromotionRequest request) {
        BookPromotion bookPromotion = bookPromotionMapper.toEntity(request);
        BookPromotion saved = bookPromotionRepository.save(bookPromotion);

        return bookPromotionMapper.toBookPromotionResponse(saved);
    }
}
