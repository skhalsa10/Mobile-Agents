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
        return (this.getFirst().equals(e.getFirst()) &&  this.getSecond().equals(e.getSecond())) ||
                (this.getFirst().equals(e.getSecond()) && this.getSecond().equals(e.getFirst()));
    }


}
