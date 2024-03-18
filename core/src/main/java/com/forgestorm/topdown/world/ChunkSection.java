package com.forgestorm.topdown.world;

import lombok.Getter;

import static com.forgestorm.topdown.GameConstants.Chunk.*;

public class ChunkSection {

    @Getter
    private final int chunkX, chunkZ, chunkY;
    private final Block[] blocks;

    public ChunkSection(int chunkX, int chunkZ, int chunkY) {
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.chunkY = chunkY;
        this.blocks = new Block[CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE]; // X * Y * Z

        for (int localX = 0; localX < CHUNK_SIZE; localX++) {
            for (int localY = 0; localY < CHUNK_SIZE; localY++) {
                for (int localZ = 0; localZ < CHUNK_SIZE; localZ++) {
                    setLocalChunkBlock(localX, localY, localZ);
                }
            }
        }
    }

    public Block getLocalChunkBlock(int localX, int localY, int localZ) {
        // Check for out of bounds
        if (localX < 0 || localY < 0 || localZ < 0 || localX > CHUNK_SIZE || localY > CHUNK_SIZE || localZ > CHUNK_SIZE) {
            throw new RuntimeException("XYZ out of bounds: " + localX + "/" + localY + "/" + localZ);
        }

        int index = getIndex(localX, localY, localZ);
        return blocks[index];
    }

    public void setLocalChunkBlock(int localX, int localY, int localZ) {
        Block block = new Block(chunkX, chunkZ, chunkY, localX, localY, localZ);

        int index = getIndex(localX, localY, localZ);
        blocks[index] = block;
    }

    public int getIndex(int localX, int localY, int localZ) {
        if (localX >= CHUNK_SIZE || localY >= CHUNK_SIZE || localZ >= CHUNK_SIZE || localX < 0 || localY < 0 || localZ < 0) {
            throw new IllegalArgumentException("Block coordinates out of chunk bounds!");
        }
        return (localZ << MAX_Z_INDEX) | (localY << MAX_Y_INDEX) | localX;
    }
}
