package org.insa.graphs.gui.simple;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.shortestpath.BellmanFordAlgorithm;
import org.insa.graphs.algorithm.shortestpath.DijkstraAlgorithm;
import org.insa.graphs.algorithm.shortestpath.ShortestPathData;
import org.insa.graphs.algorithm.shortestpath.ShortestPathSolution;
import org.insa.graphs.gui.drawing.Drawing;
import org.insa.graphs.gui.drawing.components.BasicDrawing;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.BinaryPathReader;
import org.insa.graphs.model.io.GraphReader;
import org.insa.graphs.model.io.PathReader;

public class Launch {

    /**
     * Create a new Drawing inside a JFrame an return it.
     *
     * @return The created drawing.
     * @throws Exception if something wrong happens when creating the graph.
     */
    public static Drawing createDrawing() throws Exception {
        BasicDrawing basicDrawing = new BasicDrawing();
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("BE Graphes - Launch");
                frame.setLayout(new BorderLayout());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
                frame.setSize(new Dimension(800, 600));
                frame.setContentPane(basicDrawing);
                frame.validate();
            }
        });
        return basicDrawing;
    }

    public static void main(String[] args) throws Exception { 

        // visit these directory to see the list of available files on commetud.
        final String mapName =
                "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/insa.mapgr";
        final String mapName2 =
                "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/haute-garonne.mapgr";
        final String pathName =
                "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Paths/path_fr31insa_rangueil_r2.path";
        final String pathName2 = "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Paths/path_fr31_insa_aeroport_length.path";

        final Graph graph;
        final Path path;

        // create a graph reader
        try (final GraphReader reader = new BinaryGraphReader(new DataInputStream(
                new BufferedInputStream(new FileInputStream(mapName))))) {

            // TODO: read the graph
            graph = reader.read();
        }

        // create the drawing
        final Drawing drawing = createDrawing();

        // TODO: draw the graph on the drawing
        drawing.drawGraph(graph);

        // TODO: create a path reader
        try (final PathReader pathReader = new BinaryPathReader(new DataInputStream(new BufferedInputStream(new FileInputStream(pathName))))) {

            // TODO: read the path
            path = pathReader.readPath(graph);
        }

        // TODO: draw the path on the drawing
        // drawing.drawPath(path);
        Node dest = path.getDestination();
        Node origin = path.getOrigin();
        ShortestPathData data = new ShortestPathData(graph, origin, dest, ArcInspectorFactory.getAllFilters().get(2));
        // ShortestPathSolution solution =  new ShortestPathSolution(data, Status.OPTIMAL, path);



        DijkstraAlgorithm dij = new DijkstraAlgorithm(data);
        BellmanFordAlgorithm bel = new BellmanFordAlgorithm(data);
        drawing.drawPath(path, Color.blue);
        drawing.drawPath(dij.run().getPath(), Color.red);
        System.out.println("Chemin donné de longueur : " + path.getLength() + "Chemin dijkstra de longueur : " + dij.run().getPath().getLength());
    }

    private boolean testDijkstraShortest(){
        return true;
    }

}
