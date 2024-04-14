package com.forgestorm.topdown.world;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lombok.Getter;
import lombok.Setter;

import static com.forgestorm.topdown.Main.renderUtils;
import static com.forgestorm.topdown.world.ChunkManager.CHUNK_SIZE;

@Getter
public class Block {

    private final int chunkX, chunkZ, chunkSection;
    private final int localX, localY, localZ;

    @Setter
    private TextureRegion topRegion;
    @Setter
    private TextureRegion bottomRegion;
    @Setter
    private TextureRegion leftRegion;
    @Setter
    private TextureRegion rightRegion;
    @Setter
    private TextureRegion backRegion;
    @Setter
    private TextureRegion frontRegion;

    @Setter
    private BlockType blockType = BlockType.BLOCK;

    public Block(int chunkX, int chunkZ, int chunkSection, int localX, int localY, int localZ) {
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.chunkSection = chunkSection;
        this.localX = localX;
        this.localY = localY;
        this.localZ = localZ;

        TextureAtlas.AtlasRegion top = renderUtils.getTexture("cube_top");
        setTopRegion(top);
        TextureAtlas.AtlasRegion bottom = renderUtils.getTexture("cube_bottom");
        setBottomRegion(bottom);

        TextureAtlas.AtlasRegion left = renderUtils.getTexture("cube_left");
        setLeftRegion(left);
        TextureAtlas.AtlasRegion right = renderUtils.getTexture("cube_right");
        setRightRegion(right);

        TextureAtlas.AtlasRegion front = renderUtils.getTexture("cube_front");
        setFrontRegion(front);
        TextureAtlas.AtlasRegion back = renderUtils.getTexture("cube_back");
        setBackRegion(back);
    }

    public int getWorldX() {
        return chunkX * CHUNK_SIZE + localX;
    }
    public int getWorldZ() {
        return chunkZ * CHUNK_SIZE + localZ;
    }
    public int getWorldY() {
        return chunkSection * CHUNK_SIZE + localY;
    }

    @Override
    public String toString() {
        return "Chunk XZ: " + chunkX + "/" + chunkZ + ", ChunkSection: " + chunkSection + ", BlockType: " + blockType + "\nLocal XYZ: " + localX + "/" + localY + "/" + localZ + "\nWorld XYZ: " + getWorldX() + "/" + getWorldY() + "/" + getWorldZ();
    }
}
