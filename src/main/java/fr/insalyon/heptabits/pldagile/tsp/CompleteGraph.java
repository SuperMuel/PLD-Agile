package fr.insalyon.heptabits.pldagile.tsp;

import fr.insalyon.heptabits.pldagile.model.Intersection;
import fr.insalyon.heptabits.pldagile.model.Map;
import fr.insalyon.heptabits.pldagile.model.Segment;

import java.util.HashMap;
import java.util.List;
import java.util.*;

public class CompleteGraph implements Graph {
	private static final int MAX_COST = 40;
	private static final int MIN_COST = 10;
	int nbVertices;
	double[][] cost;

	private java.util.Map<Integer, Long> intersectionsMap;

	private static final double COURIER_SPEED = 15000.0; // in m/h
	
	/**
	 * Create a complete directed graph such that each edge has a weight within [MIN_COST,MAX_COST]
	 * @param intersections
	 * @map
	 */
	public CompleteGraph(List<Intersection> intersections, Map map){
		this.nbVertices = nbVertices;
		cost = new double[intersections.size()][intersections.size()];
		intersectionsMap = new HashMap<>();
		int iterator = 0;

		for(Intersection m : intersections){
			intersectionsMap.put(iterator, m.getId());
			iterator++;
		}

		for (java.util.Map.Entry i :intersectionsMap.entrySet()){
			for(java.util.Map.Entry j: intersectionsMap.entrySet()){
				if(i.getKey() == j.getKey()) cost[(int)i.getKey()][(int)j.getKey()] = -1;
				else {
					Segment chosenSegment = map.getSegmentByOriginAndDestination((long)i.getValue(), (long)j.getValue());
					if(chosenSegment != null){
						cost[(int)i.getKey()][(int)j.getKey()] = chosenSegment.getLength() / COURIER_SPEED;
					} else {
						cost[(int)i.getKey()][(int)j.getKey()] = -1;
					}
				}
			}
		}
	}

	@Override
	public int getNbVertices() {
		return nbVertices;
	}

	@Override
	public double getCost(int i, int j) {
		if (i<0 || i>=nbVertices || j<0 || j>=nbVertices)
			return -1;
		return cost[i][j];
	}

	@Override
	public boolean isArc(int i, int j) {
		if (i<0 || i>=nbVertices || j<0 || j>=nbVertices)
			return false;
		return i != j;
	}

}
