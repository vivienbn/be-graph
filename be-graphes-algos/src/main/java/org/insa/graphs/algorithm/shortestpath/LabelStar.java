package org.insa.graphs.algorithm.shortestpath;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Point;

public class LabelStar extends Label {

    private double coutHeuristique;
    private double distHeuristique;

    public LabelStar(Node sommetCourant, Node dest) {
        super(sommetCourant);
        this.distHeuristique = Point.distance(sommetCourant.getPoint(), dest.getPoint());
        this.setCoutCourant(0);
    }

    public LabelStar(Node sommetCourant, Node dest, double cout) {
        super(sommetCourant, cout);
        this.distHeuristique = Point.distance(sommetCourant.getPoint(), dest.getPoint());
        this.setCoutCourant(cout);
    }

    @Override
    public void setCoutCourant(double cout) {
        super.setCoutCourant(cout);
        this.coutHeuristique =  this.distHeuristique + cout;
    }

    @Override
    public double getCoutCourant() {
        return this.coutHeuristique;
    }
}
