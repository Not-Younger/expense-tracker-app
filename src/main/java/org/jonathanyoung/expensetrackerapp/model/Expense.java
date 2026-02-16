package org.jonathanyoung.expensetrackerapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Expense {

    private UUID id;
    private Category category;
    private double price;
    private Instant date;
}
