package com.forgestorm.topdown.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

import static com.badlogic.gdx.graphics.GL20.GL_DEPTH_TEST;

/**
 * This class handles the camera and rendering
 */
public class OrthographicPerspectiveRenderer implements Disposable {

    public OrthographicCamera camera;
    private Vector3 focusPoint = new Vector3();

    int width;
    int height;

    private Mesh debuggingFrustumMesh;
    private ShaderProgram debugShader;

    public OrthographicPerspectiveRenderer(int width, int height) {
        camera = new OrthographicCamera();
        this.width = width;
        this.height = height;
        reCalcCamera(true);
    }

    public void translate(Vector3 translation) {
        if (translation.z > 0) {
            camera.position.z += distort(translation.z);
        }
        if (translation.z < 0) {
            camera.position.z -= distort(translation.z * -1);
        }
        if (translation.y > 0) {
            camera.position.y -= distort(translation.y);
        }
        if (translation.y < 0) {
            camera.position.y += distort(translation.y * -1);
        }
        if (translation.x < 0) {
            camera.position.x += translation.x * -1;
        }
        if (translation.x > 0) {
            camera.position.x -= translation.x;
        }
    }

    public void update() {
        camera.update();
    }

    private void reCalcCamera(boolean updateFocus) {
        camera.setToOrtho(false, width, height);
        camera.rotate(new Quaternion().setEulerAngles(0,-45,0));
        camera.far = 300f;
        camera.update();
        Vector3[] points = camera.frustum.planePoints;
        float m = (points[4].y - points[0].y) / (points[4].z - points[0].z);
        float cameraYOffset = points[0].z * m - points[0].y;
        camera.position.set(focusPoint.x,focusPoint.y + cameraYOffset, focusPoint.z);
        camera.update();

        if (updateFocus) {
            focusPoint = new Vector3((camera.viewportWidth / 2), (camera.viewportHeight / 2), 0);
            reCalcCamera(false);
        }
    }

    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
        reCalcCamera(false);
    }

    public static float distort(float val) {
        return val + (val * ((float) Math.sqrt(2) - 1f));
    }

    public void drawDebuggingFrustumMesh(Camera debugCamera) {
        if (debuggingFrustumMesh == null) {
            createFrustumMesh();
        }

        Gdx.gl.glEnable(GL_DEPTH_TEST);
        Gdx.gl.glEnable(GL20.GL_BLEND);

        debugShader.bind();
        debugShader.setUniformMatrix("u_projTrans", debugCamera.combined);
        debuggingFrustumMesh.render(debugShader, GL32.GL_TRIANGLES);

        Gdx.gl.glDisable(GL20.GL_BLEND);
        Gdx.gl.glDisable(GL_DEPTH_TEST);
    }

    public void createFrustumMesh() {
        if (debugShader == null) {
            debugShader = new ShaderProgram(
                "attribute vec4 a_position;\n" +
                    "attribute vec4 a_color;\n" +
                    "uniform mat4 u_projTrans;\n" +
                    "varying vec4 v_color;\n" +
                    "void main() {\n" +
                    "    v_color = a_color;\n" +
                    "    gl_Position = u_projTrans * a_position;\n" +
                    "}",
                "varying vec4 v_color;\n" +
                    "void main() {\n" +
                    "    gl_FragColor = v_color;\n" +
                    "}");
        }

        if (debuggingFrustumMesh != null)
            debuggingFrustumMesh.dispose();

        debuggingFrustumMesh = new Mesh(true, 8, 6*6, VertexAttribute.Position(), VertexAttribute.ColorUnpacked());

        float[] vertices = new float[8*7];

        //Bottom
        //4P------5T
        //|      |
        //|      |
        //0O------1G
        //Top
        //7Y------6P
        //|      |
        //|      |
        //3R------2B

        int verts = 0;
        for (int i = 0; i < camera.frustum.planePoints.length; i++) {
            System.out.println("point " + i + " " + camera.frustum.planePoints[i]);
            vertices[verts] = camera.frustum.planePoints[i].x;
            vertices[verts+1] = camera.frustum.planePoints[i].y;
            vertices[verts+2] = camera.frustum.planePoints[i].z;
            vertices[verts+3] = i < camera.frustum.planePoints.length/2? 0.3f: 0.8f;
            vertices[verts+4] = i < camera.frustum.planePoints.length/2? 0.7f: 0.5f;
            vertices[verts+5] = i < camera.frustum.planePoints.length/2? 0.5f: 0.3f;
            vertices[verts+6] = i < camera.frustum.planePoints.length/2? 0.5f: 1f;
            verts += 7;
        }
        debuggingFrustumMesh.setVertices(vertices);

        short[] triangles = new short[6*6];
        //Bottom
        triangles[0] = 0;
        triangles[1] = 1;
        triangles[2] = 5;

        triangles[3] = 5;
        triangles[4] = 4;
        triangles[5] = 0;

        //Top
        triangles[6] = 3;
        triangles[7] = 2;
        triangles[8] = 6;

        triangles[9] = 6;
        triangles[10] = 7;
        triangles[11] = 3;

        //Left
        triangles[12] = 4;
        triangles[13] = 0;
        triangles[14] = 3;

        triangles[15] = 3;
        triangles[16] = 7;
        triangles[17] = 4;

        //Right
        triangles[18] = 1;
        triangles[19] = 5;
        triangles[20] = 6;

        triangles[21] = 6;
        triangles[22] = 2;
        triangles[23] = 1;

        //Front
        triangles[24] = 0;
        triangles[25] = 1;
        triangles[26] = 2;

        triangles[27] = 2;
        triangles[28] = 3;
        triangles[29] = 0;

        //Back
        triangles[30] = 4;
        triangles[31] = 5;
        triangles[32] = 6;

        triangles[33] = 6;
        triangles[34] = 7;
        triangles[35] = 4;

        debuggingFrustumMesh.setIndices(triangles);
    }

    @Override
    public void dispose() {
        if (debuggingFrustumMesh != null)
            debuggingFrustumMesh.dispose();
        if (debugShader != null)
            debugShader.dispose();
    }
}
