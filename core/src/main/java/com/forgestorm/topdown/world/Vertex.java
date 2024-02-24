package com.forgestorm.topdown.world;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public class Vertex {

    private float x, y, z;
    private float u, v;
    private float nx, ny, nz;

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
