package com.forgestorm.topdown;

/**
 * Constants here can be adjusted to changes various things about the game world.
 */
public class GameConstants {

    /**
     * Constants related to chunk size and rendering.
     */
    public static class Chunk {
        public static  final int CHUNKS_RENDER_X = 2;
        public static  final int CHUNKS_RENDER_Z = 2;
        /**
         * The length, width, and height of a chunk. Min value 2.
         */
        public static final int CHUNK_XYZ_LENGTH = 16;
        /**
         * The Z height of the world in chunk layers.
         */
        public static final int CHUNK_SECTIONS = 1;
        /**
         * The Y length of blocks in chunks.
         */
        public static final int WORLD_HEIGHT = CHUNK_SECTIONS * CHUNK_XYZ_LENGTH;
        /**
         * Calculate the number of bits needed for the X index.
         * log2(width) gives the number of bits needed for X
         * Used in {@link com.forgestorm.topdown.world.ChunkSection}
         */
        public static final int MAX_Y_INDEX = (int) (Math.log(CHUNK_XYZ_LENGTH) / Math.log(2));
        /**
         * Cumulative bits of Y. Used in {@link com.forgestorm.topdown.world.ChunkSection}
         */
        public static final int MAX_Z_INDEX = MAX_Y_INDEX + (int) (Math.log(CHUNK_XYZ_LENGTH) / Math.log(2));
        /**
         * The length and width of a tile in pixels.
         */
        public static final int TILE_SIZE = 16;
        /**
         * The length and width of a quad in meters.
         */
        public static final float QUAD_WIDTH = 1f;
        /**
         * The height of a quad in meters.
         */
        public static final float QUAD_HEIGHT = 1;
    }

    public static class Text {
        public static final String SLASH = "/";
    }

    public static float distort(float value) {
        return value + (value * ((float) Math.sqrt(2) - 1f));
    }
}
