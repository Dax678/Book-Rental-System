package org.bookrental.bookrentalsystem.Data.Response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookResponse {
    private Long id;
    private String isbn;
    private String title;
    private String author;
    private boolean available;
}