package com.forgestorm.topdown.world;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import static com.forgestorm.topdown.GameConstants.Chunk.QUAD_HEIGHT;
import static com.forgestorm.topdown.GameConstants.Chunk.QUAD_WIDTH;

public class VoxelCube {

    public int createTop(float[] vertices, int vertexOffset, float x, float y, float z, TextureRegion textureRegion) {
        float u1 = 0, v1 = 0, u2 = 0, v2 = 0;
        if (textureRegion != null) u1 = textureRegion.getU();
        if (textureRegion != null) v1 = textureRegion.getV();
        if (textureRegion != null) u2 = textureRegion.getU2();
        if (textureRegion != null) v2 = textureRegion.getV2();

        // Bottom Left [0,0]
        vertices[vertexOffset++] = x;
        vertices[vertexOffset++] = y + QUAD_HEIGHT;
        vertices[vertexOffset++] = z;
        vertices[vertexOffset++] = u1;
        vertices[vertexOffset++] = v1;
        vertexOffset = addNormals(vertices, vertexOffset);

        // Bottom Right [1,0]
        vertices[vertexOffset++] = x + QUAD_WIDTH;
        vertices[vertexOffset++] = y + QUAD_HEIGHT;
        vertices[vertexOffset++] = z;
        vertices[vertexOffset++] = u2;
        vertices[vertexOffset++] = v1;
        vertexOffset = addNormals(vertices, vertexOffset);

        // Top Right [1,1]
        vertices[vertexOffset++] = x + QUAD_WIDTH;
        vertices[vertexOffset++] = y + QUAD_HEIGHT;
        vertices[vertexOffset++] = z + QUAD_WIDTH;
        vertices[vertexOffset++] = u2;
        vertices[vertexOffset++] = v2;
        vertexOffset = addNormals(vertices, vertexOffset);

        // Top Left [0,1]
        vertices[vertexOffset++] = x;
        vertices[vertexOffset++] = y + QUAD_HEIGHT;
        vertices[vertexOffset++] = z + QUAD_WIDTH;
        vertices[vertexOffset++] = u1;
        vertices[vertexOffset++] = v2;
        vertexOffset = addNormals(vertices, vertexOffset);

        return vertexOffset;
    }

    public int createBottom(float[] vertices, int vertexOffset, float x, float y, float z, TextureRegion textureRegion) {
        float u1 = 0, v1 = 0, u2 = 0, v2 = 0;
        if (textureRegion != null) u1 = textureRegion.getU();
        if (textureRegion != null) v1 = textureRegion.getV();
        if (textureRegion != null) u2 = textureRegion.getU2();
        if (textureRegion != null) v2 = textureRegion.getV2();

        vertices[vertexOffset++] = x;
        vertices[vertexOffset++] = y;
        vertices[vertexOffset++] = z;
        vertices[vertexOffset++] = u1;
        vertices[vertexOffset++] = v1;
        vertexOffset = addNormals(vertices, vertexOffset);

        vertices[vertexOffset++] = x;
        vertices[vertexOffset++] = y;
        vertices[vertexOffset++] = z + QUAD_WIDTH;
        vertices[vertexOffset++] = u2;
        vertices[vertexOffset++] = v1;
        vertexOffset = addNormals(vertices, vertexOffset);

        vertices[vertexOffset++] = x + QUAD_WIDTH;
        vertices[vertexOffset++] = y;
        vertices[vertexOffset++] = z + QUAD_WIDTH;
        vertices[vertexOffset++] = u2;
        vertices[vertexOffset++] = v2;
        vertexOffset = addNormals(vertices, vertexOffset);

        vertices[vertexOffset++] = x + QUAD_WIDTH;
        vertices[vertexOffset++] = y;
        vertices[vertexOffset++] = z;
        vertices[vertexOffset++] = u1;
        vertices[vertexOffset++] = v2;
        vertexOffset = addNormals(vertices, vertexOffset);

        return vertexOffset;
    }

    public int createLeft(float[] vertices, int vertexOffset, float x, float y, float z, TextureRegion textureRegion) {
        float u1 = 0, v1 = 0, u2 = 0, v2 = 0;
        if (textureRegion != null) u1 = textureRegion.getU();
        if (textureRegion != null) v1 = textureRegion.getV();
        if (textureRegion != null) u2 = textureRegion.getU2();
        if (textureRegion != null) v2 = textureRegion.getV2();

        vertices[vertexOffset++] = x;
        vertices[vertexOffset++] = y;
        vertices[vertexOffset++] = z;
        vertices[vertexOffset++] = u1;
        vertices[vertexOffset++] = v2;
        vertexOffset = addNormals(vertices, vertexOffset);

        vertices[vertexOffset++] = x;
        vertices[vertexOffset++] = y + QUAD_HEIGHT;
        vertices[vertexOffset++] = z;
        vertices[vertexOffset++] = u1;
        vertices[vertexOffset++] = v1;
        vertexOffset = addNormals(vertices, vertexOffset);

        vertices[vertexOffset++] = x;
        vertices[vertexOffset++] = y + QUAD_HEIGHT;
        vertices[vertexOffset++] = z + QUAD_WIDTH;
        vertices[vertexOffset++] = u2;
        vertices[vertexOffset++] = v1;
        vertexOffset = addNormals(vertices, vertexOffset);

        vertices[vertexOffset++] = x;
        vertices[vertexOffset++] = y;
        vertices[vertexOffset++] = z + QUAD_WIDTH;
        vertices[vertexOffset++] = u2;
        vertices[vertexOffset++] = v2;
        vertexOffset = addNormals(vertices, vertexOffset);

        return vertexOffset;
    }

    public int createRight(float[] vertices, int vertexOffset, float x, float y, float z, TextureRegion textureRegion) {
        float u1 = 0, v1 = 0, u2 = 0, v2 = 0;
        if (textureRegion != null) u1 = textureRegion.getU();
        if (textureRegion != null) v1 = textureRegion.getV();
        if (textureRegion != null) u2 = textureRegion.getU2();
        if (textureRegion != null) v2 = textureRegion.getV2();

        vertices[vertexOffset++] = x + QUAD_WIDTH;
        vertices[vertexOffset++] = y;
        vertices[vertexOffset++] = z;
        vertices[vertexOffset++] = u2;
        vertices[vertexOffset++] = v2;
        vertexOffset = addNormals(vertices, vertexOffset);

        vertices[vertexOffset++] = x + QUAD_WIDTH;
        vertices[vertexOffset++] = y;
        vertices[vertexOffset++] = z + QUAD_WIDTH;
        vertices[vertexOffset++] = u1;
        vertices[vertexOffset++] = v2;
        vertexOffset = addNormals(vertices, vertexOffset);

        // Top Right
        vertices[vertexOffset++] = x + QUAD_WIDTH;
        vertices[vertexOffset++] = y + QUAD_HEIGHT;
        vertices[vertexOffset++] = z + QUAD_WIDTH;
        vertices[vertexOffset++] = u1;
        vertices[vertexOffset++] = v1;
        vertexOffset = addNormals(vertices, vertexOffset);

        vertices[vertexOffset++] = x + QUAD_WIDTH;
        vertices[vertexOffset++] = y + QUAD_HEIGHT;
        vertices[vertexOffset++] = z;
        vertices[vertexOffset++] = u2;
        vertices[vertexOffset++] = v1;
        vertexOffset = addNormals(vertices, vertexOffset);

        return vertexOffset;
    }

    public int createFront(float[] vertices, int vertexOffset, float x, float y, float z, TextureRegion textureRegion) {
        float u1 = 0, v1 = 0, u2 = 0, v2 = 0;
        if (textureRegion != null) u1 = textureRegion.getU();
        if (textureRegion != null) v1 = textureRegion.getV();
        if (textureRegion != null) u2 = textureRegion.getU2();
        if (textureRegion != null) v2 = textureRegion.getV2();

        vertices[vertexOffset++] = x;
        vertices[vertexOffset++] = y;
        vertices[vertexOffset++] = z;
        vertices[vertexOffset++] = u2;
        vertices[vertexOffset++] = v2;
        vertexOffset = addNormals(vertices, vertexOffset);

        vertices[vertexOffset++] = x + QUAD_WIDTH;
        vertices[vertexOffset++] = y;
        vertices[vertexOffset++] = z;
        vertices[vertexOffset++] = u1;
        vertices[vertexOffset++] = v2;
        vertexOffset = addNormals(vertices, vertexOffset);

        vertices[vertexOffset++] = x + QUAD_WIDTH;
        vertices[vertexOffset++] = y + QUAD_HEIGHT;
        vertices[vertexOffset++] = z;
        vertices[vertexOffset++] = u1;
        vertices[vertexOffset++] = v1;
        vertexOffset = addNormals(vertices, vertexOffset);

        vertices[vertexOffset++] = x;
        vertices[vertexOffset++] = y + QUAD_HEIGHT;
        vertices[vertexOffset++] = z;
        vertices[vertexOffset++] = u2;
        vertices[vertexOffset++] = v1;
        vertexOffset = addNormals(vertices, vertexOffset);

        return vertexOffset;
    }

    public int createBack(float[] vertices, int vertexOffset, float x, float y, float z, TextureRegion textureRegion) {
        float u1 = 0, v1 = 0, u2 = 0, v2 = 0;
        if (textureRegion != null) u1 = textureRegion.getU();
        if (textureRegion != null) v1 = textureRegion.getV();
        if (textureRegion != null) u2 = textureRegion.getU2();
        if (textureRegion != null) v2 = textureRegion.getV2();

        vertices[vertexOffset++] = x;
        vertices[vertexOffset++] = y;
        vertices[vertexOffset++] = z + QUAD_WIDTH;
        vertices[vertexOffset++] = u1;
        vertices[vertexOffset++] = v2;
        vertexOffset = addNormals(vertices, vertexOffset);

        vertices[vertexOffset++] = x;
        vertices[vertexOffset++] = y + QUAD_HEIGHT;
        vertices[vertexOffset++] = z + QUAD_WIDTH;
        vertices[vertexOffset++] = u1;
        vertices[vertexOffset++] = v1;
        vertexOffset = addNormals(vertices, vertexOffset);

        vertices[vertexOffset++] = x + QUAD_WIDTH;
        vertices[vertexOffset++] = y + QUAD_HEIGHT;
        vertices[vertexOffset++] = z + QUAD_WIDTH;
        vertices[vertexOffset++] = u2;
        vertices[vertexOffset++] = v1;
        vertexOffset = addNormals(vertices, vertexOffset);

        vertices[vertexOffset++] = x + QUAD_WIDTH;
        vertices[vertexOffset++] = y;
        vertices[vertexOffset++] = z + QUAD_WIDTH;
        vertices[vertexOffset++] = u2;
        vertices[vertexOffset++] = v2;
        vertexOffset = addNormals(vertices, vertexOffset);

        return vertexOffset;
    }

    private int addNormals(float[] vertices, int vertexOffset) {
        for (int i = 0; i < 3; i++) vertices[vertexOffset++] = 1f;
        return vertexOffset;
    }
}
