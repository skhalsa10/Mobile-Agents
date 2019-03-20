package mobileAgents;

public class Edge {
    private Location first;
    private Location second;
    public Edge (Location first, Location second) {
        this.first = first;
        this.second = second;
    }

    public Location getFirst() {
        return first;
    }

    public Location getSecond() {
        return second;
    }


}
