package org.insa.graphs.algorithm.shortestpath;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;
import java.util.ArrayList;
import java.util.List;

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
        //int indexSommetCourant;
        BinaryHeap<Label> labelsHeap = new BinaryHeap<Label>();
        ArrayList<Label> labelsList = new ArrayList<Label>();

        for(Node n:data.getGraph().getNodes()){
            if(n.equals(data.getOrigin())){
                Label label = new Label(n,0);
                labelsHeap.insert(label);
                labelsList.add(label);
            } else {
                Label label = new Label(n);
                labelsHeap.insert(label);
                labelsList.add(label);
            }
        }


        // variable that will contain the solution of the shortest path problem
        ShortestPathSolution solution = null;

        while(!labelsHeap.isEmpty()){
            Label originLabel = labelsHeap.findMin();
            Node originNode = originLabel.getSommetCourant();
            for(Label label:labelsList){
                for(Arc arc:originNode.getSuccessors()){
                    if(arc.getDestination().equals(label.getSommetCourant())){
                        label.setCoutCourant(Math.min(label.getCoutCourant(), originLabel.getCoutCourant() + arc.getLength()));
                        labelsHeap.remove(label);
                        labelsHeap.insert(label);
                    }
                }
            }

            labelsHeap.deleteMin();
        
        }




        // TODO: implement the Dijkstra algorithm

        // when the algorithm terminates, return the solution that has been found
        return solution;
    }

}
