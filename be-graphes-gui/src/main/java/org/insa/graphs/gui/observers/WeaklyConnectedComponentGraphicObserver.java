package org.insa.graphs.gui.observers;

import java.awt.Color;
import java.util.ArrayList;

import org.insa.graphs.algorithm.weakconnectivity.WeaklyConnectedComponentObserver;
import org.insa.graphs.gui.drawing.Drawing;
import org.insa.graphs.gui.drawing.overlays.PointSetOverlay;
import org.insa.graphs.model.Node;

public class WeaklyConnectedComponentGraphicObserver
        implements WeaklyConnectedComponentObserver {

    private static final Color[] COLORS =
            { Color.BLUE, Color.ORANGE, Color.GREEN, Color.YELLOW, Color.RED };

    // Drawing + Graph drawing
    private PointSetOverlay grPoints;

    // Current index color
    private int cindex = 0;

    public WeaklyConnectedComponentGraphicObserver(Drawing drawing) {
        this.grPoints = drawing.createPointSetOverlay();
        this.grPoints.setWidth(1);
    }

    @Override
    public void notifyStartComponent(Node curNode) {
        this.grPoints.setColor(COLORS[cindex]);
    }

    @Override
    public void notifyNewNodeInComponent(Node node) {
        this.grPoints.addPoint(node.getPoint());
    }

    @Override
    public void notifyEndComponent(ArrayList<Node> nodes) {
        cindex = (cindex + 1) % COLORS.length;
    }

}
