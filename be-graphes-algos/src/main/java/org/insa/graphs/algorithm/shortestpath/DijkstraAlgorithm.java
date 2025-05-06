package org.insa.graphs.algorithm.shortestpath;
import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;


public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
    protected ShortestPathSolution doRun() {

        // retrieve data from the input problem (getInputData() is inherited from the
        // parent class ShortestPathAlgorithm)
        final ShortestPathData data = getInputData();
        int nbNodes = data.getGraph().size();
        Graph graph = data.getGraph();


        Label destination = new Label(data.getDestination());
        Label originLabel = new Label(data.getOrigin(), 0);

        //int indexSommetCourant;
        BinaryHeap<Label> labelsHeap = new BinaryHeap<Label>();
        Label [] labelsList = new Label[nbNodes];
        ArrayList<Label> labelSolution = new ArrayList<Label>();

        for(Node n:data.getGraph().getNodes()){
                Label label = new Label(n);
                labelsList[n.getId()] = label;
        }
        labelsHeap.insert(originLabel);
        labelsList[originLabel.getSommetCourant().getId()] = originLabel;
        labelsList[destination.getSommetCourant().getId()] = destination;

        // variable that will contain the solution of the shortest path problem
        ShortestPathSolution solution = null;
        Label sommetTas = originLabel;

        while(!labelsHeap.isEmpty() && !destination.getMarque()){
            sommetTas = labelsHeap.deleteMin();
            Node originNode = sommetTas.getSommetCourant();
            sommetTas.marqueLabel();
            notifyNodeReached(originNode);

            for(Arc arc:originNode.getSuccessors()){
                Label current = labelsList[arc.getDestination().getId()];

                if(!current.getMarque() && data.isAllowed(arc)){
                    current.setCoutCourant(Math.min(current.getCoutCourant(), sommetTas.getCoutCourant() + arc.getLength()));

                    if(!(current.getPere() == null)){
                        labelsHeap.remove(current);
                    }

                    labelsHeap.insert(current);
                    current.setPere(originNode);
                }
            }
            labelSolution.add(sommetTas);
        }


        ArrayList<Node> shortestPath = new ArrayList<>();
        int nbSommets = labelSolution.size();
        Node currentNode = labelSolution.get(nbSommets - 1).getSommetCourant();
        Node predecessor = labelSolution.get(nbSommets - 1).getPere();
        shortestPath.add(currentNode);
        notifyDestinationReached(currentNode);
        for(int i=nbSommets - 1;i>0;i--){
            if(labelSolution.get(i).getSommetCourant().equals(predecessor)){
                currentNode = labelSolution.get(i).getSommetCourant();
                shortestPath.add(currentNode);
                if(!currentNode.equals(originLabel.getSommetCourant())){
                    predecessor = labelSolution.get(i).getPere();
                }
            }
        }

        Collections.reverse(shortestPath);
        Path bestPath = Path.createShortestPathFromNodes(graph, shortestPath);
        solution = new ShortestPathSolution(data, Status.OPTIMAL, bestPath);
        // when the algorithm terminates, return the solution that has been found
        return solution;
    }

}
