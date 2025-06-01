package org.insa.graphs.algorithm.utils;
import org.insa.graphs.algorithm.ArcInspector;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.shortestpath.*;
import org.junit.Test;

public class AStarTest extends AbstractShortestPathAlgorithmTest {

    @Override
    protected ShortestPathAlgorithm createAlgorithm(ShortestPathData data) {
        return new AStarAlgorithm(data);
    }

    @Test
    public void testShortestAllRoads() {
        testPathWithArcInspector(ArcInspectorFactory.getAllFilters().get(0));
    }

    @Test
    public void testShortestOnlyCar() {
        testPathWithArcInspector(ArcInspectorFactory.getAllFilters().get(1));
    }


    @Test
    public void testImpossiblePath() {
        testInfeasiblePath(graph.getNodes().get(16278), graph.getNodes().get(14983));
    }

    @Test
    public void testSameNodeAllInspectors() {
        for(ArcInspector arcInspector:ArcInspectorFactory.getAllFilters()){
            testSameOriginDestination(graph.getNodes().get(14301), arcInspector);
        }
    }
}
