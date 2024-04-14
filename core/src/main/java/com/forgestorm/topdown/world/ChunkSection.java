package com.forgestorm.topdown.world;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.utils.Disposable;
import lombok.Getter;
import lombok.Setter;

import static com.forgestorm.topdown.world.ChunkManager.CHUNK_SIZE;

public class ChunkSection implements Disposable {

    @Getter
    private final int chunkX, chunkZ, chunkY;
    private final Block[] blocks;

    @Getter
    @Setter
    private Mesh chunkMesh;

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

        //Calculate the number of bits needed for the X index. log2(width) gives the number of bits needed for X
        int MAX_Y_INDEX = (int) (Math.log(CHUNK_SIZE) / Math.log(2));
        //Cumulative bits of Y
        int MAX_Z_INDEX = MAX_Y_INDEX + (int) (Math.log(CHUNK_SIZE) / Math.log(2));

        return (localZ << MAX_Z_INDEX) | (localY << MAX_Y_INDEX) | localX;
    }

    @Override
    public void dispose() {
        if (chunkMesh != null) chunkMesh.dispose();
    }
}
