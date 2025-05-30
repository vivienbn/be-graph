package org.insa.graphs.algorithm.utils;
import static org.junit.Assert.assertEquals;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.AbstractInputData.Mode;
import org.insa.graphs.algorithm.AbstractSolution;
import org.insa.graphs.algorithm.ArcInspector;
import org.insa.graphs.algorithm.shortestpath.BellmanFordAlgorithm;
import org.insa.graphs.algorithm.shortestpath.ShortestPathAlgorithm;
import org.insa.graphs.algorithm.shortestpath.ShortestPathData;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.BinaryPathReader;
import org.insa.graphs.model.io.GraphReader;
import org.insa.graphs.model.io.PathReader;
import org.junit.Before;


public abstract class AbstractShortestPathAlgorithmTest {

    protected Graph graph;
    protected ArrayList<Path> paths;

    protected abstract ShortestPathAlgorithm createAlgorithm(ShortestPathData data);

    private double getPathCostByMode(Path path, Mode mode){
        if(mode == Mode.TIME){
            return path.getMinimumTravelTime();
        }
        return path.getLength();
    }


    @Before
    public void initAll() throws IOException {
        paths = new ArrayList<>();
        String mapName = getFileName("guadeloupe", FileType.GRAPH);
        try (GraphReader reader = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))))) {
            graph = reader.read();
        }

        for (String name : PATHS_NAME) {
            String pathName = getFileName(name, FileType.PATH);
            try (PathReader pathReader = new BinaryPathReader(new DataInputStream(new BufferedInputStream(new FileInputStream(pathName))))) {
                paths.add(pathReader.readPath(graph));
            }
        }
    }

    protected void testPathWithArcInspector(ArcInspector inspector) {
        Node origin = this.paths.get(1).getOrigin();
        Node dest = this.paths.get(1).getDestination();
        ShortestPathData data = new ShortestPathData(graph, origin, dest, inspector);

        Path referencePath = new BellmanFordAlgorithm(data).run().getPath();
        ShortestPathAlgorithm algo = createAlgorithm(data);
        Path result = algo.run().getPath();
        assertEquals(getPathCostByMode(referencePath, inspector.getMode()), getPathCostByMode(result, inspector.getMode()), 0.0001);;
    }

    protected void testInfeasiblePath(Node origin, Node dest) {
        ShortestPathData data = new ShortestPathData(graph, origin, dest, ArcInspectorFactory.getAllFilters().get(0));
        ShortestPathAlgorithm algo = createAlgorithm(data);
        assertEquals(AbstractSolution.Status.INFEASIBLE, algo.run().getStatus());
    }

    protected void testSameOriginDestination(Node node, ArcInspector inspector) {
        ShortestPathData data = new ShortestPathData(graph, node, node, inspector);
        ShortestPathAlgorithm algo = createAlgorithm(data);
        assertEquals(0.0, getPathCostByMode(algo.run().getPath(), inspector.getMode()), 0.0001);
    }

    // Utils
    protected enum FileType { PATH, GRAPH }

    protected static final String[] PATHS_NAME = {
        "BF_shortest_all_roads",
        "BF_fastest_all_roads",
        "BF_shortest_only_cars",
        "BF_fastest_only_pedestrians"
    };

    private static final String TYPE_PATH_FILE_PATH = "../be-graphes-data-for-tests/shortest-path/";
    private static final String TYPE_GRAPH_FILE_PATH = "../be-graphes-data-for-tests/map/";
    private static final String TYPE_PATH_EXTENSION = ".path";
    private static final String TYPE_GRAPH_EXTENSION = ".mapgr";

    protected static String getFileName(String name, FileType type) throws IOException {
        String relativePath = switch (type) {
            case PATH -> TYPE_PATH_FILE_PATH + name + TYPE_PATH_EXTENSION;
            case GRAPH -> TYPE_GRAPH_FILE_PATH + name + TYPE_GRAPH_EXTENSION;
        };
        File currentDir = new File(".");
        return new File(currentDir.getCanonicalPath(), relativePath).getCanonicalPath();
    }
}
