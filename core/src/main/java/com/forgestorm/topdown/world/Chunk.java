package com.forgestorm.topdown.world;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.utils.Disposable;
import lombok.Getter;
import lombok.Setter;

import static com.forgestorm.topdown.GameConstants.Chunk.CHUNK_SECTIONS;
import static com.forgestorm.topdown.GameConstants.Chunk.CHUNK_XYZ_LENGTH;
import static com.forgestorm.topdown.GameConstants.Text.SLASH;

public class Chunk implements Disposable {

    @Getter
    private final int chunkX, chunkZ;
    private final ChunkSection[] chunkSections;
    @Getter
    @Setter
    private Mesh chunkMesh;

    public Chunk(int chunkX, int chunkZ) {
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.chunkSections = new ChunkSection[CHUNK_SECTIONS];

        initializeChunkFloors();
    }

    private void initializeChunkFloors() {
        for (int chunkSection = 0; chunkSection < CHUNK_SECTIONS; chunkSection++) {
            chunkSections[chunkSection] = new ChunkSection(chunkX, chunkZ, chunkSection);
        }
    }

    public Block getWorldChunkBlock(int worldX, int worldY, int worldZ) {

        int chunkSection = Math.floorDiv(worldY, CHUNK_XYZ_LENGTH);
        int localX = worldX - chunkX * CHUNK_XYZ_LENGTH;
        int localY = worldY - chunkSection * CHUNK_XYZ_LENGTH;
        int localZ = worldZ - chunkZ * CHUNK_XYZ_LENGTH;

        return getLocalChunkBlock(localX, localY, localZ, chunkSection);
    }

    public void setWorldChunkBlock(int worldX, int worldZ, int worldY) {

        int chunkSection = Math.floorDiv(worldY, CHUNK_XYZ_LENGTH);
        int localX = worldX - chunkX * CHUNK_XYZ_LENGTH;
        int localY = worldY - chunkSection * CHUNK_XYZ_LENGTH;
        int localZ = worldZ - chunkZ * CHUNK_XYZ_LENGTH;

        setLocalChunkBlock(localX, localY, localZ, chunkSection);
    }

    public Block getLocalChunkBlock(int localX, int localY, int localZ, int chunkSection) {
        return chunkSections[chunkSection].getLocalChunkBlock(localX, localY, localZ);
    }

    public void setLocalChunkBlock(int localX, int localY, int localZ, int chunkSection) {
        chunkSections[chunkSection].setLocalChunkBlock(localX, localY, localZ);
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
