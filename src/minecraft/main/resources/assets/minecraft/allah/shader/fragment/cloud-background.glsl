/*
 * Original shader from: https://www.shadertoy.com/view/sdcXRN
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

// --------[ Original ShaderToy begins here ]---------- //
#define PI 3.1415927
#define E .001

float n(float c) {
    return min(1., max(.0, c));
}

vec3 n3(vec3 c) {
    return min(vec3(1), max(vec3(0), c));
}

float estep(float threshold, float x, float e) {
    return smoothstep(threshold + e, threshold - e, x);
}

vec3 mask(vec3 c0, vec3 c1, float m) {
    return n3(c0 * (1.0 - m) + c1 * m);
}

float rand(float seed) {
    float v = pow(seed, 6.0 / 7.0);
    v *= sin(v) + 1.;
    return fract(v);
}

mat2 rot(float angle) {
    return mat2(
    cos(angle * 2. * PI), -sin(angle * 2. * PI),
    sin(angle * 2. * PI), cos(angle * 2. * PI)
    );
}

float ell(vec2 uv, vec2 center, vec2 size, float r, float angle, float e) {
    uv -= center;
    uv *= rot(angle);
    float t;
    float c;
    vec2 p;
    if (size.x >= size.y) {
        t = size.x;
        c = sqrt((size.x * .5) * (size.x * .5) - (size.y * .5) * (size.y * .5));
        p = vec2(c, .0);
    } else {
        t = size.y;
        c = sqrt((size.y * .5) * (size.y * .5) - (size.x * .5) * (size.x * .5));
        p = vec2(.0, c);
    }
    return estep(t, length(uv - p) + length(uv + p), e * 4.);
}

    ////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    #define SKY vec3(.4, .9, 1.)
    #define CLOUD_COMPLEXITY 20

float cloud_base(vec2 uv, float seed) {
    float cloud = .0;

    float min_x = .0;
    float max_x = .0;

    float posx, posy;
    vec2 pos, size;

    for(int i = 0; i < CLOUD_COMPLEXITY; i++) {
        size = vec2(.1 + rand(seed++) * .2, .1 + rand(seed++) * .1);
        posx = rand(seed++);
        posy = rand(seed++);
        pos = vec2((posx*posx - .5) * .5, (posy*posy) * .1 + size.y * .2);

        min_x = min(min_x, pos.x);
        max_x = max(max_x, pos.x);

        cloud += ell(uv, pos, size, .0, (rand(seed++) - .5) * .1, E);
    }

    return n(cloud);
}

vec3 cloud(vec3 c, vec2 uv, float t, float y) {
    uv += vec2(t, .0);
    float seed = floor(uv.x + .5);
    uv.x = fract(uv.x + .5) - .5;

    uv -= vec2(rand(seed++) * .1, y + (rand(seed++) - .5) * .3);

    float base = cloud_base(uv, seed);
    float shadow_in = base - cloud_base(uv - vec2(.01, .05), seed) * base;

    c = mask(c, vec3(1.), .9 * (base - shadow_in));
    c = mask(c, vec3(.9), .9 * shadow_in);

    return c;
}

vec3 image(vec2 uv, float t) {
    vec3 c = SKY;

    c = cloud(c, uv, t * .3, .2);
    c = cloud(c, uv, t * .4, .0);
    c = cloud(c, uv, t * .5, -.2);

    return c;
}

void mainImage(out vec4 fragColor, in vec2 fragCoord)
{
    vec2 uv = (fragCoord.xy / iResolution.xy - 0.5);
    uv.x *= iResolution.x / iResolution.y;


    vec3 c = image(uv, iTime);

    fragColor = vec4(c,1.0);
}
// --------[ Original ShaderToy ends here ]---------- //

void main(void)
{
    mainImage(gl_FragColor, gl_FragCoord.xy);
}