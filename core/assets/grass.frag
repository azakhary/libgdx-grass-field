#ifdef GL_ES
precision mediump float;
#endif

varying vec2 v_texCoord;
varying vec3 v_pos;

uniform sampler2D u_noise;
uniform sampler2D u_texture;
uniform vec4 u_ambientColor;
uniform float u_time;
uniform float u_camera;

void main() {
    vec4 grassColor = texture2D(u_texture, v_texCoord);
    vec4 noiseColor = texture2D(u_noise, v_texCoord);

    vec4 color = grassColor * noiseColor;
    color.rgb = color.rgb * 1.7;
    color.a = grassColor.a;

    float far = distance(u_camera, v_pos);

    gl_FragColor.a = color.a;
    gl_FragColor.rgb = color.rgb * u_ambientColor;

    //gl_FragColor.r = gl_FragColor.r + (sin(u_time)+1.0)*(1.0/6.0);
    //gl_FragColor.g = gl_FragColor.g + (cos(u_time)+1.0)*(1.0/6.0);
    //gl_FragColor.b = gl_FragColor.b + (cos(u_time)+1.0)*(1.0/6.0);

    if( gl_FragColor.a < 0.5 + noiseColor.r * 0.3) {
        discard;
    } else {
        gl_FragColor.a = mix(gl_FragColor.a, 0, far/25.0);
        gl_FragColor.a = gl_FragColor.a*(1.0/2.2);
    }

}