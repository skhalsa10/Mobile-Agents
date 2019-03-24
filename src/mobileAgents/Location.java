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


    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;

        return prime*(result+x+y);
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
        if(!(o instanceof Location)){
            return false;
        }

        Location l = (Location)o;

        //valueCheck
        return (this.x == l.getX() && this.y == l.getY());
    }
}
