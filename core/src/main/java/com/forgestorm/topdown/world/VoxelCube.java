package com.forgestorm.topdown.world;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lombok.AllArgsConstructor;

import java.util.List;

import static com.forgestorm.topdown.GameConstants.Chunk.QUAD_HEIGHT;
import static com.forgestorm.topdown.GameConstants.Chunk.QUAD_WIDTH;
import static com.forgestorm.topdown.world.ChunkMeshGenerator.generateNormals;

@AllArgsConstructor
public class VoxelCube {

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
        Vertex a = new Vertex(x + QUAD_WIDTH, distort(y + QUAD_HEIGHT), distort(z + QUAD_WIDTH), u2, v2);
        Vertex b = new Vertex(x + QUAD_WIDTH, distort(y + QUAD_HEIGHT), distort(z), u2, v1);
        Vertex c = new Vertex(x, distort(y + QUAD_HEIGHT), distort(z + QUAD_WIDTH), u1, v2);

        // Triangle 2 wrong dir
        Vertex a2 = new Vertex(x, distort(y + QUAD_HEIGHT), distort(z), u1, v1);
        Vertex b2 = new Vertex(x, distort(y + QUAD_HEIGHT), distort(z + QUAD_WIDTH), u1, v2);
        Vertex c2 = new Vertex(x + QUAD_WIDTH, distort(y + QUAD_HEIGHT), distort(z), u2, v1);

        generateNormals(b,a,c);
        generateNormals(b2,a2,c2);

        vertices.add(a);
        vertices.add(b);
        vertices.add(c);
        vertices.add(a2);
        vertices.add(b2);
        vertices.add(c2);
    }

    public void createBottom(List<Vertex> vertices, float x, float y, float z, TextureRegion textureRegion) {
        float u1 = textureRegion.getU();
        float v1 = textureRegion.getV();
        float u2 = textureRegion.getU2();
        float v2 = textureRegion.getV2();

        // Tri 1
        Vertex a = new Vertex(x + QUAD_WIDTH, distort(y), distort(z + QUAD_WIDTH), u2, v2);
        Vertex b = new Vertex(x, distort(y), distort(z + QUAD_WIDTH), u2, v1);
        Vertex c = new Vertex(x + QUAD_WIDTH, distort(y), distort(z), u1, v2);

        // Tri 2
        Vertex a2 = new Vertex(x, distort(y), distort(z), u1, v1);
        Vertex b2 = new Vertex(x + QUAD_WIDTH, distort(y), distort(z), u1, v2);
        Vertex c2 = new Vertex(x, distort(y), distort(z + QUAD_WIDTH), u2, v1);

        generateNormals(b,a,c);
        generateNormals(b2,a2,c2);

        vertices.add(a);
        vertices.add(b);
        vertices.add(c);
        vertices.add(a2);
        vertices.add(b2);
        vertices.add(c2);
    }

    public void createLeft(List<Vertex> vertices, float x, float y, float z, TextureRegion textureRegion) {
        float u1 = textureRegion.getU();
        float v1 = textureRegion.getV();
        float u2 = textureRegion.getU2();
        float v2 = textureRegion.getV2();

        // Tri 1
        Vertex a = new Vertex(x, distort(y + QUAD_HEIGHT), distort(z + QUAD_WIDTH), u2, v1);
        Vertex b = new Vertex(x, distort(y + QUAD_HEIGHT), distort(z), u1, v1);
        Vertex c = new Vertex(x, distort(y), distort(z + QUAD_WIDTH), u2, v2);

        // Tri 2
        Vertex a2 = new Vertex(x, distort(y), distort(z), u1, v2);
        Vertex b2 = new Vertex(x, distort(y), distort(z + QUAD_WIDTH), u2, v2);
        Vertex c2 = new Vertex(x, distort(y + QUAD_HEIGHT), distort(z), u1, v1);

        generateNormals(b,a,c);
        generateNormals(b2,a2,c2);

        vertices.add(a);
        vertices.add(b);
        vertices.add(c);
        vertices.add(a2);
        vertices.add(b2);
        vertices.add(c2);

    }

    public void createRight(List<Vertex> vertices, float x, float y, float z, TextureRegion textureRegion) {
        float u1 = textureRegion.getU();
        float v1 = textureRegion.getV();
        float u2 = textureRegion.getU2();
        float v2 = textureRegion.getV2();

        // Tri 1
        Vertex a = new Vertex(x + QUAD_WIDTH, distort(y + QUAD_HEIGHT), distort(z + QUAD_WIDTH), u1, v1);
        Vertex b = new Vertex(x + QUAD_WIDTH, distort(y), distort(z + QUAD_WIDTH), u1, v2);
        Vertex c = new Vertex(x + QUAD_WIDTH, distort(y + QUAD_HEIGHT), distort(z), u2, v1);

        // Tri 2
        Vertex a2 = new Vertex(x + QUAD_WIDTH, distort(y), distort(z), u2, v2);
        Vertex b2 = new Vertex(x + QUAD_WIDTH, distort(y + QUAD_HEIGHT), distort(z), u2, v1);
        Vertex c2 = new Vertex(x + QUAD_WIDTH, distort(y), distort(z + QUAD_WIDTH), u1, v2);

        generateNormals(b,a,c);
        generateNormals(b2,a2,c2);

        vertices.add(a);
        vertices.add(b);
        vertices.add(c);
        vertices.add(a2);
        vertices.add(b2);
        vertices.add(c2);
    }

    public void createFront(List<Vertex> vertices, float x, float y, float z, TextureRegion textureRegion) {
        float u1 = textureRegion.getU();
        float v1 = textureRegion.getV();
        float u2 = textureRegion.getU2();
        float v2 = textureRegion.getV2();

        // Tri 1
        Vertex a = new Vertex(x + QUAD_WIDTH, distort(y + QUAD_HEIGHT), distort(z), u1, v1);
        Vertex b = new Vertex(x + QUAD_WIDTH, distort(y), distort(z), u1, v2);
        Vertex c = new Vertex(x, distort(y + QUAD_HEIGHT), distort(z), u2, v1);

        // Tri 2
        Vertex a2 = new Vertex(x, distort(y), distort(z), u2, v2);
        Vertex b2 = new Vertex(x, distort(y + QUAD_HEIGHT), distort(z), u2, v1);
        Vertex c2 = new Vertex(x + QUAD_WIDTH, distort(y), distort(z), u1, v2);

        generateNormals(b,a,c);
        generateNormals(b2,a2,c2);

        vertices.add(a);
        vertices.add(b);
        vertices.add(c);
        vertices.add(a2);
        vertices.add(b2);
        vertices.add(c2);
    }

    public void createBack(List<Vertex> vertices, float x, float y, float z, TextureRegion textureRegion) {
        float u1 = textureRegion.getU();
        float v1 = textureRegion.getV();
        float u2 = textureRegion.getU2();
        float v2 = textureRegion.getV2();

        // Tri 1
        Vertex a = new Vertex(x + QUAD_WIDTH, distort(y + QUAD_HEIGHT), distort(z + QUAD_WIDTH), u2, v1);
        Vertex b = new Vertex(x, distort(y + QUAD_HEIGHT), distort(z + QUAD_WIDTH), u1, v1);
        Vertex c = new Vertex(x + QUAD_WIDTH, distort(y), distort(z + QUAD_WIDTH), u2, v2);

        // Tri 2
        Vertex a2 = new Vertex(x, distort(y), distort(z + QUAD_WIDTH), u1, v2);
        Vertex b2 = new Vertex(x + QUAD_WIDTH, distort(y), distort(z + QUAD_WIDTH), u2, v2);
        Vertex c2 = new Vertex(x, distort(y + QUAD_HEIGHT), distort(z + QUAD_WIDTH), u1, v1);

        generateNormals(b,a,c);
        generateNormals(b2,a2,c2);

        vertices.add(a);
        vertices.add(b);
        vertices.add(c);
        vertices.add(a2);
        vertices.add(b2);
        vertices.add(c2);
    }
}
