package org.insa.graphs.algorithm.utils;
import static org.junit.Assert.assertEquals;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.insa.graphs.algorithm.ArcInspector;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.shortestpath.BellmanFordAlgorithm;
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

// V1 de test, exhaustive et descriptive
public class ShortestPathTest {

    private enum FileType {
        PATH,
        GRAPH
    }

    private static final String TYPE_PATH_FILE_PATH = "../be-graphes-data-for-tests/shortest-path/";
    private static final String TYPE_PATH_EXTENSION = ".path";
    private static final String TYPE_GRAPH_FILE_PATH = "../be-graphes-data-for-tests/map/";
    private static final String TYPE_GRAPH_EXTENSION = ".mapgr";
    private static final String [] PATHS_NAME = {"BF_shortest_all_roads", "BF_fastest_all_roads", "BF_shortest_only_cars", "BF_fastest_only_pedestrians"};

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

        File currentDir = new File(".");
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


    //Tests for the dijkstra algorithm
    //------------------------------------------------------------------------tests by length-----------------------------------------------------------------
    @Test
    public void testDijkstraShortestPathallroads() {
        Node dest = paths.get(0).getDestination();
        Node origin = paths.get(0).getOrigin();
        ShortestPathData data = new ShortestPathData(this.graph, origin, dest, ArcInspectorFactory.getAllFilters().get(0));
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(data);
        assertEquals(dijkstra.run().getPath().getLength(), paths.get(0).getLength(), 0.0001);
    }

    @Test 
    public void testDijkstraShortestOnlyCars(){
        Node dest = paths.get(2).getDestination();
        Node origin = paths.get(2).getOrigin();
        ShortestPathData data = new ShortestPathData(this.graph, origin, dest, ArcInspectorFactory.getAllFilters().get(1));
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(data);
        Path path = dijkstra.run().getPath();
        assertEquals(path.getLength(), paths.get(2).getLength(), 0.0001);        
    }

    @Test
    public void testDijkstraImpossiblePath() { //here we chosed on purpose 2 points that are not connected (16280, 14983 which are on different islands)
        Node dest = graph.getNodes().get(14983);
        Node origin = graph.getNodes().get(16278);
        ShortestPathData data = new ShortestPathData(this.graph, origin, dest, ArcInspectorFactory.getAllFilters().get(0));
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(data);
        assertEquals(dijkstra.run().getStatus(), Status.INFEASIBLE);
    }

    //------------------------------------------------------------------------tests by time-------------------------------------------------------------------
    @Test
    public void testDijkstraFastestallroads() {
        Node dest = paths.get(1).getDestination();
        Node origin = paths.get(1).getOrigin();
        ShortestPathData data = new ShortestPathData(this.graph, origin, dest, ArcInspectorFactory.getAllFilters().get(3));
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(data);
        BellmanFordAlgorithm bellmanFord = new BellmanFordAlgorithm(data);
        Path path = dijkstra.run().getPath();
        Path pathBF = bellmanFord.run().getPath();
        assertEquals(path.getMinimumTravelTime(), pathBF.getMinimumTravelTime(), 0.0001);
    }

    @Test
    public void testDijkstraFastestOnlyPedestrians() {
        Node dest = paths.get(3).getDestination();
        Node origin = paths.get(3).getOrigin();
        ShortestPathData data = new ShortestPathData(this.graph, origin, dest, ArcInspectorFactory.getAllFilters().get(3));
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(data);
        Path path = dijkstra.run().getPath();
        assertEquals(path.getMinimumTravelTime(), paths.get(3).getMinimumTravelTime(), 0.0001);
    }
    
    @Test
    public void testDijkstraFastestOriginEqualsDestination() { 
        Node dest = graph.getNodes().get(14301);
        Node origin = graph.getNodes().get(14301);
        ShortestPathData data = new ShortestPathData(this.graph, origin, dest, ArcInspectorFactory.getAllFilters().get(0));
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(data);
        Path path = dijkstra.run().getPath();
        assertEquals(path.getLength(), 0, 0.0001);
    }

    //----------------------------------------------------Generic Test------------------------------------------------------------------
    @Test
    public void testShortestPathAllVehicules(){
        Node origin = paths.get(1).getOrigin();
        Node dest = paths.get(1).getDestination();
        for(ArcInspector inspector:ArcInspectorFactory.getAllFilters()){
            ShortestPathData data = new ShortestPathData(this.graph, origin, dest, inspector);
            BellmanFordAlgorithm bellmanFord = new BellmanFordAlgorithm(data);
            DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(data);
            Path dijkstraPath = dijkstra.run().getPath();
            Path bellmanFordPath = bellmanFord.run().getPath();
            assertEquals(bellmanFordPath.getLength(), dijkstraPath.getLength(), 0.0001);
            assertEquals(bellmanFordPath.getMinimumTravelTime(), dijkstraPath.getMinimumTravelTime(), 0.0001);
        }
    }
}
