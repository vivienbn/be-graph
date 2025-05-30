package org.insa.graphs.algorithm.utils;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.shortestpath.*;
import org.junit.Test;

public class DijkstraTest extends AStarTest {

    @Override
    protected ShortestPathAlgorithm createAlgorithm(ShortestPathData data) {
        return new DijkstraAlgorithm(data);
    }

    // Ajout des tests sur la durée non présents sur AStar (algorithme non implémenté pour AStar)
    @Test
    public void testFastestAllRoads() {
        testPathWithArcInspector(ArcInspectorFactory.getAllFilters().get(2));
    }

    // Ajout des tests sur la durée non présents sur AStar (algorithme non implémenté pour AStar)
    @Test
    public void testFastestOnlyPedestrian() {
        testPathWithArcInspector(ArcInspectorFactory.getAllFilters().get(3));
    }
}
