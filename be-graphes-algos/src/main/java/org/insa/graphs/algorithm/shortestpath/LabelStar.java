package org.insa.graphs.algorithm.shortestpath;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Point;

public class LabelStar extends Label {

    private double distHeuristique;

    public LabelStar(Node sommetCourant, Node dest) {
        super(sommetCourant);
        this.distHeuristique = Point.distance(sommetCourant.getPoint(), dest.getPoint());
    }

    public LabelStar(Node sommetCourant, Node dest, double cout) {
        super(sommetCourant, cout);
        this.distHeuristique = Point.distance(sommetCourant.getPoint(), dest.getPoint());
    }

    public double getDistHeuristique(){
        return this.distHeuristique;
    }

    @Override
    public double getCost() {
        return this.distHeuristique + this.getCoutCourant();
    }

    @Override
    public int compareTo(Label other) {
        int result = super.compareTo(other);

        // Gestion de l'égalité
        if (result == 0 && other instanceof LabelStar) {
            LabelStar otherStar = (LabelStar) other;
            return Double.compare(this.getDistHeuristique(), otherStar.getDistHeuristique());
        }

        return result;
    }
}
