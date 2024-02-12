package com.forgestorm.topdown.state.tk;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.GLFrameBuffer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.forgestorm.topdown.OrthographicPerspectiveRenderer;
import com.forgestorm.topdown.state.GameState;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static com.forgestorm.topdown.Main.renderUtils;

public class TkGameState implements GameState {

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

    OrthographicPerspectiveRenderer renderer;
    TkMapMeshRenderer mapRenderer;

    @Override
    public void init() {
        //Resize is ran after init, we do not need to setup cameras here
        System.out.println("Init TK");
        batch = new SpriteBatch();
        mapRenderer = new TkMapMeshRenderer();
    }

    @Override
    public void update() {

        //Translating the camera in a 2D
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            renderer.translate(new Vector2(0, 1));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            renderer.translate(new Vector2(0, -1));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            renderer.translate(new Vector2(-1, 0));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            renderer.translate(new Vector2(1, 0));
        }

        renderer.update();
    }

    /**
     * This is for drawing the world, with the standard pixel art scaling in the engine
     */
    public Texture drawWorld(SpriteBatch batch) {
        WorldFBO.begin();
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        //Draw the mesh here using renderer camera
        mapRenderer.render(renderer.camera);

        //This is the 2D rendering to use as reference when checking if the mesh looks correct
        batch.setProjectionMatrix(gameCam.combined);
        batch.begin();
        batch.draw(renderUtils.getTexture("dirt"),0,0);
        batch.end();

        WorldFBO.end();
        WorldFBO.getColorBufferTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        return WorldFBO.getColorBufferTexture();
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f,0.15f,0.2f,1f, true);
        Texture temp = drawWorld(batch);

        batch.setProjectionMatrix(screenCamera.combined);
        batch.begin();
        batch.draw(temp, 0, screenHeight, screenWidth, -screenHeight); //Drawing the FBO texture to screen, scaled up
        renderUtils.getFont().draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 0, Gdx.graphics.getHeight());
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
