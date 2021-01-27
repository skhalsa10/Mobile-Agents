package mobileAgents;

/**
 * location class is simple and has an x and a y representing the coordinates of the node... or whatever actually.
 */
public class Location {
    private final int x;
    private final int y;

    /**
     * contruct a location with given x and y
     * @param x
     * @param y
     */
    public Location(int x, int y){
        this.x = x;
        this.y =y;
    }

    /**
     *
     * @return the x value
     */
    public int getX() {
        return x;
    }

    /**
     *
     * @return the y value
     */
    public int getY() {
        return y;
    }


    /**
     * Over ride Hashcode needed for overriding Equals and
     * critical if we use inside of hash collections
     * @return
     */
    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;

        return prime*(result+x+y);
    }

    /**
     * better equals method that checks internals for equality
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
        if(!(o instanceof Location)){
            return false;
        }

        Location l = (Location)o;

        //valueCheck
        return (this.x == l.getX() && this.y == l.getY());
    }
}
