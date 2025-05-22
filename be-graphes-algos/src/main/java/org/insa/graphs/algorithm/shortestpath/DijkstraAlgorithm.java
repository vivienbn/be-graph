package org.insa.graphs.algorithm.shortestpath;
import org.insa.graphs.algorithm.AbstractInputData.Mode;
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

    protected boolean updateLabel(Label currentLabel, Label minHeapLabel, Arc arc){
        return currentLabel.getCoutCourant() > minHeapLabel.getCoutCourant() + data.getCost(arc);
    }
    

    @Override
    protected ShortestPathSolution doRun() {

        // retrieve data from the input problem (getInputData() is inherited from the
        // parent class ShortestPathAlgorithm)
        final ShortestPathData data = getInputData();
        Graph graph = data.getGraph();

        // Création des labels points de départ et arrivée
        Label destinationLabel = setLabel(data.getDestination());
        Label originLabel = setLabel(data.getOrigin(), 0.0);

        // variable that will contain the solution of the shortest path problem
        ShortestPathSolution solution = null;
        
        // Création de la solution
        Label [] labelsList = this.getLabelsListOrderedWithDijkstra(graph, originLabel, destinationLabel, data);
        if (labelsList[data.getDestination().getId()].getPere() == null) {
            solution = new ShortestPathSolution(data, Status.INFEASIBLE);
        } else {
            Path bestPath = getShortestPathFromLabels(labelsList, graph, originLabel, destinationLabel);
            solution = new ShortestPathSolution(data, Status.OPTIMAL, bestPath);    
        }

        // when the algorithm terminates, return the solution that has been found
        return solution;
    }

    private Label [] getLabelsListOrderedWithDijkstra(Graph graph, Label originLabel, Label destinationLabel, ShortestPathData data){
        // Init de nos structures de labels
        Label [] labelsList = new Label[graph.size()];
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
                // Lazy loading
                if(labelsList[destinationNodeId] == null){
                    labelsList[destinationNodeId] = this.setLabel(arc);
                }

                // Selection du label en cours d'actualisation
                Label currentLabel = labelsList[destinationNodeId];

                if(!currentLabel.isVisited() && data.isAllowed(arc)){
                    if(this.updateLabel(currentLabel, minHeapLabel, arc)){
                        currentLabel.setCoutCourant(minHeapLabel.getCost() + data.getCost(arc));
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
        return labelsList;
    }

    private Path getShortestPathFromLabels (Label [] labelsList, Graph graph, Label originLabel, Label destinationLabel){
        // Initialisation des structures de donnée
        ArrayList<Node> shortestPath = new ArrayList<>();

        // Création du Path en lisant les labels en partant de la destination et en remontant les predecesseurs
        Label currentLabel = destinationLabel;
        while (currentLabel != null) {
            shortestPath.add(currentLabel.getSommetCourant());
            if (currentLabel.equals(originLabel)) {
                break;
            }
            currentLabel = labelsList[currentLabel.getPere().getId()];
        }
        Collections.reverse(shortestPath);
        if(data.getMode().equals(Mode.LENGTH)){
            return Path.createShortestPathFromNodes(graph, shortestPath);
        } else {
            return Path.createFastestPathFromNodes(graph, shortestPath);
        }

    }
}
