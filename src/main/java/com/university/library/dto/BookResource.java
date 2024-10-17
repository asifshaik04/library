package com.university.library.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;


public record BookResource(@NotEmpty String title, @NotEmpty String author, @Positive int publicationYear,
                           @PositiveOrZero int availableCopies) {


}
