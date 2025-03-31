package org.insa.graphes.model;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.RoadInformation;
import org.insa.graphs.model.RoadInformation.RoadType;
import org.junit.BeforeClass;
import org.junit.Test;

public class GraphTest {

    // Small graph use for tests
    private static Graph graph;

    // List of nodes
    private static Node[] nodes;

    @BeforeClass
    public static void initAll() throws IOException {

        // Create nodes
        nodes = new Node[5];
        for (int i = 0; i < nodes.length; ++i) {
            nodes[i] = new Node(i, null);
        }

        Node.linkNodes(nodes[0], nodes[1], 0,
                new RoadInformation(RoadType.UNCLASSIFIED, null, false, 1, null),
                new ArrayList<>());
        Node.linkNodes(nodes[0], nodes[2], 0,
                new RoadInformation(RoadType.UNCLASSIFIED, null, false, 1, null),
                new ArrayList<>());
        Node.linkNodes(nodes[0], nodes[4], 0,
                new RoadInformation(RoadType.UNCLASSIFIED, null, true, 1, null),
                new ArrayList<>());
        Node.linkNodes(nodes[1], nodes[2], 0,
                new RoadInformation(RoadType.UNCLASSIFIED, null, false, 1, null),
                new ArrayList<>());
        Node.linkNodes(nodes[2], nodes[3], 0,
                new RoadInformation(RoadType.UNCLASSIFIED, null, true, 1, null),
                new ArrayList<>());
        Node.linkNodes(nodes[2], nodes[3], 0,
                new RoadInformation(RoadType.UNCLASSIFIED, null, true, 1, null),
                new ArrayList<>());
        Node.linkNodes(nodes[2], nodes[3], 0,
                new RoadInformation(RoadType.UNCLASSIFIED, null, true, 1, null),
                new ArrayList<>());
        Node.linkNodes(nodes[3], nodes[0], 0,
                new RoadInformation(RoadType.UNCLASSIFIED, null, false, 1, null),
                new ArrayList<>());
        Node.linkNodes(nodes[3], nodes[4], 0,
                new RoadInformation(RoadType.UNCLASSIFIED, null, true, 1, null),
                new ArrayList<>());
        Node.linkNodes(nodes[4], nodes[0], 0,
                new RoadInformation(RoadType.UNCLASSIFIED, null, true, 1, null),
                new ArrayList<>());

        graph = new Graph("ID", "", Arrays.asList(nodes), null);

    }

    /**
     * @return List of arcs between a and b.
     */
    private List<Arc> getArcsBetween(Node a, Node b) {
        List<Arc> arcs = new ArrayList<>();
        for (Arc arc : a.getSuccessors()) {
            if (arc.getDestination().equals(b)) {
                arcs.add(arc);
            }
        }
        return arcs;
    }

    @Test
    public void testTranspose() {
        Graph transpose = graph.transpose();

        // Basic asserts...
        assertEquals("R/" + graph.getMapId(), transpose.getMapId());
        assertEquals(graph.size(), transpose.size());

        final int expNbSucc[] = { 4, 2, 2, 4, 2 };
        for (int i = 0; i < expNbSucc.length; ++i) {
            assertEquals(expNbSucc[i], transpose.get(i).getNumberOfSuccessors());
        }

        assertEquals(1, getArcsBetween(transpose.get(0), transpose.get(1)).size());
        assertEquals(1, getArcsBetween(transpose.get(0), transpose.get(2)).size());
        assertEquals(1, getArcsBetween(transpose.get(0), transpose.get(3)).size());
        assertEquals(1, getArcsBetween(transpose.get(0), transpose.get(4)).size());
        assertEquals(1, getArcsBetween(transpose.get(1), transpose.get(0)).size());
        assertEquals(1, getArcsBetween(transpose.get(1), transpose.get(2)).size());
        assertEquals(0, getArcsBetween(transpose.get(1), transpose.get(3)).size());
        assertEquals(0, getArcsBetween(transpose.get(1), transpose.get(4)).size());
        assertEquals(1, getArcsBetween(transpose.get(2), transpose.get(0)).size());
        assertEquals(1, getArcsBetween(transpose.get(2), transpose.get(1)).size());
        assertEquals(0, getArcsBetween(transpose.get(2), transpose.get(3)).size());
        assertEquals(0, getArcsBetween(transpose.get(2), transpose.get(4)).size());
        assertEquals(1, getArcsBetween(transpose.get(3), transpose.get(0)).size());
        assertEquals(0, getArcsBetween(transpose.get(3), transpose.get(1)).size());
        assertEquals(3, getArcsBetween(transpose.get(3), transpose.get(2)).size());
        assertEquals(0, getArcsBetween(transpose.get(3), transpose.get(4)).size());
        assertEquals(1, getArcsBetween(transpose.get(4), transpose.get(0)).size());
        assertEquals(0, getArcsBetween(transpose.get(4), transpose.get(1)).size());
        assertEquals(0, getArcsBetween(transpose.get(4), transpose.get(2)).size());
        assertEquals(1, getArcsBetween(transpose.get(4), transpose.get(3)).size());

    }
}
