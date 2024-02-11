package com.forgestorm.topdown.world;

import com.badlogic.gdx.utils.Disposable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.forgestorm.topdown.GameConstants.Chunk.CHUNK_SECTIONS;
import static com.forgestorm.topdown.GameConstants.Chunk.CHUNK_XYZ_LENGTH;

public class ChunkManager implements Disposable {
    private final Map<Integer, Chunk> chunkConcurrentMap = new HashMap<>();

    public ChunkManager() {
        makeTestChunks();
        verifyChunks();
    }

    public Block getWorldChunkBlock(int worldX, int worldY, int worldZ) {
        int chunkX = (int) Math.floor(worldX / (float) CHUNK_XYZ_LENGTH);
        int chunkZ = (int) Math.floor(worldZ / (float) CHUNK_XYZ_LENGTH);

        Chunk chunk = getChunk(chunkX, chunkZ);
        return chunk.getWorldChunkBlock(worldX, worldY, worldZ);
    }

    public void setWorldChunkBlock(int worldX, int worldY, int worldZ) {
        int chunkX = (int) Math.floor(worldX / (float) CHUNK_XYZ_LENGTH);
        int chunkZ = (int) Math.floor(worldZ / (float) CHUNK_XYZ_LENGTH);

        Chunk chunk = getChunk(chunkX, chunkZ);
        if (chunk == null) {
            chunk = new Chunk(chunkX, chunkZ);
            setChunk(chunk);
        }

        chunk.setWorldChunkBlock(worldX, worldY, worldZ);
    }

    public Chunk getChunk(int chunkX, int chunkZ) {
        return chunkConcurrentMap.get(getChunkHashCode(chunkX, chunkZ));
    }

    private void setChunk(Chunk chunk) {
        chunkConcurrentMap.put(getChunkHashCode(chunk.getChunkX(), chunk.getChunkZ()), chunk);
    }

    private int getChunkHashCode(int chunkX, int chunkZ) {
        return chunkX * 31 + chunkZ;
    }

    public Collection<Chunk> getChunks() {
        return chunkConcurrentMap.values();
    }

    private void verifyChunks() {
        int renderWidthX = 3;
        int renderHeightY = 2;

        System.out.println("Total Chunks: " + chunkConcurrentMap.size());
        for (int chunkX = 0; chunkX < renderWidthX; chunkX++) {
            for (int chunkZ = 0; chunkZ < renderHeightY; chunkZ++) {
                Chunk chunk = getChunk(chunkX, chunkZ);
                for (int chunkSection = 0; chunkSection < CHUNK_SECTIONS; chunkSection++) {
                    int nonZeroTiles = 0;
                    for (int x = 0; x < CHUNK_XYZ_LENGTH; x++) {
                        for (int y = 0; y < CHUNK_XYZ_LENGTH; y++) {
                            for (int z = 0; z < CHUNK_XYZ_LENGTH; z++) {
                                Block block = chunk.getLocalChunkBlock(x, z, y, chunkSection);
                                if (block.getBlockType() != BlockType.AIR) nonZeroTiles++;
                            }
                        }
                    }
                    System.out.println("nonZeroTiles: " + nonZeroTiles);
                }
            }
        }
    }

    private void makeTestChunks() {
        int renderWidthX = 3;
        int renderHeightY = 2;
        Random random = new Random();

        // TESTING:
        for (int chunkX = 0; chunkX < renderWidthX; chunkX++) {
            for (int chunkZ = 0; chunkZ < renderHeightY; chunkZ++) {

                Chunk chunk = new Chunk(chunkX, chunkZ);
                setChunk(chunk);

                for (int chunkSection = 0; chunkSection < CHUNK_SECTIONS; chunkSection++) {
                    for (int x = 0; x < CHUNK_XYZ_LENGTH; x++) {
                        for (int z = 0; z < CHUNK_XYZ_LENGTH; z++) {
                            for (int y = 0; y < CHUNK_XYZ_LENGTH; y++) {
                                chunk.setLocalChunkBlock(x, y, z, chunkSection);

                                Block volume = chunk.getLocalChunkBlock(x, y, z, chunkSection);
                                int rand = random.nextInt(10);
                                if (rand == 0) {
                                    volume.setBlockType(BlockType.BLOCK);
                                } else if (rand == 1) {
                                    volume.setBlockType(BlockType.DIAGONAL_45);
                                } else if (rand == 2) {
                                    volume.setBlockType(BlockType.DIAGONAL_135);
                                } else if (rand == 3) {
                                    volume.setBlockType(BlockType.DIAGONAL_255);
                                } else if (rand == 4) {
                                    volume.setBlockType(BlockType.DIAGONAL_315);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void dispose() {
        for (Chunk chunk : chunkConcurrentMap.values()) chunk.dispose();
    }
}
