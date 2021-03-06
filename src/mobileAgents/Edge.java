package mobileAgents;

/**
 * Class for edge between two nodes
 */
public class Edge {
    private Location first;
    private Location second;

    /**
     * Constructs an edge with a first and second location
     * @param first the starting location
     * @param second the ending location
     */
    public Edge (Location first, Location second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Gets the first (starting) location
     * @return first/starting location
     */
    public Location getFirst() {
        return first;
    }

    /**
     * Gets the second (ending) location
     * @return second/ending location
     */
    public Location getSecond() {
        return second;
    }

    /**
     * Checks if two edges are equal
     * @param o object
     * @return true or false
     */
    @Override
    public boolean equals(Object o) {
        //null check
        if(o == null) {
            return false;
        }

        //reference check
        if (o == this) {
            return true;
        }

        //instance check
        if(!(o instanceof Edge)){
            return false;
        }

        Edge e = (Edge)o;

        //valueCheck
        return (this.getFirst().equals(e.getFirst()) &&
                this.getSecond().equals(e.getSecond())) ||
                (this.getFirst().equals(e.getSecond()) &&
                        this.getSecond().equals(e.getFirst()));
    }


}
