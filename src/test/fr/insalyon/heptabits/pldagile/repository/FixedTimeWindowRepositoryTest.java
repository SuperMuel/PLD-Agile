package fr.insalyon.heptabits.pldagile.repository;

import fr.insalyon.heptabits.pldagile.model.TimeWindow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FixedTimeWindowRepositoryTest {

    FixedTimeWindowRepository fixedTimeWindowRepository;

    @BeforeEach
    void setUp() {
        fixedTimeWindowRepository = new FixedTimeWindowRepository();
    }



    @Test
    void getAll() {

    }

    @Test
    void getAllReturnsSortedTimeWindows() {
        final var timeWindows = fixedTimeWindowRepository.getAll();
        for (int i = 0; i < timeWindows.size() - 1; i++) {
            assertTrue(timeWindows.get(i).getStart().isBefore(timeWindows.get(i + 1).getStart()));
        }
    }

    @Test
    void getAllReturnsNonEmptyList() {
        final var timeWindows = fixedTimeWindowRepository.getAll();
        assertFalse(timeWindows.isEmpty());
    }

    @Test
    void getAllReturnsNonZeroDurationTimeWindows() {
        final var timeWindows = fixedTimeWindowRepository.getAll();
        for (TimeWindow timeWindow : timeWindows) {
            assertTrue(timeWindow.getStart().isBefore(timeWindow.getEnd()));
        }
    }


}