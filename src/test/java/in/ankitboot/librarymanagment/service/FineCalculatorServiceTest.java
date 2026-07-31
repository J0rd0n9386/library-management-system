
package in.ankitboot.librarymanagment.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FineCalculatorServiceTest {

    private final FineCalculatorService fineCalculatorService = new FineCalculatorService();

    @Test
    @DisplayName("Return on time -> fine zero honi chahiye")
    void testNoFine_WhenReturnedOnTime() {
        LocalDate dueDate = LocalDate.of(2026, 7, 18);
        LocalDate returnDate = LocalDate.of(2026, 7, 18);
        double fine = fineCalculatorService.calculateFine(dueDate, returnDate);
        /*
assertEquals(0.0, fine) — assertion — check kar raha hai ki fine ka result bilkul 0.0 hona chahiye
 */
        assertEquals(0.0, fine);
    }

    @Test
    @DisplayName("Return before due Date")
    void testNoFine_WhenReturnBeforeDueDate() {
        LocalDate dueDate = LocalDate.of(2026, 7, 18);
        LocalDate returnDate = LocalDate.of(2026, 7, 17);
        double fine = fineCalculatorService.calculateFine(dueDate, returnDate);
        assertEquals(0.0, fine);
    }

    @Test
    @DisplayName("3 Days late -> fine = 15 (3 * 5)")
    void testFine_WhenReturnAfterDueDate() {
        LocalDate dueDate = LocalDate.of(2026, 7, 18);
        LocalDate returnDate = LocalDate.of(2026, 7, 21);
        double fine = fineCalculatorService.calculateFine(dueDate, returnDate);
        assertEquals(15.0, fine);
    }

    @Test
    @DisplayName("1 Days Late -> fine = 5")
    void testFine_OneDayLate() {
        LocalDate dueDate = LocalDate.of(2026, 7, 18);
        LocalDate returnDate = LocalDate.of(2026, 7, 19);
        double fine = fineCalculatorService.calculateFine(dueDate, returnDate);
        assertEquals(5.0, fine);
    }
}