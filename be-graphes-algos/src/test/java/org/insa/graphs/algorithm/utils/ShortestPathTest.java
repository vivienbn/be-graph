package org.insa.graphs.algorithm.utils;
import static org.junit.Assert.assertEquals;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.shortestpath.DijkstraAlgorithm;
import org.insa.graphs.algorithm.shortestpath.ShortestPathData;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.BinaryPathReader;
import org.insa.graphs.model.io.GraphReader;
import org.insa.graphs.model.io.PathReader;
import org.junit.Before;
import org.junit.Test;


public class ShortestPathTest {

    private enum FileType {
        PATH,
        GRAPH
    }

    private static final String TYPE_PATH_FILE_PATH = "../be-graphes-data-for-tests/shortest-path/";
    private static final String TYPE_PATH_EXTENSION = ".path";
    private static final String TYPE_GRAPH_FILE_PATH = "../be-graphes-data-for-tests/map/";
    private static final String TYPE_GRAPH_EXTENSION = ".mapgr";
    private static final String [] PATHS_NAME = {"BF_shortest_all_roads", "BF_fastest_all_roads"};

    private static String getFileName(String name, FileType type) throws IOException{
        File mapFile = null;
        String relativPath = "";
        switch(type){
            case PATH:
                relativPath = ShortestPathTest.TYPE_PATH_FILE_PATH + name + ShortestPathTest.TYPE_PATH_EXTENSION;
                break;
            case GRAPH:
                relativPath = ShortestPathTest.TYPE_GRAPH_FILE_PATH + name + ShortestPathTest.TYPE_GRAPH_EXTENSION;
                break;
            default:
                break;
        }

        File currentDir = new File("."); // = ./ (répertoire de travail)
        String basePath = currentDir.getCanonicalPath();
        mapFile = new File(basePath, relativPath);
        return mapFile.getCanonicalPath();
    }

   // List of path
    private ArrayList<Path> paths;
    private Graph graph;

    @Before
    public void initAll() throws IOException {
        this.paths = new ArrayList<>();
        final String guadeloupeMapName = ShortestPathTest.getFileName("guadeloupe", FileType.GRAPH);
        // create a graph reader
        try (final GraphReader reader = new BinaryGraphReader(new DataInputStream(
                new BufferedInputStream(new FileInputStream(guadeloupeMapName))))) {

            // read the graph
            this.graph = reader.read();
        }

        for(String name:ShortestPathTest.PATHS_NAME){
            final String pathName = ShortestPathTest.getFileName(name, FileType.PATH);

            // create a path reader
            try (final PathReader pathReader = new BinaryPathReader(new DataInputStream(new BufferedInputStream(new FileInputStream(pathName))))) {
                // read the path
                paths.add(pathReader.readPath(graph));
            }

        }
    }
    
    @Test
    public void testDijkstraShortest() {
        Node dest = paths.get(0).getDestination();
        Node origin = paths.get(0).getOrigin();
        ShortestPathData data = new ShortestPathData(this.graph, origin, dest, ArcInspectorFactory.getAllFilters().get(0));
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(data);
        assertEquals(dijkstra.run().getPath().getLength(), paths.get(0).getLength(), 0.0001);
    }

    @Test
    public void testDijkstraFastest() {
        Node dest = paths.get(1).getDestination();
        Node origin = paths.get(1).getOrigin();
        ShortestPathData data = new ShortestPathData(this.graph, origin, dest, ArcInspectorFactory.getAllFilters().get(2));
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(data);
        assertEquals(dijkstra.run().getPath().getMinimumTravelTime(), paths.get(0).getMinimumTravelTime(), 0.0001);
    }

    @Test
}
