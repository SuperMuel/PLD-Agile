package fr.insalyon.heptabits.pldagile.service;

import fr.insalyon.heptabits.pldagile.model.DeliveryRequest;
import fr.insalyon.heptabits.pldagile.model.Intersection;
import fr.insalyon.heptabits.pldagile.model.Map;
import fr.insalyon.heptabits.pldagile.model.RoadMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.lang.Double.MAX_VALUE;

public class RoadMapOptimizerImpl implements RoadMapOptimizer {


    public List<Intersection> djikstra (Map map, List<Intersection> intersectionsDestination, Intersection depart){
        // depart doit être contenu dans intersectionsDestination

        double[][] adjacencyMatrix = map.getAdjacencyMatrix();

        //on stocke pour chaque destination sa distance des autres destinations
        double [][] distanceDestination = new double[intersectionsDestination.size()][intersectionsDestination.size()];
        //on stocke également une manière de retrouver le chemin choisi
        Intersection[][] cheminDestination = new Intersection[intersectionsDestination.size()][adjacencyMatrix.length];
        for(int i = 0; i<intersectionsDestination.size(); i++){
            Intersection departTemp = intersectionsDestination.get(i);

            List<Intersection> intersectionsBlanches = new ArrayList<>(map.getIntersections().values().stream().toList());
            List<Intersection> intersectionsGrises = new ArrayList<>();
            List<Intersection> intersectionsNoires = new ArrayList<>();

            double[] distance = new double[intersectionsBlanches.size()];
            Intersection[] parent = new Intersection[intersectionsBlanches.size()];
            for(int j = 0; j< distance.length; j++){
                distance[j] = MAX_VALUE;
                parent[j] = null;
            }
            intersectionsGrises.add(departTemp);
            distance[map.getKeyIntersection(departTemp)] = 0;

            while(!intersectionsGrises.isEmpty() || !reachEachDestination(intersectionsDestination, intersectionsBlanches)){

                //trouver l'intersection tel que la distance est minimum
                double min = MAX_VALUE;
                int indiceIntersection = 0;
                for(int j = 0; j<distance.length ; j++){
                    if(min>distance[j] && intersectionsGrises.contains(map.getIntersectionByKey(j))){
                        min = distance[j];
                        indiceIntersection = j;
                    }
                }

                for(int j = 0; j<adjacencyMatrix.length; j++){
                    if(adjacencyMatrix[indiceIntersection][j] != -1 ){
                        if(intersectionsBlanches.contains(map.getIntersectionByKey(j)) || intersectionsGrises.contains(map.getIntersectionByKey(j))){
                            if(distance[j] > distance[indiceIntersection]+adjacencyMatrix[indiceIntersection][j]){
                                distance[j] = distance[indiceIntersection]+adjacencyMatrix[indiceIntersection][j];
                                parent[j] = map.getIntersectionByKey(indiceIntersection);
                            }
                            if(intersectionsBlanches.contains(map.getIntersectionByKey(j))){
                               intersectionsGrises.add(map.getIntersectionByKey(j));
                               intersectionsBlanches.remove(map.getIntersectionByKey(j));
                            }
                        }
                    }
                }
                intersectionsGrises.remove(map.getIntersectionByKey(indiceIntersection));
                intersectionsNoires.add(map.getIntersectionByKey(indiceIntersection));
            }

            //on copie nos résultats dans nos tableaux résumés
            for(int j = 0; j< intersectionsDestination.size(); j++){
                distanceDestination[i][j] = distance[map.getKeyIntersection(intersectionsDestination.get(j))];
                for(int h = 0; h<parent.length; h++){
                    cheminDestination[i][h] = parent[h];
                }
            }
        }

        // On forme l'ordre de passage des destinations (avec les indices du tableau destinations)
        int indDepartTemp = getIndByKey(map, intersectionsDestination, map.getKeyIntersection(depart));
        int indProchainTemp = -1;
        List<Integer> ordreDePassageDestinations = new ArrayList<>();

        List<Integer> indicesDestination = new ArrayList<>();
        for(int i=0; i<intersectionsDestination.size(); i++){
            indicesDestination.add(i);
        }

        while(!indicesDestination.isEmpty()){
            ordreDePassageDestinations.add(indDepartTemp);
            indicesDestination.remove(indDepartTemp);
            double min = MAX_VALUE;
            for(int i=0; i< distanceDestination.length; i++){
                if(min>distanceDestination[indDepartTemp][i] && indicesDestination.contains(i)){
                    indProchainTemp = i;
                    min = distanceDestination[indDepartTemp][i];
                }
            }
            indDepartTemp = indProchainTemp;
        }

        // On forme transforme l'ordre de passage en ordre d'intersections
        Intersection[] ordreIntersections = new Intersection[intersectionsDestination.size()];
        for(int i=0; i<ordreDePassageDestinations.size(); i++){
             ordreIntersections[i] = intersectionsDestination.get(ordreDePassageDestinations.get(i));
        }

        List<Intersection> itinerary = new ArrayList<>();

        for(int i=ordreIntersections.length-1; i>0; i--){
            Intersection parentIntersection = ordreIntersections[i];
            while(parentIntersection != ordreIntersections[i-1]){
                itinerary.add(parentIntersection);
                parentIntersection = cheminDestination[ordreDePassageDestinations.get(i-1)][map.getKeyIntersection(parentIntersection)];
            }
        }
        itinerary.add(ordreIntersections[0]);

        itinerary.reversed();

        return itinerary;
    }

    public int getIndByKey(Map map, List<Intersection> destination, int key){
        for(int i=0; i<destination.size(); i++){
            if(map.getKeyIntersection(destination.get(i))==key) {
                return i;
            }
        }
        return -1;
    }

    public boolean reachEachDestination(List<Intersection> destinations, List<Intersection> intersectionsBlanches){
        for(int i=0; i<destinations.size(); i++){
            if(intersectionsBlanches.contains(destinations.get(i))){
                return false;
            }
        }
        return true;
    }

    @Override
    public RoadMap optimize(Collection<DeliveryRequest> requests, Map map) throws ImpossibleRoadMapException {



        return null;
    }
}
