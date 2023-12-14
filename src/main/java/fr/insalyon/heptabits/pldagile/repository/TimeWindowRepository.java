package fr.insalyon.heptabits.pldagile.repository;

import fr.insalyon.heptabits.pldagile.model.TimeWindow;

import java.util.List;

/**
 * Repository for time windows.
 * <p>
 * This interface is used to abstract the storage of time windows.
 * For the moment, it is only used to get all time windows.
 */
public interface TimeWindowRepository {

    /**
     * Finds all time windows.
     *
     * @return a list of all time windows
     */
    List<TimeWindow> getAll();

}
