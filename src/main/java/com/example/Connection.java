package com.example;

import java.util.Objects;

public class Connection {
    private String connectFrom;
    private String connectTo;

    public Connection(String connectFrom, String connectTo) {
        this.connectFrom = connectFrom;
        this.connectTo = connectTo;
    }

    public String getConnectFrom() {
        return connectFrom;
    }

    public void setConnectFrom(String connectFrom) {
        this.connectFrom = connectFrom;
    }

    public String getConnectTo() {
        return connectTo;
    }

    public void setConnectTo(String connectTo) {
        this.connectTo = connectTo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Connection that = (Connection) o;
        return connectFrom.equals(that.connectFrom) &&
                connectTo.equals(that.connectTo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(connectFrom, connectTo);
    }

    @Override
    public String toString() {
        return connectFrom + " - " + connectTo;
    }
}
