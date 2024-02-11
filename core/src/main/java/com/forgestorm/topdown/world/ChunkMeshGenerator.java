package com.forgestorm.topdown.world;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import lombok.Setter;

import static com.forgestorm.topdown.GameConstants.Chunk.*;

public class ChunkMeshGenerator {

    private final VoxelCube voxelCube = new VoxelCube();

    private final ChunkManager chunkManager;

    /**
     * A quad has 6 indices.
     */
    private static final int INDICES_PER_FACE = 6;
    /**
     * A quad has 4 vertices, one at each corner.
     */
    private static final int QUAD_VERTICES = 4;

    @Setter
    private TextureRegion textureRegion;

    public ChunkMeshGenerator(ChunkManager chunkManager) {
        this.chunkManager = chunkManager;
    }

    /**
     * Generates a chunk landscape mesh.
     */
    public void generateChunkMesh(Chunk chunk) {
        System.out.println(chunk);
        // Define the attributes for this model
        VertexAttribute position = new VertexAttribute(VertexAttributes.Usage.Position, 3, ShaderProgram.POSITION_ATTRIBUTE);
        VertexAttribute textureCoordinates = new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE + "0");
        VertexAttribute normal = new VertexAttribute(VertexAttributes.Usage.Normal, 3, ShaderProgram.NORMAL_ATTRIBUTE);

        // Init vertices array
        int totalFacesToRender = getNumberOfVisibleBlockFaces(chunk);
        int numberComponents = (position.numComponents + normal.numComponents + textureCoordinates.numComponents);
        float[] vertices = new float[totalFacesToRender * numberComponents * QUAD_VERTICES];
        populateVertices(chunk, vertices);

        // Generate the indices
        short[] indices = new short[totalFacesToRender * INDICES_PER_FACE];
        generateIndices(indices);

        // Create the mesh
        Mesh terrainMesh = new Mesh(true, vertices.length, indices.length, position, textureCoordinates, normal);
        terrainMesh.setVertices(vertices);
        terrainMesh.setIndices(indices);

        chunk.setChunkMesh(terrainMesh);
    }

    private int getNumberOfVisibleBlockFaces(Chunk chunk) {
        int totalFacesToRender = 0;
        for (int chunkSection = 0; chunkSection < CHUNK_SECTIONS; chunkSection++) {
            for (int x = 0; x < CHUNK_XYZ_LENGTH; x++) {
                for (int y = 0; y < CHUNK_XYZ_LENGTH; y++) {
                    for (int z = 0; z < CHUNK_XYZ_LENGTH; z++) {

                        int worldX = x + CHUNK_XYZ_LENGTH * chunk.getChunkX();
                        int worldY = y + CHUNK_XYZ_LENGTH * chunkSection;
                        int worldZ = z + CHUNK_XYZ_LENGTH * chunk.getChunkZ();

                        Block block = chunkManager.getWorldChunkBlock(worldX, worldY, worldZ);
                        if (block == null || block.getBlockType() != BlockType.AIR) continue;

                        // Check neighboring blocks to determine which faces to cull
                        boolean top = isSolid(worldX, worldY + 1, worldZ);
                        boolean bot = isSolid(worldX, worldY - 1, worldZ);
                        boolean lef = isSolid(worldX - 1, worldY, worldZ);
                        boolean rig = isSolid(worldX + 1, worldY, worldZ);
                        boolean fro = isSolid(worldX, worldY, worldZ - 1);
                        boolean bac = isSolid(worldX, worldY, worldZ + 1);

                        if (!top) totalFacesToRender = totalFacesToRender + 1;
                        if (!bot) totalFacesToRender = totalFacesToRender + 1;
                        if (!lef) totalFacesToRender = totalFacesToRender + 1;
                        if (!rig) totalFacesToRender = totalFacesToRender + 1;
                        if (!fro) totalFacesToRender = totalFacesToRender + 1;
                        if (!bac) totalFacesToRender = totalFacesToRender + 1;
                    }
                }
            }
        }
        return totalFacesToRender;
    }

    private void populateVertices(Chunk chunk, float[] vertices) {
        // Populate the vertices array with data
        int vertexOffset = 0;
        for (int chunkSection = 0; chunkSection < CHUNK_SECTIONS; chunkSection++) {
            for (int x = 0; x < CHUNK_XYZ_LENGTH; x++) {
                for (int y = 0; y < CHUNK_XYZ_LENGTH; y++) {
                    for (int z = 0; z < CHUNK_XYZ_LENGTH; z++) {

                        int worldX = x + CHUNK_XYZ_LENGTH * chunk.getChunkX();
                        int worldY = y + CHUNK_XYZ_LENGTH * chunkSection;
                        int worldZ = z + CHUNK_XYZ_LENGTH * chunk.getChunkZ();

                        Block block = chunkManager.getWorldChunkBlock(worldX, worldY, worldZ);
                        if (block == null || block.getBlockType() != BlockType.AIR) continue;

                        // Check neighboring blocks to determine which faces to cull
                        boolean top = isSolid(worldX, worldY + 1, worldZ);
                        boolean bot = isSolid(worldX, worldY - 1, worldZ);
                        boolean lef = isSolid(worldX - 1, worldY, worldZ);
                        boolean rig = isSolid(worldX + 1, worldY, worldZ);
                        boolean fro = isSolid(worldX, worldY, worldZ - 1);
                        boolean bac = isSolid(worldX, worldY, worldZ + 1);

                        // Scale the worldX and worldZ by QUAD_SIZE to avoid overlap
                        float renderX = (x * QUAD_WIDTH) + (CHUNK_XYZ_LENGTH * chunk.getChunkX() * QUAD_WIDTH);
                        float renderY = y * QUAD_WIDTH;
                        float renderZ = (z * QUAD_WIDTH) + (CHUNK_XYZ_LENGTH * chunk.getChunkZ() * QUAD_WIDTH);

                        if (!top) {
                            vertexOffset = voxelCube.createTop(vertices, vertexOffset, renderX, renderY, renderZ, textureRegion);
                        }
                        if (!bot) {
                            vertexOffset = voxelCube.createBottom(vertices, vertexOffset, renderX, renderY, renderZ, textureRegion);
                        }
                        if (!lef) {
                            vertexOffset = voxelCube.createLeft(vertices, vertexOffset, renderX, renderY, renderZ, textureRegion);
                        }
                        if (!rig) {
                            vertexOffset = voxelCube.createRight(vertices, vertexOffset, renderX, renderY, renderZ, textureRegion);
                        }
                        if (!fro) {
                            vertexOffset = voxelCube.createFront(vertices, vertexOffset, renderX, renderY, renderZ, textureRegion);
                        }
                        if (!bac) {
                            vertexOffset = voxelCube.createBack(vertices, vertexOffset, renderX, renderY, renderZ, textureRegion);
                        }
                    }
                }
            }
        }
    }

    @SuppressWarnings("PointlessArithmeticExpression")
    private void generateIndices(short[] indices) {
        short j = 0;
        for (int i = 0; i < indices.length; i += 6, j += 4) {
            indices[i + 0] = (short) (j + 2);
            indices[i + 1] = (short) (j + 1);
            indices[i + 2] = (short) (j + 3);
            indices[i + 3] = (short) (j + 0);
            indices[i + 4] = (short) (j + 3);
            indices[i + 5] = (short) (j + 1);
        }
    }

    private boolean isSolid(int worldX, int worldY, int worldZ) {
        if (worldY < 0) return false;
        if (worldY >= WORLD_HEIGHT) return false;

        int chunkX = (int) Math.floor(worldX / (float) CHUNK_XYZ_LENGTH);
        int chunkZ = (int) Math.floor(worldZ / (float) CHUNK_XYZ_LENGTH);
        Chunk chunk = chunkManager.getChunk(chunkX, chunkZ);
        if (chunk == null) return false;

        Block block = chunk.getWorldChunkBlock(worldX, worldY, worldZ);
        return block != null && block.getBlockType() != BlockType.AIR;
    }
}
