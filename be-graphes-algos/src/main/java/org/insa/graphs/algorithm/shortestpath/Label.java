package org.insa.graphs.algorithm.shortestpath;

import java.util.Optional;

import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;

public class Label implements Comparable<Label>{

    private Node sommetCourant;
    private boolean marque;
    private double coutCourant;
    private Optional<Node> pere;

   public Label(Node sommetCourant){
    this.sommetCourant = sommetCourant;
    this.marque = false;
    this.coutCourant= Double.POSITIVE_INFINITY;
    this.pere = Optional.empty();
   }

   public Label(Node sommetCourant, double cout){
    this.sommetCourant = sommetCourant;
    this.marque = false;
    this.coutCourant= cout;
    this.pere = Optional.empty();
   }


   public double getCost(){
    return this.getCoutCourant();
   }

    public Node getSommetCourant(){
        return this.sommetCourant;
    }

    public boolean getMarque(){
        return this.marque;
    }

    public double getCoutCourant(){
        return this.coutCourant;
    }

    public Node getPere(){
        if(this.pere.isPresent()){
            return this.pere.get();
        } else {
            return null;
        }
    }

    public void setPere(Node pere){
        this.pere = Optional.of(pere);
    }

    public void setCoutCourant(double nouveauCout){
        this.coutCourant = nouveauCout;
    }

    public void marqueLabel(){
        this.marque = true;
    }

    @Override
    public int compareTo(Label other) {
        return Double.compare(this.getCost(), other.getCost());
    }

}


