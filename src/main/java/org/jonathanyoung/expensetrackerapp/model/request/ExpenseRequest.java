package org.jonathanyoung.expensetrackerapp.model.request;

public record ExpenseRequest(
        String category,
        double price
) {

}
