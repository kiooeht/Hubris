//SpriteBatch will use texture unit 0
uniform sampler2D u_texture;

//"in" varyings from our vertex shader
varying vec4 v_color;
varying vec2 v_texCoord;

uniform float white;
uniform float fadeIn;

void main() {
    //sample the texture
    vec4 texColor = texture2D(u_texture, v_texCoord);
    //texColor.a = min(texColor.a, v_color.a);
    texColor *= v_color * 0.75;
    texColor = vec4(mix(texColor.rgb, v_color.rgb, white), texColor.a);

    texColor.a *= fadeIn;
    
    float gray = dot(texColor.rgb, vec3(0.299, 0.587, 0.114));
    vec4 grayColor = vec4(gray, gray, gray, texColor.a);

    //final color
    gl_FragColor = texColor;
}
