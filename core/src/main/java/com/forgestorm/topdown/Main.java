package com.forgestorm.topdown;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.forgestorm.topdown.world.Chunk;
import com.forgestorm.topdown.world.ChunkManager;
import com.forgestorm.topdown.world.ChunkMeshGenerator;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends ApplicationAdapter {
    private final ChunkManager chunkManager = new ChunkManager();
    private final ChunkMeshGenerator chunkMeshGenerator = new ChunkMeshGenerator(chunkManager);
    private ShaderProgram shaderProgram;
    private Camera camera;
    private Texture texture;

    @Override
    public void create() {

        shaderProgram = loadShader("default.vert", "default.frag");

        camera = CameraUtils.createPerspectiveCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        texture = new Texture(Gdx.files.internal("dirt.png"));

        chunkMeshGenerator.setTextureRegion(new TextureRegion(texture));

        // Move this...
        for (Chunk chunk : chunkManager.getChunks()) {
            System.out.println("Generating Chunk Mesh: " + chunk);
            chunkMeshGenerator.generateChunkMesh(chunk);
        }

        // Example light properties
        Vector3 lightDirection = new Vector3(-1f, -1f, -1f).nor(); // Direction of the light
        Color lightColor = new Color(1f, 1f, 1f, 1f); // White light


        // Set the uniform variables for the light
        shaderProgram.setUniformf("u_lightDirection", lightDirection);
        shaderProgram.setUniformf("u_lightColor", lightColor.r, lightColor.g, lightColor.b, lightColor.a);
    }

    @Override
    public void render() {
        camera.update();
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.2f, 1f);
        Gdx.gl.glClear(GL32.GL_COLOR_BUFFER_BIT);

        shaderProgram.bind();
        // Bind texture if used
        texture.bind(0);
        shaderProgram.setUniformi("u_texture", 0);
        shaderProgram.setUniformMatrix("u_projTrans", camera.combined);

        for (Chunk chunk : chunkManager.getChunks()) {
            Mesh mesh = chunk.getChunkMesh();
            if (mesh == null) continue;
            mesh.render(shaderProgram, GL32.GL_TRIANGLES);
        }

        int error = Gdx.gl.glGetError();
        if (error != GL20.GL_NO_ERROR) {
            System.out.println("OpenGL Error: " + error);
        }
    }

    @Override
    public void dispose() {
        texture.dispose();
        chunkManager.dispose();
        shaderProgram.dispose();
    }

    public ShaderProgram loadShader(String vert, String frag) {
        ShaderProgram temp = new ShaderProgram(Gdx.files.internal(vert), Gdx.files.internal(frag));

        if (temp.isCompiled()) {
            System.out.println("Shader Loaded");
            return temp;
        } else {
            System.out.println("Did not swap shader, failed to compile due to \n" + temp.getLog());
            return null;
        }
    }
}
