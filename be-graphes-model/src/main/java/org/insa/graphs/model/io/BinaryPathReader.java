package org.insa.graphs.model.io;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;

/**
 * Implementation of {@link PathReader} to read paths in binary format.
 */
public class BinaryPathReader extends BinaryReader implements PathReader {

    // Map version and magic number targeted for this reader.
    protected static final int VERSION = 1;
    protected static final int MAGIC_NUMBER = 0xdecafe;

    /**
     * Create a new BinaryPathReader that read from the given input stream.
     *
     * @param dis Input stream to read from.
     */
    public BinaryPathReader(DataInputStream dis) {
        super(MAGIC_NUMBER, VERSION, dis);
    }

    @SuppressWarnings("deprecation")
    @Override
    public Path readPath(Graph graph) throws IOException {

        // Read and check magic number and version.
        checkMagicNumberOrThrow(dis.readInt());
        checkVersionOrThrow(dis.readInt());

        // Read map ID and check against graph.
        final String mapId =
                readFixedLengthString(BinaryGraphReader.MAP_ID_FIELD_LENGTH, "UTF-8");

        if (!mapId.equals(graph.getMapId())) {
            throw new MapMismatchException(mapId, graph.getMapId());
        }

        // Number of nodes in the path (without first and last).
        final int nbNodes = dis.readInt();

        // Skip (duplicate) first and last node
        readNode(graph);
        readNode(graph);

        // Read intermediate nodes:
        final ArrayList<Node> nodes = new ArrayList<Node>();
        for (int i = 0; i < nbNodes; ++i) {
            nodes.add(readNode(graph));
        }

        return Path.createFastestPathFromNodes(graph, nodes);
    }

    /**
     * Read a node from the input stream and returns it.
     *
     * @param graph Graph containing the nodes.
     * @return The next node in the input stream.
     * @throws IOException if something occurs while reading the note.
     * @throws IndexOutOfBoundsException if the node is not in the graph.
     */
    protected Node readNode(Graph graph) throws IOException {
        return graph.get(dis.readInt());
    }

}
