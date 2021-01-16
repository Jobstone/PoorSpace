package com.gmail.jobstone.space;

public class Position2D {

    public final int x;
    public final int y;

    public Position2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position2D diff(Position2D pos) {
        return new Position2D(this.x - pos.x, this.y - pos.y);
    }

    public Position2D add(Position2D pos) {
        return new Position2D(this.x + pos.x, this.y + pos.y);
    }

    public Position3D toPosition3D(int z) {
        return new Position3D(this.x, this.y, z);
    }

    @Override
    public String toString() {
        return this.x + "." + this.y;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Position2D) {
            Position2D pos = (Position2D)o;
            return this.x == pos.x && this.y == pos.y;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = result * 31 + this.x;
        result = result * 31 + this.y;
        return result;
    }

}
