package fr.insalyon.heptabits.pldagile.repository;

import fr.insalyon.heptabits.pldagile.model.TimeWindow;

import java.util.List;

public interface TimeWindowRepository {
    List<TimeWindow> getAll();

}
