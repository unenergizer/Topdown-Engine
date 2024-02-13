package com.forgestorm.topdown.state.tk;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;

import static com.forgestorm.topdown.Main.renderUtils;
import static com.forgestorm.topdown.state.tk.TkMapMeshUtils.*;

public class TkMapMeshRenderer {

    private Mesh tilesMesh;
    private final int mS = 16; //MeshSize
    private Texture skyboxTexture;
    private ShaderProgram skyShader;

    public TkMapMeshRenderer() {
        createSkyBox();
    }

    private void createSkyBox() {
        skyShader = loadShader("shaders/skybox.vert", "shaders/skybox.frag");
        tilesMesh = new Mesh(true, 10000, 0, VertexAttribute.Position(), VertexAttribute.TexCoords(0), VertexAttribute.Normal());

        //0,0,0,   //0
        //1,0,0,   //1
        //1,0,1,   //2
        //0,0,1,   //3
        //0,1,0,   //4
        //1,1,0,   //5
        //1,1,1,   //6
        //0,1,1,   //7

        skyboxTexture = renderUtils.getTexture("Proto-000").getTexture();

        float[] vertices = new float[10000*8];
        int vertIndex = 0;

        boolean flatGrid = false;
        boolean topTiles = true;
        boolean frontTiles = true;
        boolean frontLeftDiagonal = true;
        boolean frontRightDiagonal = true;
        boolean topLeftRamp = true;
        boolean topRightRamp = true;
        boolean topTriangles = true;
        boolean frontTriangles = true;

        if (flatGrid) {
            for (int x = 0; x < 25; x++) {
                for (int y = 0; y < 25; y++) {
                    float[] tempVerts = getTopTileVertices(x * mS, 0, -y * (mS), mS, renderUtils.getTexture("Proto-014"));
                    for (int t = 0; t < tempVerts.length; t++) {
                        vertices[vertIndex] = tempVerts[t];
                        vertIndex++;
                    }
                }
            }
        }

        if (topTiles) {
            for (int i = 0; i < 25; i++) {
                float[] tempVerts = getTopTileVertices(i * mS, i * (mS / 2f), -i * (mS / 2f), mS, renderUtils.getTexture("Proto-010"));
                for (int t = 0; t < tempVerts.length; t++) {
                    vertices[vertIndex] = tempVerts[t];
                    vertIndex++;
                }
            }
        }

        if (frontTiles) {
            for (int i = 0; i < 25; i++) {
                float[] tempVerts = getFrontTileVertices((i + 1) * mS, i * (mS / 2f), -i * (mS / 2f), mS, renderUtils.getTexture("Proto-058"));
                for (int t = 0; t < tempVerts.length; t++) {
                    vertices[vertIndex] = tempVerts[t];
                    vertIndex++;
                }
            }
        }

        if (frontLeftDiagonal) {
            float[] tempVerts = getFrontLeftDiagonalTileVertices(mS*2, 2*(mS/2f), -2*(mS/2f), mS, renderUtils.getTexture("Proto-239"));
            for (int t = 0; t < tempVerts.length; t++) {
                vertices[vertIndex] = tempVerts[t];
                vertIndex++;
            }
        }

        if (frontRightDiagonal) {
            float[] tempVerts = getFrontRightDiagonalTileVertices(mS*4, 2*(mS/2f), -2*(mS/2f), mS, renderUtils.getTexture("Proto-239"));
            for (int t = 0; t < tempVerts.length; t++) {
                vertices[vertIndex] = tempVerts[t];
                vertIndex++;
            }
        }

        if (topLeftRamp) {
            float[] tempVerts = getTopLeftRampTileVertices(mS*5, 6*(mS/2f), -2*(mS/2f), mS, renderUtils.getTexture("Proto-239"));
            for (int t = 0; t < tempVerts.length; t++) {
                vertices[vertIndex] = tempVerts[t];
                vertIndex++;
            }
        }

        if (topRightRamp) {
            float[] tempVerts = getTopRightRampTileVertices(mS*6, 6*(mS/2f), -2*(mS/2f), mS, renderUtils.getTexture("Proto-239"));
            for (int t = 0; t < tempVerts.length; t++) {
                vertices[vertIndex] = tempVerts[t];
                vertIndex++;
            }
        }

        if (topTriangles) {
            for (int i = 0; i < 4; i++) {
                float[] tempVerts = getTopTriangleTileVertices(i, mS * 2, (6 + (i * 2)) * (mS / 2f), -2 * (mS / 2f), mS, renderUtils.getTexture("Proto-014"));
                for (int t = 0; t < tempVerts.length; t++) {
                    vertices[vertIndex] = tempVerts[t];
                    vertIndex++;
                }
            }
        }

        if (frontTriangles) {
            for (int i = 0; i < 4; i++) {
                float[] tempVerts = getFrontTriangleTileVertices(i, mS * 3, (6 + (i * 2)) * (mS / 2f), -2 * (mS / 2f), mS, renderUtils.getTexture("Proto-014"));
                for (int t = 0; t < tempVerts.length; t++) {
                    vertices[vertIndex] = tempVerts[t];
                    vertIndex++;
                }
            }
        }

        tilesMesh.setVertices(vertices);
    }

    public void render(Camera camera) {
        skyboxTexture.bind();
        skyShader.bind();
        skyShader.setUniformi("u_texture", 0);
        skyShader.setUniformMatrix("u_projTrans", camera.combined);
        skyShader.setUniformf("u_modelPos", new Vector3(0,0,0));

        tilesMesh.render(skyShader, GL32.GL_TRIANGLES);
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
