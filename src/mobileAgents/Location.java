package mobileAgents;

public class Location {
    private final int x;
    private final int y;

    public Location(int x, int y){
        this.x = x;
        this.y =y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    //may need this later
    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if(!(o instanceof Location)) {
            return false;
        }
        Location loc = (Location) o;

        return x == loc.x && y == loc.y;
    }
}
