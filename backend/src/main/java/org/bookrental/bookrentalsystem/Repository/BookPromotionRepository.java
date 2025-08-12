package org.bookrental.bookrentalsystem.Repository;

import org.bookrental.bookrentalsystem.Data.Entity.BookPromotion;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.Optional;

public interface BookPromotionRepository extends JpaRepository<BookPromotion, Long> {
    Optional<BookPromotion> findFirstByBookIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(Long bookId, LocalDateTime startDate, LocalDateTime endDate);

    Page<BookPromotion> findAllByStartDateLessThanEqualAndEndDateGreaterThanEqual(
            LocalDateTime start,
            LocalDateTime end,
            Pageable pageable
    );

    Page<BookPromotion> findAllByEndDateBeforeOrStartDateAfter(
            LocalDateTime endBefore,
            LocalDateTime startAfter,
            Pageable pageable
    );}
