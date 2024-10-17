package com.university.library.dto;

public record BookDTO(String isbn, String title, String author, int publicationYear, int availableCopies) {
}
