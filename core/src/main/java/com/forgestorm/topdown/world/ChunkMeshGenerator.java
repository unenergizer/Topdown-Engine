package com.forgestorm.topdown.world;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.forgestorm.topdown.GameConstants.Chunk.*;

public class ChunkMeshGenerator {

    private final VoxelCube voxelCube = new VoxelCube(true);
    private final VoxelRamp voxelRamp = new VoxelRamp(true);

    private final ChunkManager chunkManager;

    public ChunkMeshGenerator(ChunkManager chunkManager) {
        this.chunkManager = chunkManager;
    }

    /**
     * Generates a chunk landscape mesh.
     */
    public void generateChunkMesh(Chunk chunk) {
        System.out.println(chunk);

        for (int i = 0; i < chunk.getChunkSections().length; i++) {
            ChunkSection chunkSection = chunk.getChunkSections()[i];

            // Generate vertices
            List<Vertex> allVertices = new ArrayList<>();
            populateVertices(chunkSection, allVertices);

            // Generate indices
            List<Vertex> uniqueVertices = new ArrayList<>();
            List<Integer> indices = new ArrayList<>();
            generateIndices(allVertices, uniqueVertices, indices);

            System.out.println("AllVertices: " + allVertices.size() + ", UniqueVertices: " + uniqueVertices.size() + ", Indices: " + indices.size());

            // Create the mesh
            chunkSection.setChunkMesh(createMesh(uniqueVertices, indices));
        }
    }

    private void populateVertices(ChunkSection chunkSection, List<Vertex> vertices) {
        //Populate the vertices array with data
        for (int x = 0; x < CHUNK_SIZE; x++) {
            for (int z = 0; z < CHUNK_SIZE; z++) {
                for (int y = 0; y < CHUNK_SIZE; y++) {
                    Block volume = chunkSection.getLocalChunkBlock(x, y, z);
                    if (volume == null) continue;

                    BlockType blockType = volume.getBlockType();
                    if (blockType == BlockType.AIR) continue;

                    switch (blockType) {
                        case BLOCK:
                            populateBlockVertices(vertices, x, y, z, volume);
                            break;
                        case TRIANGULAR_PRISM_45:
                        case TRIANGULAR_PRISM_135:
                        case TRIANGULAR_PRISM_255:
                        case TRIANGULAR_PRISM_315:
                            populateRampVertices(vertices, x, y, z, volume);
                            break;
                    }
                }
            }
        }
    }

    private void populateBlockVertices(List<Vertex> vertices, int x, int y, int z, Block block) {
        // Check neighboring blocks to determine which faces to cull
        //TODO make the sides not visible in 2D view toggle
        boolean top = isSolid(block.getWorldX(), block.getWorldY() + 1, block.getWorldZ());
        boolean bot = isSolid(block.getWorldX(), block.getWorldY() - 1, block.getWorldZ());
        boolean lef = isSolid(block.getWorldX() - 1, block.getWorldY(), block.getWorldZ());
        boolean rig = isSolid(block.getWorldX() + 1, block.getWorldY(), block.getWorldZ());
        boolean fro = isSolid(block.getWorldX(), block.getWorldY(), block.getWorldZ() - 1);
        boolean bac = isSolid(block.getWorldX(), block.getWorldY(), block.getWorldZ() + 1);

        // Scale the worldX and worldZ by QUAD_SIZE to avoid overlap
        float renderX = x * QUAD_WIDTH;
        float renderY = y * QUAD_HEIGHT;
        float renderZ = z * QUAD_WIDTH;

        if (!top) voxelCube.createTop(vertices, renderX, renderY, renderZ, block.getTopRegion());
        if (!bot) voxelCube.createBottom(vertices, renderX, renderY, renderZ, block.getBottomRegion());
        if (!lef) voxelCube.createLeft(vertices, renderX, renderY, renderZ, block.getLeftRegion());
        if (!rig) voxelCube.createRight(vertices, renderX, renderY, renderZ, block.getRightRegion());
        if (!fro) voxelCube.createFront(vertices, renderX, renderY, renderZ, block.getFrontRegion());
        if (!bac) voxelCube.createBack(vertices, renderX, renderY, renderZ, block.getBackRegion());
    }

    private void populateRampVertices(List<Vertex> vertices, int x, int y, int z, Block block) {
        // Check neighboring blocks to determine which faces to cull
        boolean top = isSolid(block.getWorldX(), y + 1, block.getWorldZ());
        boolean bot = isSolid(block.getWorldX(), y - 1, block.getWorldZ());
//        boolean lef = isSolid(worldX - 1, y, worldZ);
        boolean rig = isSolid(block.getWorldX() + 1, y, block.getWorldZ());
//        boolean fro = isSolid(worldX, y, worldZ - 1);
        boolean bac = isSolid(block.getWorldX(), y, block.getWorldZ() + 1);

        // Scale the worldX and worldZ by QUAD_SIZE to avoid overlap
        float renderX = x * QUAD_WIDTH;
        float renderY = y * QUAD_HEIGHT;
        float renderZ = z * QUAD_WIDTH;

        if (!top) voxelRamp.createTop(vertices, renderX, renderY, renderZ, block.getTopRegion());
        if (!bot) voxelRamp.createBottom(vertices, renderX, renderY, renderZ, block.getBottomRegion());
        if (!rig) voxelRamp.createRight(vertices, renderX, renderY, renderZ, block.getRightRegion());
        if (!bac) voxelRamp.createBack(vertices, renderX, renderY, renderZ, block.getBackRegion());
        voxelRamp.createDiagonal(vertices, renderX, renderY, renderZ, block.getLeftRegion());
    }

    private void generateIndices(List<Vertex> allVertices, List<Vertex> uniqueVertices, List<Integer> indices) {
        Map<Vertex, Integer> vertexToIndexMap = new HashMap<>();

        for (Vertex vertex : allVertices) {
            if (vertexToIndexMap.containsKey(vertex)) {
                // Vertex is duplicated, use the existing index
//                System.out.println("Contains Key: " + vertex);
                indices.add(vertexToIndexMap.get(vertex));
            } else {
//                System.out.println("Key Not contained: " + vertex);
                // New unique vertex, add to list and map
                uniqueVertices.add(vertex);
                int newIndex = uniqueVertices.size() - 1;
                vertexToIndexMap.put(vertex, newIndex);
                indices.add(newIndex);
            }
        }
    }

    private Mesh createMesh(List<Vertex> uniqueVertices, List<Integer> indices) {
        // Define the attributes for this model
        int positionComponents = 3;
        int textureCoordinatesComponents = 2;
        int normalComponents = 3;

        //We will replace with converted mesh after we split chunk sections into separate meshes
        //VertexAttribute test = new VertexAttribute(VertexAttributes.Usage.Generic, 1, GL20.GL_INT, false, "a_data");

        int numberComponents = (positionComponents + textureCoordinatesComponents + normalComponents);

        // Convert to libgdx mesh
        float[] verticesArray = new float[uniqueVertices.size() * numberComponents]; // Assuming 3 floats per vertex
        short[] indicesArray = new short[indices.size()];

        int i = 0;
        for (Vertex vertex : uniqueVertices) {
            verticesArray[i++] = vertex.getX();
            verticesArray[i++] = vertex.getY();
            verticesArray[i++] = vertex.getZ();
            verticesArray[i++] = vertex.getU();
            verticesArray[i++] = vertex.getV();
            verticesArray[i++] = vertex.getNx();
            verticesArray[i++] = vertex.getNy();
            verticesArray[i++] = vertex.getNz();
        }

        for (i = 0; i < indices.size(); i++) {
            indicesArray[i] = indices.get(i).shortValue(); // Cast to short if necessary
        }

        Mesh mesh = new Mesh(true, verticesArray.length, indicesArray.length, VertexAttribute.Position(), VertexAttribute.TexCoords(0), VertexAttribute.Normal());
        mesh.setVertices(verticesArray);
        mesh.setIndices(indicesArray);

        return mesh;
    }

    private boolean isSolid(int worldX, int worldY, int worldZ) {
        if (worldY < 0) return false;
        if (worldY >= chunkManager.worldHeight) return false;

        Block block = chunkManager.getWorldChunkBlock(worldX, worldY, worldZ);
        if (block == null) return false;

        BlockType blockType = block.getBlockType();
        if (blockType == BlockType.AIR) return false;
        if (blockType == BlockType.TRIANGULAR_PRISM_315) return false;
        return true;
    }
}
