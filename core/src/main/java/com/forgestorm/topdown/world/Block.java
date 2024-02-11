package com.forgestorm.topdown.world;

import lombok.Getter;
import lombok.Setter;

import static com.forgestorm.topdown.GameConstants.Chunk.CHUNK_XYZ_LENGTH;
import static com.forgestorm.topdown.GameConstants.Text.SLASH;

@Getter
public class Block {

    private final int chunkX, chunkZ, chunkSection;
    private final int localX, localY, localZ;

    @Setter
    private BlockType blockType = BlockType.AIR;

    public Block(int chunkX, int chunkZ, int chunkSection, int localX, int localY, int localZ) {
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.chunkSection = chunkSection;
        this.localX = localX;
        this.localY = localY;
        this.localZ = localZ;
    }

    @Override
    public String toString() {
        int worldX = chunkX * CHUNK_XYZ_LENGTH + localX;
        int worldY = chunkZ * CHUNK_XYZ_LENGTH + localY;
        int worldZ = chunkSection * CHUNK_XYZ_LENGTH + localZ;
        return "Chunk XZ: " + chunkX + SLASH + chunkZ + ", ChunkSection: " + chunkSection + "\nLocal XYZ: " + localX + SLASH + localY + SLASH + localZ + "\nWorld XYZ: " + worldX + SLASH + worldY + SLASH + worldZ;
    }
}
