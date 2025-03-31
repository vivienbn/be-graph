package org.insa.graphes.model;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;

import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.RoadInformation;
import org.insa.graphs.model.RoadInformation.RoadType;
import org.junit.Before;
import org.junit.Test;

public class NodeTest {

    // List of nodes
    private Node[] nodes;

    @Before
    public void initAll() throws IOException {

        // Create nodes
        nodes = new Node[6];
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

    }

    /**
     * @return The first arc between from a to b, or null.
     */
    private Arc getFirstArcBetween(Node a, Node b) {
        for (Arc arc : a.getSuccessors()) {
            if (arc.getDestination().equals(b)) {
                return arc;
            }
        }
        return null;
    }

    @Test
    public void testGetNumberOfSuccessors() {
        final int[] expNbSucc = { 4, 2, 5, 2, 1, 0 };
        assertEquals(expNbSucc.length, nodes.length);
        for (int i = 0; i < expNbSucc.length; ++i) {
            assertEquals(expNbSucc[i], nodes[i].getNumberOfSuccessors());
        }
    }

    @Test
    public void testHasSuccessors() {
        final int[] expNbSucc = { 4, 2, 5, 2, 1, 0 };
        assertEquals(expNbSucc.length, nodes.length);
        for (int i = 0; i < expNbSucc.length; ++i) {
            assertEquals(expNbSucc[i] != 0, nodes[i].hasSuccessors());
        }
    }

    @Test
    public void testLinkNodes() {
        assertEquals(getFirstArcBetween(nodes[0], nodes[1]).getRoadInformation(),
                getFirstArcBetween(nodes[1], nodes[0]).getRoadInformation());
    }

}
