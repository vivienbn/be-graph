package org.insa.graphs.algorithm.utils;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;

import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Path;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.BinaryPathReader;
import org.insa.graphs.model.io.GraphReader;
import org.insa.graphs.model.io.PathReader;


import java.awt.BorderLayout;
import java.awt.Dimension;


public class ShortestPathTest {
    

        public static void main(String[] args) throws Exception { 

        // visit these directory to see the list of available files on commetud.
        final String mapName =
                "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/insa.mapgr";
        final String pathName =
                "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Paths/path_fr31insa_rangueil_r2.path";

        final Graph graph;
        final Path path;

        // create a graph reader
        try (final GraphReader reader = new BinaryGraphReader(new DataInputStream(
                new BufferedInputStream(new FileInputStream(mapName))))) {

            // TODO: read the graph
            graph = reader.read();
        }


        // TODO: create a path reader
        try (final PathReader pathReader = new BinaryPathReader(new DataInputStream(new BufferedInputStream(new FileInputStream(pathName))))) {

            // TODO: read the path
            path = pathReader.readPath(graph);
        }

        // TODO: draw the path on the drawing
        path.getDestination();
    }
}
