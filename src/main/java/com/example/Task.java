package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Test task #1
 * Not thread-safe impl.!
 * @Autor Andrey P.
 */
public class Task {

    private static final Logger LOGGER = LoggerFactory.getLogger(Task.class);

    private final List<Connection> connections = new ArrayList<>();

    // Copies connections list, used to speed up search!
    private final Set<Connection> connectionsSet = new HashSet<>();

    // Helping structure, e.g. for case of [1, 2, 3]
    // [1, {1, 2, 3}], [2, {1, 2, 3}], [2, {1, 2, 3}]
    private final Map<String, Set<String>> cachedNodes = new HashMap<>();

    public List<Connection> getConnections() {
        LOGGER.info("Connections = {}", connections);
        return connections;
    }

    /**
     * Algorithm:
     * We distinguish between 2 cases a) separate graph, b) connecting graph
     * If input does not contain nodes that are being vertex of other graph(s)
     * we deal with case a), otherwise with case b)
     * @param connectFrom a node to be connected to connectTos
     * @param connectTos a list of nodes to be connected to connectFrom
     */
    public void apply(String connectFrom, List<String> connectTos) {
        // Guard statements!
        if (connectFrom==null || connectTos==null)
            throw new IllegalArgumentException("Nullable input!");
        if (connectFrom.isEmpty() || connectTos.isEmpty())
            throw new IllegalArgumentException("Invalid empty input!");
        // Filter out nulls and empty strings
        List<String> filteredConnectTos = connectTos.stream()
                .filter(c -> c != null && !c.isEmpty()).collect(Collectors.toList());
        if (filteredConnectTos.isEmpty())
            throw new IllegalArgumentException("Invalid input! No valid connectTos!");
        Set<String> newNodes =
                new HashSet<>(connectTos); newNodes.add(connectFrom);
        if (newNodes.isEmpty() || newNodes.size()<2)
            throw new IllegalArgumentException("Invalid input! No valid connectTos!");

        if (cachedNodes.isEmpty()) {// Init case
            processSeparateGraph(newNodes);
        } else { // Some graphs already exist
            // Find out if we face a separate graph?
            Optional<String> resultOfSearch =
                    newNodes.stream().filter(node ->
                            cachedNodes.containsKey(node)).findFirst();
            if (!resultOfSearch.isPresent()) {
                // Like init case: new separate graph
                processSeparateGraph(newNodes);
            } else {// Case when some merge is required!
                processConnectingGraphs(newNodes);
            }
        }
        // Sort the output list
        Collections.sort(connections, Comparator
                .comparing(Connection::getConnectFrom)
                .thenComparing(Connection::getConnectTo));
    }

    private void processSeparateGraph(Set<String> nodes) {
        nodes.forEach(node->createAndSaveNode(node, nodes));
        // Create and save interconnections
        createAndSaveInterconnections(nodes);
    }

    private void processConnectingGraphs(Set<String> newNodes) {
        // Split into new nodes and existing
        Map<Boolean,Set<String>> split = newNodes.stream()
                .collect(Collectors
                        .groupingBy(node ->
                                cachedNodes.containsKey(node), Collectors.toSet()));
        Set<String> existingNodes = split.get(true);
        Set<String> nonExistingNodes = split.get(false);
        if (nonExistingNodes!=null && !nonExistingNodes.isEmpty() && nonExistingNodes.size()>1) {
            // Create a new separate graph of 2+;
            processSeparateGraph(nonExistingNodes);
        }
        // Gather all graphs to be connected
        Set<Set<String>> setOfToBeConnectedGraphs =
                existingNodes.stream().map(cachedNodes::get).collect(Collectors.toSet());
        // Add non-existing graph if present
        setOfToBeConnectedGraphs
                .add((nonExistingNodes!=null) ? nonExistingNodes : Collections.emptySet());
        // Connect graphs
        connectGraphs(setOfToBeConnectedGraphs);
    }

    private void createAndSaveNode(String newNode, Set<String> newConnectedNodes) {
        this.cachedNodes.putIfAbsent(newNode, newConnectedNodes);
    }

    private void mergeConnectedNodes(String oldNode, Set<String> newConnectedNodes) {
        this.cachedNodes.computeIfAbsent(oldNode, (k) -> newConnectedNodes);
        this.cachedNodes.computeIfPresent(oldNode, (k, v) ->
                Stream.concat(v.stream(), newConnectedNodes.stream()).collect(Collectors.toSet()));
        //LOGGER.info("Merged key = {}, values = {}", oldNode, newConnectedNodes);
    }

    private void createAndSaveInterconnections(Set<String> nodes) {
        nodes.forEach(nodeFrom->
                nodes.forEach(nodeTo->createAndSaveConnectionBetween(nodeFrom, nodeTo)));
    }

    private void createAndSaveConnectionBetween(String connectFrom, String connectTo) {
        if (!connectFrom.equals(connectTo)) {
            Connection newConnection =
                    new Connection(connectFrom, connectTo);
            Connection reversedNewConnection =
                    new Connection(connectTo, connectFrom);
            if (!connectionsSet.contains(newConnection)) {
                connections.add(newConnection);
                // Duplicate to the set to speed up "contains"!
                connectionsSet.add(newConnection);
            }
            if (!connectionsSet.contains(reversedNewConnection)) {
                connections.add(reversedNewConnection);
                // Duplicate to the set to speed up "contains"!
                connectionsSet.add(reversedNewConnection);
            }
            //LOGGER.info("Connected: {} - {}", connectFrom, connectTo);
        }
    }

    // Like [[1, 2, 3], [4, 5], [6, 7]]
    private void connectGraphs(Set<Set<String>> setToBeConnectedGraphs) {
        //LOGGER.info("Connect graphs = {}", setToBeConnectedGraphs);
        for (Set<String> setOfToBeConnectedGraph : setToBeConnectedGraphs) {
            for (String nodeToBeConnected : setOfToBeConnectedGraph) {
                Set<String> setOfToBeMerged =
                        prepareSetToBeMerged(setToBeConnectedGraphs, setOfToBeConnectedGraph);
                mergeConnectedNodes(nodeToBeConnected, setOfToBeMerged);
                // Create Connection objects
                setOfToBeMerged.add(nodeToBeConnected);
                createAndSaveInterconnections(setOfToBeMerged);
            }
        }
        //LOGGER.info("Connect graphs end");
    }

    private Set<String> prepareSetToBeMerged(Set<Set<String>> setToBeConnectedGraphs, Set<String> setToBeConnectedGraph) {
        Set<String> setToBeMerged = new HashSet<>(setToBeConnectedGraphs)
                .stream().filter(nextSet -> !nextSet.equals(setToBeConnectedGraph))
                .flatMap(Collection::stream).collect(Collectors.toSet());
        //LOGGER.info("SetOfToBeMerged = {}", setOfToBeMerged);
        return setToBeMerged;
    }
}
