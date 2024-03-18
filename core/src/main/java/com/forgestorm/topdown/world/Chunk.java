package com.forgestorm.topdown.world;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.utils.Disposable;
import lombok.Getter;
import lombok.Setter;

import static com.forgestorm.topdown.GameConstants.Chunk.CHUNK_SIZE;
import static com.forgestorm.topdown.GameConstants.Text.SLASH;

public class Chunk implements Disposable {

    @Getter
    private final int chunkX, chunkZ, height;
    private final ChunkSection[] chunkSections;
    @Getter
    @Setter
    private Mesh chunkMesh;

    public Chunk(int chunkX, int chunkZ, int height) {
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.height = height;
        this.chunkSections = new ChunkSection[height];

        initializeVerticalChunks();
    }

    private void initializeVerticalChunks() {
        for (int chunkY = 0; chunkY < height; chunkY++) {
            this.chunkSections[chunkY] = new ChunkSection(chunkX, chunkZ, chunkY);
        }
    }

    public Block getWorldChunkBlock(int worldX, int worldY, int worldZ) {

        int chunkSection = Math.floorDiv(worldY, CHUNK_SIZE);
        int localX = worldX - chunkX * CHUNK_SIZE;
        int localY = worldY - chunkSection * CHUNK_SIZE;
        int localZ = worldZ - chunkZ * CHUNK_SIZE;

        return getLocalChunkBlock(localX, localY, localZ, chunkSection);
    }

    public void setWorldChunkBlock(int worldX, int worldZ, int worldY) {

        int chunkSection = Math.floorDiv(worldY, CHUNK_SIZE);
        int localX = worldX - chunkX * CHUNK_SIZE;
        int localY = worldY - chunkSection * CHUNK_SIZE;
        int localZ = worldZ - chunkZ * CHUNK_SIZE;

        setLocalChunkBlock(localX, localY, localZ, chunkSection);
    }

    public Block getLocalChunkBlock(int localX, int localY, int localZ, int verticalChunkIndex) {
        return chunkSections[verticalChunkIndex].getLocalChunkBlock(localX, localY, localZ);
    }

    public void setLocalChunkBlock(int localX, int localY, int localZ, int verticalChunkIndex) {
        chunkSections[verticalChunkIndex].setLocalChunkBlock(localX, localY, localZ);
    }

    @Override
    public String toString() {
        return "Chunk XY: " + chunkX + SLASH + chunkZ + ", ChunkSections: " + chunkSections.length;
    }

    @Override
    public void dispose() {
        if (chunkMesh != null) chunkMesh.dispose();
    }
}
