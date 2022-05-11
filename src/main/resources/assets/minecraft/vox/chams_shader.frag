#version 120

uniform sampler2D u_texture;
uniform float u_alpha;
uniform int u_coloring;
uniform vec3 u_color;

void main() {
    vec4 texture = texture2D(u_texture, gl_TexCoord[0].st);
    if (texture.a == 0) {
        gl_FragColor = vec4(0);
        return;
    }
    if (u_coloring == 0) {
        gl_FragColor = vec4(texture.rgb, u_alpha);
        return;
    }
    gl_FragColor = vec4(u_color, u_alpha);
}
