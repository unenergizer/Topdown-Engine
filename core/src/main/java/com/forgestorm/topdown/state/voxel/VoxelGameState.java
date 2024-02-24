package com.forgestorm.topdown.state.voxel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.forgestorm.topdown.CameraUtils;
import com.forgestorm.topdown.demoUtils.camera.TkPerspectiveCameraController;
import com.forgestorm.topdown.input.CursorCatchedController;
import com.forgestorm.topdown.input.FirstPersonCameraController;
import com.forgestorm.topdown.input.FirstPersonMovementController;
import com.forgestorm.topdown.state.GameState;
import com.forgestorm.topdown.world.*;

import static com.badlogic.gdx.graphics.GL20.GL_DEPTH_TEST;
import static com.forgestorm.topdown.Main.loadShader;

public class VoxelGameState implements GameState {

    private final ChunkManager chunkManager = new ChunkManager();
    private final ChunkMeshGenerator chunkMeshGenerator = new ChunkMeshGenerator(chunkManager);
    private ShaderProgram shaderProgram;
    private Camera camera;
    private TkPerspectiveCameraController camController;
    private FirstPersonMovementController firstPersonMovementController;
    private Texture texture;

    Vector3 lightDirection;
    Color lightColor;


    private FrameBuffer WorldFBO;

    @Override
    public void init() {
        shaderProgram = loadShader("shaders/default.vert", "shaders/default.frag");

        camera = CameraUtils.createPerspectiveCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//        camController = new TkPerspectiveCameraController(camera);
//        Gdx.input.setInputProcessor(camController);
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(new CursorCatchedController());
        inputMultiplexer.addProcessor(new FirstPersonCameraController(camera));
        inputMultiplexer.addProcessor(firstPersonMovementController = new FirstPersonMovementController(camera));
        Gdx.input.setInputProcessor(inputMultiplexer);

        texture = new Texture(Gdx.files.internal("dirt.png"));

        chunkMeshGenerator.setTextureRegion(new TextureRegion(texture));

        // Move this...
        for (Chunk chunk : chunkManager.getChunks()) {
            System.out.println("Generating Chunk Mesh: " + chunk);
            chunkMeshGenerator.generateChunkMesh(chunk);
        }

        // Example light properties
        lightDirection = new Vector3(-1f, -1f, -1f).nor(); // Direction of the light
        lightColor = new Color(1f, 1f, 1f, 1f); // White light
    }

    @Override
    public void update() {
//        Gdx.input.setInputProcessor(camController);
//        camController.update();
        firstPersonMovementController.update();
        camera.update();
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f, true);

        Gdx.gl.glEnable(GL_DEPTH_TEST);

        // Bind texture if used
        texture.bind(0);
        shaderProgram.bind();
        shaderProgram.setUniformi("u_texture", 0);
        shaderProgram.setUniformMatrix("u_projTrans", camera.combined);

        //Unused for now, we can setup once mesh is generating correctly
//        shaderProgram.setUniformf("u_lightDirection", camera.position);
//        shaderProgram.setUniformf("u_lightColor", lightColor.r, lightColor.g, lightColor.b, lightColor.a);

        for (Chunk chunk : chunkManager.getChunks()) {
            Mesh mesh = chunk.getChunkMesh();
            if (mesh == null) continue;
            mesh.render(shaderProgram, GL32.GL_TRIANGLES);
        }

        int error = Gdx.gl.glGetError();
        if (error != GL20.GL_NO_ERROR) System.out.println("OpenGL Error: " + error);

        Gdx.gl.glDisable(GL_DEPTH_TEST);
    }

    @Override
    public void dispose() {
        texture.dispose();
        chunkManager.dispose();
        shaderProgram.dispose();
    }

}
