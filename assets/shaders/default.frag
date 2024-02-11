#ifdef GL_ES
precision mediump float;
#endif

varying vec2 v_texCoords;
varying vec3 v_normal;

uniform sampler2D u_texture;
uniform vec3 u_lightDirection;
uniform vec4 u_lightColor;

void main() {
    vec4 texColor = texture2D(u_texture, v_texCoords);
    float lightIntensity = max(dot(normalize(v_normal), normalize(u_lightDirection)), 0.0);
    vec4 lightEffect = u_lightColor * lightIntensity;
    gl_FragColor = texColor; //* lightEffect;
}
