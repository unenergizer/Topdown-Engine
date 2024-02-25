package com.forgestorm.topdown.state.voxel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.GLFrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.forgestorm.topdown.OrthographicPerspectiveRenderer;
import com.forgestorm.topdown.demoUtils.camera.TkPerspectiveCameraController;
import com.forgestorm.topdown.state.GameState;
import com.forgestorm.topdown.world.Chunk;
import com.forgestorm.topdown.world.ChunkManager;
import com.forgestorm.topdown.world.ChunkMeshGenerator;

import static com.badlogic.gdx.graphics.GL20.GL_DEPTH_TEST;
import static com.forgestorm.topdown.Main.loadShader;
import static com.forgestorm.topdown.Main.renderUtils;

public class VoxelGameState2 implements GameState {
    private final ChunkManager chunkManager = new ChunkManager();
    private final ChunkMeshGenerator chunkMeshGenerator = new ChunkMeshGenerator(chunkManager);
    private ShaderProgram shaderProgram;
    private Texture texture;
    private OrthographicCamera screenCamera;
    private OrthographicCamera gameCam;
    private FrameBuffer WorldFBO;
    private SpriteBatch batch;

    private int Scale = 4;
    private int screenWidth;
    private int screenHeight;
    private int WorldWidth;
    private int WorldHeight;
    private float WorldScaleLeftovers;

    boolean rendering2D = true;

    //This is just for debugging the 3D
    private PerspectiveCamera debugCamera;
    private TkPerspectiveCameraController camController;

    OrthographicPerspectiveRenderer renderer;

    @Override
    public void init() {
        shaderProgram = loadShader("shaders/skybox.vert", "shaders/skybox.frag");
        //Resize is ran after init, we do not need to setup cameras here
        System.out.println("Init TK");

        batch = new SpriteBatch();

        texture = renderUtils.getTexture("cube_top").getTexture();

        TextureAtlas.AtlasRegion top = renderUtils.getTexture("cube_top");
        chunkMeshGenerator.setTopRegion(top);
        TextureAtlas.AtlasRegion bottom = renderUtils.getTexture("cube_bottom");
        chunkMeshGenerator.setBottomRegion(bottom);

        TextureAtlas.AtlasRegion left = renderUtils.getTexture("cube_left");
        chunkMeshGenerator.setLeftRegion(left);
        TextureAtlas.AtlasRegion right = renderUtils.getTexture("cube_right");
        chunkMeshGenerator.setRightRegion(right);

        TextureAtlas.AtlasRegion front = renderUtils.getTexture("cube_front");
        chunkMeshGenerator.setFrontRegion(front);
        TextureAtlas.AtlasRegion back = renderUtils.getTexture("cube_back");
        chunkMeshGenerator.setBackRegion(back);


        // Move this...
        for (Chunk chunk : chunkManager.getChunks()) {
            System.out.println("Generating Chunk Mesh: " + chunk);
            chunkMeshGenerator.generateChunkMesh(chunk);
        }
    }

    @Override
    public void update() {

        //Translating the camera in a 2D
        if (rendering2D) {
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                renderer.translate(new Vector3(0, -1,0));
            }
            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                renderer.translate(new Vector3(0, 1,0));
            }
            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                renderer.translate(new Vector3(0, 0,-1));
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                renderer.translate(new Vector3(0, 0,1));
            }
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                renderer.translate(new Vector3(1, 0,0));
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                renderer.translate(new Vector3(-1, 0,0));
            }
            renderer.update();
        } else {
            camController.update();
            debugCamera.update();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            rendering2D = !rendering2D;
        }
    }

    /**
     * This is for drawing the world, with the standard pixel art scaling in the engine
     */
    public Texture drawWorld(SpriteBatch batch) {
        WorldFBO.begin();
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f, true);

        Gdx.gl.glEnable(GL_DEPTH_TEST);

        //Draw the mesh here using renderer camera
        for (Chunk chunk : chunkManager.getChunks()) {
            Mesh mesh = chunk.getChunkMesh();
            if (mesh == null) continue;

            if (rendering2D) {
                texture.bind();
                shaderProgram.bind();
                shaderProgram.setUniformi("u_texture", 0);
                shaderProgram.setUniformMatrix("u_projTrans", renderer.camera.combined);
                shaderProgram.setUniformf("u_modelPos", new Vector3(0,0,0));
            } else {
                texture.bind();
                shaderProgram.bind();
                shaderProgram.setUniformi("u_texture", 0);
                shaderProgram.setUniformMatrix("u_projTrans", debugCamera.combined);
                shaderProgram.setUniformf("u_modelPos", new Vector3(0,0,0));
            }

            mesh.render(shaderProgram, GL32.GL_TRIANGLES);
        }

        Gdx.gl.glDisable(GL_DEPTH_TEST);

        //This is the 2D rendering to use as reference when checking if the mesh looks correct
        batch.setProjectionMatrix(gameCam.combined);
        batch.begin();

        Vector3 mousePos = new Vector3(Gdx.input.getX(),Gdx.input.getY(), 0);
        gameCam.unproject(mousePos);
        batch.draw(renderUtils.getTexture("dirt"), (int) mousePos.x, (int) mousePos.y);
        batch.draw(renderUtils.getTexture("dirt"), (int) mousePos.x, (int) mousePos.y+16);

        batch.draw(renderUtils.getTexture("dirt"), 0, 0);
        batch.end();

        WorldFBO.end();
        WorldFBO.getColorBufferTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        return WorldFBO.getColorBufferTexture();
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f, true);
        Texture temp = drawWorld(batch);

        batch.setProjectionMatrix(screenCamera.combined);
        batch.begin();
        batch.draw(temp, 0, screenHeight, screenWidth, -screenHeight); //Drawing the FBO texture to screen, scaled up
        renderUtils.getFont().draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 0, screenHeight);
        renderUtils.getFont().draw(batch, "OrthographicPerspectiveRenderer: " + rendering2D, 0, screenHeight - 16);
        renderUtils.getFont().draw(batch, "Controls: ", 0, screenHeight - 32);
        renderUtils.getFont().draw(batch, "     Press E to switch to 3D debugging View", 0, screenHeight - 48);
        renderUtils.getFont().draw(batch, "         Controls similar to minecraft flight", 0, screenHeight - 64);
        renderUtils.getFont().draw(batch, "     Press G to swap GameStates", 0, screenHeight - 80);
        renderUtils.getFont().draw(batch, "         VoxelGameState", 0, screenHeight - 96);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        System.out.println("Resize TK");
        //Resize our screenSize Rendering
        Matrix4 matrix = new Matrix4();
        matrix.setToOrtho2D(0, 0, width, height);
        batch.setProjectionMatrix(matrix);
        if (screenCamera == null)
            screenCamera = new OrthographicCamera();
        screenCamera.setToOrtho(false, width, height);

        screenWidth = width;
        screenHeight = height;
        if (screenWidth <= 10) {
            screenWidth = 32;
        }

        if (screenHeight <= 10) {
            screenHeight = 32;
        }

        resizeGame(Scale);
    }

    private void resizeGame(int scale) {
        int Width = this.screenWidth;
        int Height = this.screenHeight;
        if (Width/scale <= 10) {
            Width = 32;
        }

        if (Height/scale <= 10) {
            Height = 32;
        }

        WorldScaleLeftovers = (((float) Height / scale) - (Height / scale))*scale;

        WorldWidth = Width / (scale);
        WorldHeight = Height / (scale);

        while (WorldHeight%2==1) {
            if (WorldScaleLeftovers >= 1) {
                WorldHeight += 1;
                WorldScaleLeftovers -= 1;
            } else {
                break;
            }
        }

        if (debugCamera == null) {
            debugCamera = new PerspectiveCamera(67, WorldWidth, WorldHeight);
            debugCamera.position.set(0, 0, 0);
            debugCamera.lookAt(0,0,0);
            debugCamera.near = 1f;
            debugCamera.far = 300f;
            camController = new TkPerspectiveCameraController(debugCamera);
            Gdx.input.setInputProcessor(camController);
        }

        GLFrameBuffer.FrameBufferBuilder frameBufferBuilder = new GLFrameBuffer.FrameBufferBuilder(WorldWidth, WorldHeight);
        frameBufferBuilder.addColorTextureAttachment(GL30.GL_RGBA8, GL30.GL_RGBA, GL30.GL_UNSIGNED_BYTE);
        frameBufferBuilder.addDepthTextureAttachment(GL20.GL_DEPTH_COMPONENT, GL20.GL_FLOAT);
        WorldFBO = frameBufferBuilder.build();

        if (gameCam == null)
            gameCam = new OrthographicCamera();
        gameCam.setToOrtho(false, WorldFBO.getWidth(), WorldFBO.getHeight());

        //3D Orthographic Camera
        if (renderer == null)
            renderer = new OrthographicPerspectiveRenderer(WorldWidth, WorldHeight);
        else
            renderer.resize(WorldFBO.getWidth(), WorldFBO.getHeight());
    }

    @Override
    public void dispose() {
        batch.dispose();
        WorldFBO.dispose();
    }

}
