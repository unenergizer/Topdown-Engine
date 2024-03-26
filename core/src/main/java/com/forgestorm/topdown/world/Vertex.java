package com.forgestorm.topdown.world;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
public class Vertex {

    private int x, y, z;
    private float u, v;
    @Setter
    private float nx, ny, nz;

    public Vertex(int x, int y, int z, float u, float v) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.u = u;
        this.v = v;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Vertex vertex = (Vertex) obj;
        return Float.compare(vertex.x, x) == 0 &&
            Float.compare(vertex.y, y) == 0 &&
            Float.compare(vertex.z, z) == 0 &&
            Float.compare(vertex.u, u) == 0 &&
            Float.compare(vertex.v, v) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z, u, v);
    }

    @Override
    public String toString() {
        return "Vertex{" +
            "x=" + x +
            ", y=" + y +
            ", z=" + z +
            ", u=" + u +
            ", v=" + v +
            ", nx=" + nx +
            ", ny=" + ny +
            ", nz=" + nz +
            '}';
    }
}
