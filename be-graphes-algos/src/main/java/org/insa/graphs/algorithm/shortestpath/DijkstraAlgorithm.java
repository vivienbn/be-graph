package org.insa.graphs.algorithm.shortestpath;
import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;

import java.util.ArrayList;
import java.util.Collections;



public class DijkstraAlgorithm extends ShortestPathAlgorithm {


    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    protected Label setLabel(Arc arc) {
        return  new Label(arc.getDestination());
    }

    protected Label setLabel(Node node, Double cout) {
        return  new Label(node, cout);
    }

    protected Label setLabel(Node node) {
        return  new Label(node);
    }

    protected boolean needUpdate(Label currentLabel, Label minHeapLabel, Arc arc){
        return currentLabel.getCoutCourant() > minHeapLabel.getCoutCourant() + data.getCost(arc);
    }
    

    @Override
    protected ShortestPathSolution doRun() {

        // retrieve data from the input problem (getInputData() is inherited from the
        // parent class ShortestPathAlgorithm)
        final ShortestPathData data = getInputData();
        Graph graph = data.getGraph();
        int nbNodes = graph.size();

        // Création des labels points de départ et arrivée
        Label destinationLabel = setLabel(data.getDestination());
        Label originLabel = setLabel(data.getOrigin(), 0.0);

        // verification de la distinction du départ et de l'arrivée
        if(destinationLabel.equals(originLabel)){
            return new ShortestPathSolution(data, Status.OPTIMAL, new Path(graph, data.getOrigin()));
        }
        
        //New DijkstraAlgo
        Arc[] predecessorArcs = new Arc[nbNodes];
        Label [] labelsList = new Label[nbNodes];
        BinaryHeap<Label> labelsHeap = new BinaryHeap<Label>();

        // Initialisation des données dans les structures
        labelsHeap.insert(originLabel);
        labelsList[originLabel.getSommetCourant().getId()] = originLabel;
        labelsList[destinationLabel.getSommetCourant().getId()] = destinationLabel;

        // Boucle d'actualisation des labels
        Label minHeapLabel = originLabel;
        while(!labelsHeap.isEmpty() && !destinationLabel.isVisited()){
            // Mise à jour du tas et du sommet courant
            minHeapLabel = labelsHeap.deleteMin();
            minHeapLabel.visit();

            // Récupération du sommet courant
            Node originNode = minHeapLabel.getSommetCourant();
            notifyNodeReached(originNode);

            // Mise à jour des sommets voisins
            for(Arc arc:originNode.getSuccessors()){
                int destinationNodeId = arc.getDestination().getId();
                // Lazy initializing
                if(labelsList[destinationNodeId] == null){
                    labelsList[destinationNodeId] = this.setLabel(arc);
                }

                // Selection du label en cours d'actualisation
                Label currentLabel = labelsList[destinationNodeId];

                if(!currentLabel.isVisited() && data.isAllowed(arc)){
                    if(this.needUpdate(currentLabel, minHeapLabel, arc)){
                        currentLabel.setCoutCourant(minHeapLabel.getCoutCourant() + data.getCost(arc));
                        predecessorArcs[destinationNodeId] = arc;
                    }

                    if(!(currentLabel.getPere() == null)){
                        labelsHeap.remove(currentLabel);
                    }
                    currentLabel.setPere(originNode);
                    labelsHeap.insert(currentLabel);
                }
            }
        }
        notifyDestinationReached(destinationLabel.getSommetCourant());

        if (predecessorArcs[data.getDestination().getId()] == null) {
                return new ShortestPathSolution(data, Status.INFEASIBLE);
        } else {
            // The destination has been found, notify the observers.
            notifyDestinationReached(data.getDestination());

            // Create the path from the array of predecessors...
            ArrayList<Arc> arcs = new ArrayList<>();
            Arc arc = predecessorArcs[data.getDestination().getId()];
            while (arc != null) {
                arcs.add(arc);
                arc = predecessorArcs[arc.getOrigin().getId()];
            }

            // Reverse the path...
            Collections.reverse(arcs);

            // Create the final solution.
            return new ShortestPathSolution(data, Status.OPTIMAL, new Path(graph, arcs));
        }
    }
}
