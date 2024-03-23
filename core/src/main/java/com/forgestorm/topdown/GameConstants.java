package com.forgestorm.topdown;

/**
 * Constants here can be adjusted to changes various things about the game world.
 */
public class GameConstants {

    /**
     * Constants related to chunk size and rendering.
     */
    public static class Chunk {
        /**
         * The length, width, and height of a chunk in blocks. Min value 2.
         */
        public static final int CHUNK_SIZE = 16;
        /**
         * Calculate the number of bits needed for the X index.
         * log2(width) gives the number of bits needed for X
         * Used in {@link com.forgestorm.topdown.world.ChunkSection}
         */
        public static final int MAX_Y_INDEX = (int) (Math.log(CHUNK_SIZE) / Math.log(2));
        /**
         * Cumulative bits of Y. Used in {@link com.forgestorm.topdown.world.ChunkSection}
         */
        public static final int MAX_Z_INDEX = MAX_Y_INDEX + (int) (Math.log(CHUNK_SIZE) / Math.log(2));
    }

    public static class Text {
        public static final String SLASH = "/";
    }
}
