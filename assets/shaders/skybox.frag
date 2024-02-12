varying vec2 v_texCoords;
varying vec3 v_normal;
uniform sampler2D u_texture;

void main() {
    vec4 tgt = texture2D(u_texture, v_texCoords);
    gl_FragColor = tgt;
}