package com.forgestorm.topdown.test.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.GLFrameBuffer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.forgestorm.topdown.world.OrthographicPerspectiveRenderer;
import com.forgestorm.topdown.test.demoUtils.camera.TkPerspectiveCameraController;
import com.forgestorm.topdown.test.state.GameState;
import com.forgestorm.topdown.world.ChunkManager;

import static com.forgestorm.topdown.test.Main.loadShader;
import static com.forgestorm.topdown.test.Main.renderUtils;

public class VoxelGameState2 implements GameState {
    private ChunkManager chunkManager;
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
        //Resize is ran after init, we do not need to setup cameras here
        System.out.println("Init VoxelGameState");

        batch = new SpriteBatch();

        chunkManager = new ChunkManager(5,5, 5, renderUtils.getTexture("cube_top").getTexture(), loadShader("shaders/chunk.vert", "shaders/chunk.frag"));
        chunkManager.generateAllMeshes();
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
            //3D Debug Camera
            camController.update();
            debugCamera.update();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            //Switches from 2D to 3D
            rendering2D = !rendering2D;
            if (!rendering2D)
                renderer.createFrustumMesh(); //Create a new mesh for the cameras position at time of switch
        }
    }

    /**
     * This is for drawing the world, with the standard pixel art scaling in the engine
     */
    public Texture drawWorld(SpriteBatch batch) {
        WorldFBO.begin();
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f, true);

        if (rendering2D) {
            chunkManager.draw(renderer.camera);
        } else {
            chunkManager.draw(debugCamera);
            renderer.drawDebuggingFrustumMesh(debugCamera);
        }

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

        //Return the output texture to be scaled up to screen size
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
            debugCamera.far = 10000f;
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
        chunkManager.dispose();
        renderer.dispose();
    }

}
