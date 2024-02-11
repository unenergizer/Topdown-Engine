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
         * The length, width, and height of a chunk. Min value 2.
         */
        public static final int CHUNK_XYZ_LENGTH = 16;
        /**
         * The Z height of the world in chunk layers.
         */
        public static final int CHUNK_SECTIONS = 2;
        /**
         * The maximum number of vertical sections in a single chunk.
         */
        public static final int MAX_VERTICAL_CHUNK_SECTIONS = 1;
        /**
         * The Y length of blocks in chunks.
         */
        public static final int WORLD_HEIGHT = MAX_VERTICAL_CHUNK_SECTIONS * CHUNK_XYZ_LENGTH;
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
        public static final float QUAD_HEIGHT = distort(TILE_SIZE);
    }

    public static class Text {
        public static final String SLASH = "/";
    }

    public static float distort(float value) {
        return value + (value * ((float) Math.sqrt(2) - 1f));
    }
}
