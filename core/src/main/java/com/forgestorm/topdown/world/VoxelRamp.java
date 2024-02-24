package com.forgestorm.topdown.world;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lombok.AllArgsConstructor;

import java.util.List;

import static com.forgestorm.topdown.GameConstants.Chunk.QUAD_HEIGHT;
import static com.forgestorm.topdown.GameConstants.Chunk.QUAD_WIDTH;

@AllArgsConstructor
public class VoxelRamp {

    private final boolean enableOrthographicDistortion;

    private float distort(float val) {
        if (!enableOrthographicDistortion) return val;
        return val + (val * ((float) Math.sqrt(2) - 1f));
    }

    public void createTop(List<Vertex> vertices, float x, float y, float z, TextureRegion textureRegion) {
        float u1 = textureRegion.getU();
        float v1 = textureRegion.getV();
        float u2 = textureRegion.getU2();
        float v2 = textureRegion.getV2();

        // Triangle 1
        vertices.add(new Vertex(x + QUAD_WIDTH, distort(y + QUAD_HEIGHT), distort(z + QUAD_WIDTH), u2, v2, 9f, 9f, 9f));
        vertices.add(new Vertex(x + QUAD_WIDTH, distort(y + QUAD_HEIGHT), distort(z), u2, v1, 9f, 9f, 9f));
        vertices.add(new Vertex(x, distort(y + QUAD_HEIGHT), distort(z + QUAD_WIDTH), u1, v2, 9f, 9f, 9f));
    }

    public void createBottom(List<Vertex> vertices, float x, float y, float z, TextureRegion textureRegion) {
        float u1 = textureRegion.getU();
        float v1 = textureRegion.getV();
        float u2 = textureRegion.getU2();
        float v2 = textureRegion.getV2();

        // Tri 1
        vertices.add(new Vertex(x + QUAD_WIDTH, distort(y), distort(z + QUAD_WIDTH), u2, v2, 1f, 1f, 1f));
        vertices.add(new Vertex(x, distort(y), distort(z + QUAD_WIDTH), u2, v1, 1f, 1f, 1f));
        vertices.add(new Vertex(x + QUAD_WIDTH, distort(y), distort(z), u1, v2, 1f, 1f, 1f));
    }

    public void createDiagonal(List<Vertex> vertices, float x, float y, float z, TextureRegion textureRegion) {
        float u1 = textureRegion.getU();
        float v1 = textureRegion.getV();
        float u2 = textureRegion.getU2();
        float v2 = textureRegion.getV2();

        // Tri 1
        vertices.add(new Vertex(x, distort(y + QUAD_HEIGHT), distort(z + QUAD_WIDTH), u2, v1, 1f, 1f, 1f));
        vertices.add(new Vertex(x + QUAD_WIDTH, distort(y + QUAD_HEIGHT), distort(z), u1, v1, 1f, 1f, 1f));
        vertices.add(new Vertex(x, distort(y), distort(z + QUAD_WIDTH), u2, v2, 1f, 1f, 1f));

        // Tri 2
        vertices.add(new Vertex(x + QUAD_WIDTH, distort(y), distort(z), u1, v2, 1f, 1f, 1f));
        vertices.add(new Vertex(x, distort(y), distort(z + QUAD_WIDTH), u2, v2, 1f, 1f, 1f));
        vertices.add(new Vertex(x + QUAD_WIDTH, distort(y + QUAD_HEIGHT), distort(z), u1, v1, 1f, 1f, 1f));

    }

    public void createRight(List<Vertex> vertices, float x, float y, float z, TextureRegion textureRegion) {
        float u1 = textureRegion.getU();
        float v1 = textureRegion.getV();
        float u2 = textureRegion.getU2();
        float v2 = textureRegion.getV2();

        // Tri 1
        vertices.add(new Vertex(x + QUAD_WIDTH, distort(y + QUAD_HEIGHT), distort(z + QUAD_WIDTH), u1, v1, 1f, 1f, 1f));
        vertices.add(new Vertex(x + QUAD_WIDTH, distort(y), distort(z + QUAD_WIDTH), u1, v2, 1f, 1f, 1f));
        vertices.add(new Vertex(x + QUAD_WIDTH, distort(y + QUAD_HEIGHT), distort(z), u2, v1, 1f, 1f, 1f));

        // Tri 2
        vertices.add(new Vertex(x + QUAD_WIDTH, distort(y), distort(z), u2, v2, 1f, 1f, 1f));
        vertices.add(new Vertex(x + QUAD_WIDTH, distort(y + QUAD_HEIGHT), distort(z), u2, v1, 1f, 1f, 1f));
        vertices.add(new Vertex(x + QUAD_WIDTH, distort(y), distort(z + QUAD_WIDTH), u1, v2, 1f, 1f, 1f));
    }

    public void createBack(List<Vertex> vertices, float x, float y, float z, TextureRegion textureRegion) {
        float u1 = textureRegion.getU();
        float v1 = textureRegion.getV();
        float u2 = textureRegion.getU2();
        float v2 = textureRegion.getV2();

        // Tri 1
        vertices.add(new Vertex(x + QUAD_WIDTH, distort(y + QUAD_HEIGHT), distort(z + QUAD_WIDTH), u2, v1, 1f, 1f, 1f));
        vertices.add(new Vertex(x, distort(y + QUAD_HEIGHT), distort(z + QUAD_WIDTH), u1, v1, 1f, 1f, 1f));
        vertices.add(new Vertex(x + QUAD_WIDTH, distort(y), distort(z + QUAD_WIDTH), u2, v2, 1f, 1f, 1f));

        // Tri 2
        vertices.add(new Vertex(x, distort(y), distort(z + QUAD_WIDTH), u1, v2, 1f, 1f, 1f));
        vertices.add(new Vertex(x + QUAD_WIDTH, distort(y), distort(z + QUAD_WIDTH), u2, v2, 1f, 1f, 1f));
        vertices.add(new Vertex(x, distort(y + QUAD_HEIGHT), distort(z + QUAD_WIDTH), u1, v1, 1f, 1f, 1f));
    }
}
