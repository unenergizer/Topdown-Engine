package com.forgestorm.topdown.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL32;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.badlogic.gdx.graphics.GL20.GL_CULL_FACE;
import static com.badlogic.gdx.graphics.GL20.GL_DEPTH_TEST;

public class ChunkManager implements Disposable {

    /**
     * The length, width, and height of a chunk in blocks. Min value 2.
     */
    public static final int CHUNK_SIZE = 16;

    private final Map<Integer, Chunk> chunkConcurrentMap = new HashMap<>();
    private final ChunkMeshGenerator chunkMeshGenerator;

    private final ShaderProgram shaderProgram;
    private final Texture texture;

    private final int width;
    private final int height;
    private final int depth;

    public int worldHeight;

    /**
     * Overload of contructor that uses the default shader
     * @param sourceTex The source texture is the texture for the entire atlas, the tileset
     */
    public ChunkManager(int w, int d, int h, Texture sourceTex) {
        this(w,d,h,sourceTex, null); //TODO add default shader method
        //Default shader should or should not have the light code? Probably not, this question is why I postponed
    }

    /**
     *
     * @param sourceTex The source texture is the texture for the entire atlas, the tileset
     * @param shader the shader used to get
     */
    public ChunkManager(int w, int d, int h, Texture sourceTex, ShaderProgram shader) {
        this.texture = sourceTex;
        this.shaderProgram = shader;

        this.width = w;
        this.depth = d;
        this.height = h;
        this.worldHeight = height*CHUNK_SIZE;

        //This code needs to be moved
        makeTestChunks(width, depth, height);
        //verifyChunks();

        chunkMeshGenerator = new ChunkMeshGenerator(this);
    }

    public void generateAllMeshes() {
        for (Chunk chunk : getChunks()) {
            System.out.println("Generating Chunk Mesh: " + chunk);
            chunkMeshGenerator.generateChunkMesh(chunk);
        }
    }

    public void draw(Camera cam) {
        Gdx.gl.glEnable(GL_DEPTH_TEST);
        Gdx.gl.glEnable(GL_CULL_FACE);

        //Draw the mesh here using renderer camera
        for (Chunk chunk : getChunks()) {
            for (int i = 0; i < chunk.getChunkSections().length; i++) {
                Mesh mesh = chunk.getChunkSections()[i].getChunkMesh();
                if (mesh == null) {
                    continue;
                }

                texture.bind();
                shaderProgram.bind();
                shaderProgram.setUniformi("u_texture", 0);
                shaderProgram.setUniformMatrix("u_projTrans", cam.combined);
                shaderProgram.setUniformf("u_lightPos", cam.position);
                shaderProgram.setUniformf("u_modelPos", new Vector3(CHUNK_SIZE * chunk.getChunkX(), CHUNK_SIZE * chunk.getChunkSections()[i].getChunkY(),CHUNK_SIZE * chunk.getChunkZ()));
                mesh.render(shaderProgram, GL32.GL_TRIANGLES);
            }
        }

        Gdx.gl.glDisable(GL_CULL_FACE);
        Gdx.gl.glDisable(GL_DEPTH_TEST);
    }

    private void verifyChunks() {
        System.out.println("Total Chunks: " + chunkConcurrentMap.size());
        for (Chunk chunk : getChunks()) {
            for (int chunkSection = 0; chunkSection < height; chunkSection++) {
                int nonZeroTiles = 0;
                for (int x = 0; x < CHUNK_SIZE; x++) {
                    for (int y = 0; y < CHUNK_SIZE; y++) {
                        for (int z = 0; z < CHUNK_SIZE; z++) {
                            Block block = chunk.getLocalChunkBlock(x, z, y, chunkSection);
                            if (block.getBlockType() != BlockType.AIR) nonZeroTiles++;
                        }
                    }
                }
                System.out.println("nonZeroTiles: " + nonZeroTiles);
            }
        }
    }

    private void makeTestChunks(int width, int depth, int height) {
        // TESTING:
        for (int chunkX = 0; chunkX < width; chunkX++) {
            for (int chunkZ = 0; chunkZ < depth; chunkZ++) {
                //Create chunk
                Chunk chunk = new Chunk(chunkX, chunkZ, height);
                setChunk(chunk);

                //Fill chunk with blocks
                for (int chunkSection = 0; chunkSection < height; chunkSection++) {
                    for (int x = 0; x < CHUNK_SIZE; x++) {
                        for (int z = 0; z < CHUNK_SIZE; z++) {
                            for (int y = 0; y < CHUNK_SIZE; y++) {
                                Block volume = chunk.getLocalChunkBlock(x, y, z, chunkSection);

                                if (x*2 + (chunkX + 1) > y && z*2 + (chunkZ + 1) > y)
                                    volume.setBlockType(BlockType.RAMP_W);
                                else
                                    volume.setBlockType(BlockType.AIR);
                            }
                        }
                    }
                }
            }
        }
    }

    public Block getWorldChunkBlock(int worldX, int worldY, int worldZ) {
        int chunkX = (int) Math.floor(worldX / (float) CHUNK_SIZE);
        int chunkZ = (int) Math.floor(worldZ / (float) CHUNK_SIZE);

        Chunk chunk = getChunk(chunkX, chunkZ);

        if (chunk != null)
            return chunk.getWorldChunkBlock(worldX, worldY, worldZ);
        else return null;
    }

    public void setWorldChunkBlock(int worldX, int worldY, int worldZ) {
        int chunkX = (int) Math.floor(worldX / (float) CHUNK_SIZE);
        int chunkZ = (int) Math.floor(worldZ / (float) CHUNK_SIZE);

        Chunk chunk = getChunk(chunkX, chunkZ);
        if (chunk == null) {
            chunk = new Chunk(chunkX, chunkZ, height);
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

    /**
     * This limits the number of chunks that can be stored in the manager to 1024, this can be changed if needed
     */
    private int getChunkHashCode(int chunkX, int chunkZ) {
        return chunkX * 1024 + chunkZ;
    }

    public Collection<Chunk> getChunks() {
        return chunkConcurrentMap.values();
    }

    @Override
    public void dispose() {
        for (Chunk chunk : chunkConcurrentMap.values()) chunk.dispose();
        shaderProgram.dispose();
        //texture is just a reference, we do not need to dispose of here
    }
}
