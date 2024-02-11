package com.forgestorm.topdown.world;

import lombok.Getter;

import static com.forgestorm.topdown.GameConstants.Chunk.*;

public class ChunkSection {

    @Getter
    private final int chunkX, chunkZ, chunkSection;
    private final Block[] blocks;

    public ChunkSection(int chunkX, int chunkZ, int chunkSection) {
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.chunkSection = chunkSection;
        this.blocks = new Block[CHUNK_XYZ_LENGTH * CHUNK_XYZ_LENGTH * CHUNK_XYZ_LENGTH]; // X * Y * Z

        initializeChunkSection();
    }

    private void initializeChunkSection() {
        for (int localX = 0; localX < CHUNK_XYZ_LENGTH; localX++) {
            for (int localY = 0; localY < CHUNK_XYZ_LENGTH; localY++) {
                for (int localZ = 0; localZ < CHUNK_XYZ_LENGTH; localZ++) {
                    setLocalChunkBlock(localX, localY, localZ);
                }
            }
        }
    }

    public Block getLocalChunkBlock(int localX, int localY, int localZ) {
        // Check for out of bounds
        if (localX < 0 || localY < 0 || localZ < 0 || localX > CHUNK_XYZ_LENGTH || localY > CHUNK_XYZ_LENGTH || localZ > CHUNK_XYZ_LENGTH) {
            throw new RuntimeException("XYZ out of bounds: " + localX + "/" + localY + "/" + localZ);
        }

        int index = getIndex(localX, localY, localZ);
        return blocks[index];
    }

    public void setLocalChunkBlock(int localX, int localY, int localZ) {
        Block block = new Block(chunkX, chunkZ, chunkSection, localX, localY, localZ);

        int index = getIndex(localX, localY, localZ);
        blocks[index] = block;
    }

    public int getIndex(int localX, int localY, int localZ) {
        if (localX >= CHUNK_XYZ_LENGTH || localY >= CHUNK_XYZ_LENGTH || localZ >= CHUNK_XYZ_LENGTH || localX < 0 || localY < 0 || localZ < 0) {
            throw new IllegalArgumentException("Block coordinates out of chunk bounds!");
        }
        return (localZ << MAX_Z_INDEX) | (localY << MAX_Y_INDEX) | localX;
    }
}
