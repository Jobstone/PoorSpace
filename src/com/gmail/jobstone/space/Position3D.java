package com.gmail.jobstone.space;

public class Position3D {

    public final int x;
    public final int y;
    public final int z;

    public Position3D(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Position3D diff(Position3D pos) {
        return new Position3D(this.x - pos.x, this.y - pos.y, this.z - pos.z);
    }

    public Position3D add(Position3D pos) {
        return new Position3D(this.x + pos.x, this.y + pos.y, this.z + pos.z);
    }

    public Position2D toPosition2D() {
        return new Position2D(this.x, this.y);
    }

    @Override
    public String toString() {
        return this.x + "." + this.y + "." + this.z;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Position3D) {
            Position3D pos = (Position3D)o;
            return this.x == pos.x && this.y == pos.y && this.z == pos.z;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = result * 31 + this.x;
        result = result * 31 + this.y;
        result = result * 31 + this.z;
        return result;
    }

}
