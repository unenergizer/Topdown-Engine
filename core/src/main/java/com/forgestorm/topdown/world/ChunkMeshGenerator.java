package com.forgestorm.topdown.world;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.forgestorm.topdown.GameConstants.Chunk.*;

public class ChunkMeshGenerator {

    private final VoxelCube voxelCube = new VoxelCube(true);
    private final VoxelRamp voxelRamp = new VoxelRamp(true);

    private final ChunkManager chunkManager;

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

        // Generate vertices
        List<Vertex> allVertices = new ArrayList<>();
        populateVertices(chunk, allVertices);

        // Generate indices
        List<Vertex> uniqueVertices = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();
        generateIndices(allVertices, uniqueVertices, indices);

        System.out.println("AllVertices: " + allVertices.size() + ", UniqueVertices: " + uniqueVertices.size() + ", Indices: " + indices.size());
        System.out.println(indices);

        // Create the mesh
        Mesh mesh = createMesh(uniqueVertices, indices);
        chunk.setChunkMesh(mesh);
    }

    private void populateVertices(Chunk chunk, List<Vertex> vertices) {
        // Populate the vertices array with data
        for (int y = 0; y < WORLD_HEIGHT; y++) {
            for (int x = 0; x < CHUNK_XYZ_LENGTH; x++) {
                for (int z = 0; z < CHUNK_XYZ_LENGTH; z++) {

                    int worldX = x + CHUNK_XYZ_LENGTH * chunk.getChunkX();
                    int worldZ = z + CHUNK_XYZ_LENGTH * chunk.getChunkZ();

                    Block block = chunkManager.getWorldChunkBlock(worldX, y, worldZ);
                    if (block == null) continue;

                    BlockType blockType = block.getBlockType();
                    if (blockType == BlockType.AIR) continue;

                    switch (blockType) {
                        case BLOCK:
                            populateBlockVertices(chunk, vertices, x, y, z, worldX, worldZ);
                            break;
                        case TRIANGULAR_PRISM_45:
                        case TRIANGULAR_PRISM_135:
                        case TRIANGULAR_PRISM_255:
                        case TRIANGULAR_PRISM_315:
                            populateRampVertices(chunk, vertices, x, y, z, worldX, worldZ);
                            break;
                    }
                }
            }
        }
    }

    private void populateBlockVertices(Chunk chunk, List<Vertex> vertices, int x, int y, int z, int worldX, int worldZ) {
        // Check neighboring blocks to determine which faces to cull
        boolean top = isSolid(worldX, y + 1, worldZ, FaceType.TOP);
        boolean bot = isSolid(worldX, y - 1, worldZ, FaceType.BOTTOM);
        boolean lef = isSolid(worldX - 1, y, worldZ, FaceType.LEFT);
        boolean rig = isSolid(worldX + 1, y, worldZ, FaceType.RIGHT);
        boolean fro = isSolid(worldX, y, worldZ - 1, FaceType.FRONT);
        boolean bac = isSolid(worldX, y, worldZ + 1, FaceType.BACK);

        // Scale the worldX and worldZ by QUAD_SIZE to avoid overlap
        float renderX = (x * QUAD_WIDTH) + (CHUNK_XYZ_LENGTH * chunk.getChunkX() * QUAD_WIDTH);
        float renderY = y * QUAD_HEIGHT;
        float renderZ = (z * QUAD_WIDTH) + (CHUNK_XYZ_LENGTH * chunk.getChunkZ() * QUAD_WIDTH);

        if (!top) voxelCube.createTop(vertices, renderX, renderY, renderZ, textureRegion);
        if (!bot) voxelCube.createBottom(vertices, renderX, renderY, renderZ, textureRegion);
        if (!lef) voxelCube.createLeft(vertices, renderX, renderY, renderZ, textureRegion);
        if (!rig) voxelCube.createRight(vertices, renderX, renderY, renderZ, textureRegion);
        if (!fro) voxelCube.createFront(vertices, renderX, renderY, renderZ, textureRegion);
        if (!bac) voxelCube.createBack(vertices, renderX, renderY, renderZ, textureRegion);
    }

    private void populateRampVertices(Chunk chunk, List<Vertex> vertices, int x, int y, int z, int worldX, int worldZ) {
        // Check neighboring blocks to determine which faces to cull
        boolean top = isSolid(worldX, y + 1, worldZ, FaceType.TOP);
        boolean bot = isSolid(worldX, y - 1, worldZ, FaceType.BOTTOM);
//        boolean lef = isSolid(worldX - 1, y, worldZ, FaceType.LEFT);
        boolean rig = isSolid(worldX + 1, y, worldZ, FaceType.RIGHT);
//        boolean fro = isSolid(worldX, y, worldZ - 1, FaceType.FRONT);
        boolean bac = isSolid(worldX, y, worldZ + 1, FaceType.BACK);

        // Scale the worldX and worldZ by QUAD_SIZE to avoid overlap
        float renderX = (x * QUAD_WIDTH) + (CHUNK_XYZ_LENGTH * chunk.getChunkX() * QUAD_WIDTH);
        float renderY = y * QUAD_HEIGHT;
        float renderZ = (z * QUAD_WIDTH) + (CHUNK_XYZ_LENGTH * chunk.getChunkZ() * QUAD_WIDTH);

        if (!top) voxelRamp.createTop(vertices, renderX, renderY, renderZ, textureRegion);
        if (!bot) voxelRamp.createBottom(vertices, renderX, renderY, renderZ, textureRegion);
        if (!rig) voxelRamp.createRight(vertices, renderX, renderY, renderZ, textureRegion);
        if (!bac) voxelRamp.createBack(vertices, renderX, renderY, renderZ, textureRegion);
        voxelRamp.createDiagonal(vertices, renderX, renderY, renderZ, textureRegion);
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
        VertexAttribute position = new VertexAttribute(VertexAttributes.Usage.Position, 3, ShaderProgram.POSITION_ATTRIBUTE);
        VertexAttribute textureCoordinates = new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE + "0");
        VertexAttribute normal = new VertexAttribute(VertexAttributes.Usage.Normal, 3, ShaderProgram.NORMAL_ATTRIBUTE);

        int numberComponents = (position.numComponents + normal.numComponents + textureCoordinates.numComponents);

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

        Mesh mesh = new Mesh(true, verticesArray.length, indicesArray.length, position, textureCoordinates, normal);
        mesh.setVertices(verticesArray);
        mesh.setIndices(indicesArray);

        return mesh;
    }

    private boolean isSolid(int worldX, int worldY, int worldZ, FaceType faceType) {
        if (worldY < 0) return false;
        if (worldY >= WORLD_HEIGHT) return false;

        int chunkX = (int) Math.floor(worldX / (float) CHUNK_XYZ_LENGTH);
        int chunkZ = (int) Math.floor(worldZ / (float) CHUNK_XYZ_LENGTH);
        Chunk chunk = chunkManager.getChunk(chunkX, chunkZ);
        if (chunk == null) return false;

        Block block = chunk.getWorldChunkBlock(worldX, worldY, worldZ);
        if (block == null) return false;

        BlockType blockType = block.getBlockType();
        if (blockType == BlockType.AIR) return false;
        if (blockType == BlockType.TRIANGULAR_PRISM_315) return false;
        return true;
    }
}
