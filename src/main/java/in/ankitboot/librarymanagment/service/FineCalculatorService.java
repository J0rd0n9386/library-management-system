package in.ankitboot.librarymanagment.service;


import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class FineCalculatorService {
    private static final double FINE_PER_DAY = 5.0;

    public double calculateFine(LocalDate dueDate, LocalDate returnDate) {
        if (returnDate.isAfter(dueDate)) {
            long daysLate = ChronoUnit.DAYS.between(dueDate, returnDate);
            return daysLate * FINE_PER_DAY;

        }
        return 0.0;
    }
}