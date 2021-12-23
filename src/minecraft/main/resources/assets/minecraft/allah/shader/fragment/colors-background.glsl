/*
 * Original shader from: https://www.shadertoy.com/view/styGRW
 */

#ifdef GL_ES
precision mediump float;
#endif

// glslsandbox uniforms
uniform float time;
uniform vec2 resolution;

// shadertoy emulation
#define iTime time
#define iResolution resolution

// Emulate a black texture
#define texture(s, uv) vec4(0.0)

// --------[ Original ShaderToy begins here ]---------- //
//#define ATMOSHPERE

void mainImage( out vec4 fragColor, in vec2 fragCoord )
{
    vec2 uv = fragCoord/iResolution.xy;
    vec2 p = (fragCoord - iResolution.xy*.5) / iResolution.y;

    vec3 texCol = texture(iChannel0, uv).rgb;

    float time = iTime / 8.0;
    vec3 colOut = vec3(0.0);
    for (float i = 0.0; i < 32.0; i += 1.0) {
        // pick random point
        float rnd = i*(3.141 * 2.0 + .4123);
        vec2 pos = vec2(sin(time*0.95 + rnd*3.131)*0.5, cos(time*1.05 + rnd*5.763)*0.4) * 2.0;
        pos *= vec2(sin(time*1.05 + i*i*3.141/7.131), cos(time*.95 + i*(i-1.0)*3.141/4.235))*1.25;

        // random color
        vec3 randCol = vec3(mod(rnd * 2.0, 1.0), mod(rnd, 1.0) * 0.8, mod(rnd * 3.0, 1.0));

        // adding the points together
        float dist = clamp(1.0 - distance(p, pos) * 2.0, 0.0, 0.5);
        colOut += mix(vec3(0.0), randCol, dist);
    }
        // bad idea, dont uncomment
        //colOut = clamp(colOut, 0.0, 1.0);

        #ifdef ATMOSHPERE
    vec3 col = colOut / 5.0;
    fragColor = vec4(texCol + col,1.0);
    #else
    vec3 col = colOut / 2.0;
    fragColor = vec4(col,1.0);
    #endif
}
// --------[ Original ShaderToy ends here ]---------- //

void main(void)
{
    mainImage(gl_FragColor, gl_FragCoord.xy);
}