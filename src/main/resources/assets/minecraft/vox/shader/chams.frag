#version 120

uniform sampler2D u_texture;
uniform float u_alpha;
uniform int u_coloring;
uniform vec3 u_color;
uniform float u_mixin;

void main() {
    if (u_coloring == 0) {
        vec4 texture = texture2D(u_texture, gl_TexCoord[0].st);
        if (texture.a == 0) {
            gl_FragColor = vec4(0);
            return;
        }

        gl_FragColor = vec4(texture.rgb, u_alpha);
    } else {
        vec4 centerCol = texture2D(u_texture, gl_TexCoord[0].xy);
        if (centerCol.a == 0) {
            gl_FragColor = vec4(0);
            return;
        }

        gl_FragColor = vec4(mix(u_color.rgb, centerCol.rgb, u_mixin), u_alpha);
    }
}
