package org.insa.graphs.gui.drawing.overlays;

import org.insa.graphs.model.Point;

public interface MarkerOverlay extends Overlay {

    /**
     * @return The current position of this marker.
     */
    public Point getPoint();

    /**
     * Move this marker to the specified location.
     *
     * @param point New position for the marker.
     */
    public void moveTo(Point point);

}
