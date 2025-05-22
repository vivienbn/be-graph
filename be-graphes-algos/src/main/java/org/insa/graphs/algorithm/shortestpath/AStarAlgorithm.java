package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Point;

public class AStarAlgorithm extends DijkstraAlgorithm {

    private Node destination;

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
        this.destination = data.getDestination();
    }

    @Override
    protected Label setLabel(Arc arc) {
        return  new LabelStar(arc.getDestination(), this.destination);
    }

    @Override
    protected Label setLabel(Node node, Double cout) {
        return  new LabelStar(node, this.destination, cout);
    }

    @Override
    protected Label setLabel(Node node) {
        return  new LabelStar(node, this.destination);
    }

    @Override
    protected boolean updateLabel(Label currentLabel, Label minHeapLabel, Arc arc){
        return currentLabel.getCoutCourant() < minHeapLabel.getCost() + data.getCost(arc) + Point.distance(arc.getDestination().getPoint(), this.destination.getPoint());
    }
}
