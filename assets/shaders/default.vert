attribute vec4 a_position;
attribute vec2 a_texCoord0;
attribute vec3 a_normal;

uniform mat4 u_projTrans;
uniform mat4 u_modelViewMatrix;
uniform mat3 u_normalMatrix;

varying vec2 v_texCoords;
varying vec3 v_normal;

void main() {
    v_texCoords = a_texCoord0;
    v_normal = normalize(u_normalMatrix * a_normal);
    gl_Position = u_projTrans * a_position;
}
