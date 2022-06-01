package com.example;

import org.junit.Assert;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;

public class TaskTest {

    @Test(expected = IllegalArgumentException.class)
    public void applyInvalidInput1() {
        Task task = new Task();
        task.apply("1", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void applyInvalidInput2() {
        Task task = new Task();
        task.apply("1", Arrays.asList(null, ""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void applyInvalidInput3() {
        Task task = new Task();
        task.apply("1", Arrays.asList("2", "3"));
        task.apply("4", Arrays.asList("4", "4", "4"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void applyInvalidConnection() {
        Task task = new Task();
        task.apply("1", Arrays.asList("1"));
        List<Connection> connections = task.getConnections();
    }

    @Test
    public void applySingle2VertexGraph() {
        Task task = new Task();
        task.apply("1", Arrays.asList("4"));
        List<Connection> connections = task.getConnections();
        Assert.assertEquals("Connection size is not 2", 2, connections.size());
        Assert.assertTrue("List differs!", connections.containsAll(Arrays.asList(
                new Connection("1", "4"),
                new Connection("4", "1")
        )));
    }

    @Test
    public void applySingle3VertexGraph() {
        Task task = new Task();
        task.apply("1", Arrays.asList("2", "3"));
        List<Connection> connections = task.getConnections();
        Assert.assertEquals("Connection size is not 6", 6, connections.size());
        Assert.assertTrue("List differs!", connections.containsAll(Arrays.asList(
                new Connection("1", "2"),
                new Connection("1", "3"),
                new Connection("2", "1"),
                new Connection("2", "3"),
                new Connection("3", "1"),
                new Connection("3", "2")
        )));
    }

    @Test
    public void applyTwoSeparateGraphs() {
        Task task = new Task();
        task.apply("1", Arrays.asList("2", "3"));
        task.apply("4", Arrays.asList("5"));
        List<Connection> connections = task.getConnections();
        Assert.assertEquals("Connection size is not 8", 8, connections.size());
        Assert.assertTrue("List differs!", connections.containsAll(Arrays.asList(
                new Connection("1", "2"),
                new Connection("1", "3"),
                new Connection("2", "1"),
                new Connection("2", "3"),
                new Connection("3", "1"),
                new Connection("3", "2"),
                new Connection("4", "5"),
                new Connection("5", "4")
        )));
    }

    @Test
    public void applyTreeSeparateGraphs() {
        Task task = new Task();
        task.apply("1", Arrays.asList("2", "3"));
        task.apply("4", Arrays.asList("5", "6"));
        task.apply("7", Arrays.asList("8"));
        List<Connection> connections = task.getConnections();
        Assert.assertEquals("Connection size is not 14", 14, connections.size());
        Assert.assertTrue("List differs!", connections.containsAll(Arrays.asList(
                new Connection("1", "2"),
                new Connection("1", "3"),
                new Connection("2", "1"),
                new Connection("2", "3"),
                new Connection("3", "1"),
                new Connection("3", "2"),
                new Connection("4", "5"),
                new Connection("4", "6"),
                new Connection("5", "4"),
                new Connection("5", "6"),
                new Connection("6", "4"),
                new Connection("6", "5"),
                new Connection("7", "8"),
                new Connection("8", "7")
        )));
    }

    @Test
    public void applyTwoConnectedGraphs() {
        Task task = new Task();
        task.apply("1", Arrays.asList("2", "3"));
        task.apply("1", Arrays.asList("4"));
        List<Connection> connections = task.getConnections();
        Assert.assertEquals("Connection size is not 12", 12, connections.size());
        Assert.assertTrue("List differs!", connections.containsAll(Arrays.asList(
                new Connection("1", "2"),
                new Connection("1", "3"),
                new Connection("1", "4"),
                new Connection("2", "1"),
                new Connection("2", "3"),
                new Connection("2", "4"),
                new Connection("3", "1"),
                new Connection("3", "2"),
                new Connection("3", "4"),
                new Connection("4", "1"),
                new Connection("4", "2"),
                new Connection("4", "3")
        )));
    }

    @Test
    public void applyTwoConnectedGraphsReversed() {
        Task task = new Task();
        task.apply("1", Arrays.asList("2", "3"));
        task.apply("4", Arrays.asList("1"));
        List<Connection> connections = task.getConnections();
        Assert.assertEquals("Connection size is not 12", 12, connections.size());
        Assert.assertTrue("List differs!", connections.containsAll(Arrays.asList(
                new Connection("1", "2"),
                new Connection("1", "3"),
                new Connection("1", "4"),
                new Connection("2", "1"),
                new Connection("2", "3"),
                new Connection("2", "4"),
                new Connection("3", "1"),
                new Connection("3", "2"),
                new Connection("3", "4"),
                new Connection("4", "1"),
                new Connection("4", "2"),
                new Connection("4", "3")
        )));
    }

    @Test
    public void applyTwoConnectedGraphsWithDuplicates() {
        Task task = new Task();
        task.apply("1", Arrays.asList("2", "3"));
        task.apply("4", Arrays.asList("2","3", "5"));
        List<Connection> connections = task.getConnections();
        Assert.assertEquals("Connection size is not 20", 20, connections.size());
        Assert.assertTrue("List differs!", connections.containsAll(Arrays.asList(
                new Connection("1", "2"),
                new Connection("1", "3"),
                new Connection("1", "4"),
                new Connection("1", "5"),
                new Connection("2", "1"),
                new Connection("2", "3"),
                new Connection("2", "4"),
                new Connection("2", "5"),
                new Connection("3", "1"),
                new Connection("3", "2"),
                new Connection("3", "4"),
                new Connection("3", "5"),
                new Connection("4", "1"),
                new Connection("4", "2"),
                new Connection("4", "3"),
                new Connection("4", "5"),
                new Connection("5", "1"),
                new Connection("5", "2"),
                new Connection("5", "3"),
                new Connection("5", "4")
        )));
    }

    @Test
    public void applyConnectTreeSeparateGraphs() {
        Task task = new Task();
        task.apply("1", Arrays.asList("2", "3"));
        task.apply("4", Arrays.asList("5"));
        task.apply("1", Arrays.asList("4", "6", "7")); // Connecting
        List<Connection> connections = task.getConnections();
        Assert.assertEquals("Connection size is not 42", 42, connections.size());
        Assert.assertTrue("List differs!", connections.containsAll(Arrays.asList(
                new Connection("1", "2"),
                new Connection("1", "3"),
                new Connection("1", "4"),
                new Connection("1", "5"),
                new Connection("1", "6"),
                new Connection("1", "7"),

                new Connection("2", "1"),
                new Connection("2", "3"),
                new Connection("2", "4"),
                new Connection("2", "5"),
                new Connection("2", "6"),
                new Connection("2", "7"),

                new Connection("3", "1"),
                new Connection("3", "2"),
                new Connection("3", "4"),
                new Connection("3", "5"),
                new Connection("3", "6"),
                new Connection("3", "7"),

                new Connection("4", "1"),
                new Connection("4", "2"),
                new Connection("4", "3"),
                new Connection("4", "5"),
                new Connection("4", "6"),
                new Connection("4", "7"),

                new Connection("5", "1"),
                new Connection("5", "2"),
                new Connection("5", "3"),
                new Connection("5", "4"),
                new Connection("5", "6"),
                new Connection("5", "7"),

                new Connection("6", "1"),
                new Connection("6", "2"),
                new Connection("6", "3"),
                new Connection("6", "4"),
                new Connection("6", "5"),
                new Connection("6", "7"),

                new Connection("7", "1"),
                new Connection("7", "2"),
                new Connection("7", "3"),
                new Connection("7", "4"),
                new Connection("7", "5"),
                new Connection("7", "6")
        )));
    }
}