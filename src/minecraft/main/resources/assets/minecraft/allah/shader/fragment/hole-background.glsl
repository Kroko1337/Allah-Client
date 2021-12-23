precision mediump float;

uniform float time;
uniform vec2 resolution;

mat2 r2d(float a) {
    float c = cos(a), s = sin(a);
    return mat2(c, s, -s, c);
}

vec2 path(float t, float d) {
    float a = sin(cos(t * d * 0.05) * 3.14 + t * 0.3 + 1.5), b = cos(sin(t*0.1) + t*0.2 + 3.14 * 0.5 * sin(d * t * 0.2 + 0.5));
    return vec2(2.0 * a * a, a * b);
}

float g = 0.0;

float de(vec3 p, float t) {
    p.xy -= path(p.z, 1.0 / tan(sin(t*0.00025 + 0.3) + t*0.0001 + 0.5)*0.1);

    float d = -length(p.xy) * 1.0 + 5.0;

    g += 0.0035 / (0.0025 + d * d);
    return d;
}

void mainImage(out vec4 fragColor, in vec2 fragCoord){
    vec2 uv = fragCoord / resolution.xy - 0.5;
    uv.x *= resolution.x / resolution.y;

    float dt = time * 3.0;
    vec3 ro = vec3(0, 0, -3.0 + dt);
    vec3 ta = vec3(0, 0, dt * 5.0);

    ro.xy += path(ro.z, 1.0);
    ta.xy += path(ta.z, 1.0);

    vec3 fwd = normalize(ta - ro);
    vec3 right = cross(fwd, vec3(0, 1, 0));
    vec3 up = cross(right, fwd);
    vec3 rd = normalize(fwd * 0.5 + uv.x * right + uv.y * up);

    rd.xy *= r2d(dt * 0.1 + sin(-ro.x / 3.14 + dt * 0.1) * 1.25);
    vec3 p = floor(ro) + 0.5;
    vec3 mask;
    vec3 drd = 2.0 / abs(rd);
    rd = sign(rd);
    vec3 side = drd * (rd * (p - ro) + 0.5);

    float t = 0.0, ri = 0.0;
    for (float i = 0.0; i < 1.0; i += .01) {
        ri = i;
        if (de(p, cos(dt)) < 0.0) break;
        mask = step(side, side.yzx) * step(side, side.zxy);

        side += drd * mask;
        p += rd * mask;
    }
    t = length(p - ro);

    vec3 c = vec3(1.0) * length(mask * vec3(2.0, 0.5, 0.75));
    c = mix(vec3(0.7, 0.2, 0.7), vec3(0.2, 0.1, 0.6), c);
    c += g * 0.4;
    c.r += 0.42 * sin(p.z * 0.25);
    c = mix(c, vec3(0.2, 0.1, 0.2), 1.0 - exp(-0.001 * t * t));

    fragColor = vec4(c, 1.0);
}

void main(void){
    mainImage(gl_FragColor, gl_FragCoord.xy);
}