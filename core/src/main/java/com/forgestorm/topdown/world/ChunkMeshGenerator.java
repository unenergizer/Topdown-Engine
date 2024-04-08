package com.forgestorm.topdown.world;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.forgestorm.topdown.GameConstants.Chunk.*;

public class ChunkMeshGenerator {

    private final VoxelCube voxelCube = new VoxelCube();
    private final VoxelRamp voxelRamp = new VoxelRamp();

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
                        case TRIANGULAR_PRISM_NE:
                        case TRIANGULAR_PRISM_SE:
                        case TRIANGULAR_PRISM_SW:
                        case TRIANGULAR_PRISM_NW:
                            populateTriangularPrismVertices(vertices, x, y, z, volume);
                            break;
                        case RAMP_N:
                        case RAMP_E:
                        case RAMP_S:
                        case RAMP_W:
                            populateTriangularPrismVertices(vertices, x, y, z, volume);
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

        if (!top) voxelCube.createTop(vertices, x, y, z, block.getTopRegion());
        if (!bot) voxelCube.createBottom(vertices, x, y, z, block.getBottomRegion());
        if (!lef) voxelCube.createLeft(vertices, x, y, z, block.getLeftRegion());
        if (!rig) voxelCube.createRight(vertices, x, y, z, block.getRightRegion());
        if (!fro) voxelCube.createFront(vertices, x, y, z, block.getFrontRegion());
        if (!bac) voxelCube.createBack(vertices, x, y, z, block.getBackRegion());
    }

    private void populateTriangularPrismVertices(List<Vertex> vertices, int x, int y, int z, Block block) {
        // Check neighboring blocks to determine which faces to cull
        boolean top = isSolid(block.getWorldX(), y + 1, block.getWorldZ());
        boolean bot = isSolid(block.getWorldX(), y - 1, block.getWorldZ());
        boolean lef = true;
        boolean rig = true;
        boolean fro = true;
        boolean bac = true;

        switch (block.getBlockType()) {
            case TRIANGULAR_PRISM_NE:
                lef = isSolid(block.getWorldX() - 1, y, block.getWorldZ());
                bac = isSolid(block.getWorldX(), y, block.getWorldZ() + 1);
                voxelRamp.createDiagonalNE(vertices, x, y, z, block.getFrontRegion());

                if (!top) voxelRamp.createTopNE(vertices, x, y, z, block.getTopRegion());
                if (!bot) voxelRamp.createBottomNE(vertices, x, y, z, block.getBottomRegion());
                if (!lef) voxelCube.createLeft(vertices, x, y, z, block.getLeftRegion());
                if (!bac) voxelCube.createBack(vertices, x, y, z, block.getBackRegion());

                break;
            case TRIANGULAR_PRISM_SE:
                lef = isSolid(block.getWorldX() - 1, y, block.getWorldZ());
                fro = isSolid(block.getWorldX(), y, block.getWorldZ() - 1);
                voxelRamp.createDiagonalSE(vertices, x, y, z, block.getBackRegion());

                if (!top) voxelRamp.createTopSE(vertices, x, y, z, block.getTopRegion());
                if (!bot) voxelRamp.createBottomSE(vertices, x, y, z, block.getBottomRegion());
                if (!lef) voxelCube.createLeft(vertices, x, y, z, block.getLeftRegion());
                if (!fro) voxelCube.createFront(vertices, x, y, z, block.getFrontRegion());

                break;
            case TRIANGULAR_PRISM_SW:
                rig = isSolid(block.getWorldX() + 1, y, block.getWorldZ());
                fro = isSolid(block.getWorldX(), y, block.getWorldZ() - 1);
                voxelRamp.createDiagonalSW(vertices, x, y, z, block.getBackRegion());

                if (!top) voxelRamp.createTopSW(vertices, x, y, z, block.getTopRegion());
                if (!fro) voxelRamp.createBottomSW(vertices, x, y, z, block.getBottomRegion());
                if (!rig) voxelCube.createRight(vertices, x, y, z, block.getRightRegion());
                if (!fro) voxelCube.createFront(vertices, x, y, z, block.getFrontRegion());

                break;
            case TRIANGULAR_PRISM_NW:
                rig = isSolid(block.getWorldX() + 1, y, block.getWorldZ());
                bac = isSolid(block.getWorldX(), y, block.getWorldZ() + 1);
                voxelRamp.createDiagonalNW(vertices, x, y, z, block.getFrontRegion());

                if (!top) voxelRamp.createTopNW(vertices, x, y, z, block.getTopRegion());
                if (!bot) voxelRamp.createBottomNW(vertices, x, y, z, block.getBottomRegion());
                if (!rig) voxelCube.createRight(vertices, x, y, z, block.getRightRegion());
                if (!bac) voxelCube.createBack(vertices, x, y, z, block.getBackRegion());

                break;
        }
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
        /*
        We should be checking for ramps and prisms here as well to cull faces.
        Instead, we do not check them, the culling to work for those shapes they would need more data on if the types match the block adjacent,
        because of this we will make the easier and less efficient decision to treat ramps the same as AIR.
         */
        return blockType == BlockType.BLOCK;
    }

    //We keep these around so that we don't create lots of garbage for each calculation
    private static final Vector3 cacheA = new Vector3();
    private static final Vector3 cacheB = new Vector3();
    public static Vector3 generateNormals(Vertex p2, Vertex p1, Vertex p3) {
        Vector3 normal = new Vector3();
        cacheA.set(p2.getX()- p1.getX(), p2.getY()- p1.getY(), p2.getZ()- p1.getZ());
        cacheB.set(p3.getX()- p1.getX(), p3.getY()- p1.getY(), p3.getZ()- p1.getZ());
        normal.set(
            cacheA.y * cacheB.z - cacheA.z * cacheB.y,
            cacheA.z * cacheB.x - cacheA.x * cacheB.z,
            cacheA.x * cacheB.y - cacheA.y * cacheB.x
        );

        p1.setNx(normal.x);
        p1.setNy(normal.y);
        p1.setNz(normal.z);
        p2.setNx(normal.x);
        p2.setNy(normal.y);
        p2.setNz(normal.z);
        p3.setNx(normal.x);
        p3.setNy(normal.y);
        p3.setNz(normal.z);

        return normal;
    }
}
