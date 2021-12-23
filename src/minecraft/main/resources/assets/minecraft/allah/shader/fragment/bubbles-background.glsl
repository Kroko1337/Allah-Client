#ifdef GL_ES
precision mediump float;
#endif



uniform float time;
uniform vec2 mouse;
uniform vec2 resolution;

#define BALLS 20
#define MINSIZE .05
#define MAXSIZE .065

// from https://gist.github.com/yiwenl/745bfea7f04c456e0101
vec3 hsv2rgb(vec3 c)
{
    vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);
    vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);
    return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);
}

float rand(vec2 co){
    return fract(sin(dot(co, vec2(12.9898, 78.233))) * 43758.5453);
}

float invLerp(float a, float b, float t) {
    return (t - a)/(b - a);
}

vec4 color(vec2 coord) {
    vec2 yToXRatio = vec2(resolution.y/resolution.x,1.0);

    vec2 position = (( coord / resolution.xy ) - vec2(.5)) / yToXRatio;
    vec2 mousePos = (mouse - vec2(.5)) / yToXRatio;

    float val = .0;
    float colVal = 0.;
    vec3 col = vec3(0);

    int ballCount = 0;


    for (int i = 0; i < BALLS * 4; i++){
        vec2 ballPosInit = (vec2(rand(vec2(i, 14.6324)), rand(vec2(23.7644, i))) - vec2(.5)) / yToXRatio;
        vec2 ballVel = vec2(rand(vec2(i, 11.3654)), rand(vec2(14.5324, i))) * 2. - 1.;
        float ballRad = mix(MINSIZE, MAXSIZE, rand(vec2(i, 43.5463)));
        vec3 ballCol = hsv2rgb(vec3(rand(vec2(i, 24.4452)), 1, 1));
        vec2 ballDisp = sin(ballVel * time) / 8.;
        vec2 ballPos = ballPosInit + ballDisp;
        vec2 ballToMouse = mousePos - ballPos;
        float dispFactor = 1./(1.+pow(2., (length(ballToMouse) - .25) * 20.));
        //float dispFactor = 1. - 1./(pow(length(ballToMouse) / 1., 2.)+1.);
        //float dispFactor = step(length(ballToMouse), .3);
        ballPos += ballToMouse * dispFactor;
        float len = length(position - ballPos);
        float valAdd = ballRad / len;
        float colValAdd = ballRad / pow(len,3.0);
        val += valAdd;
        col += ballCol * colValAdd;
        colVal += colValAdd;
        ballCount++;
        if (float(ballCount) > float(BALLS) / yToXRatio.x) break;
    }

    float on = step(1.0 * pow(float(ballCount), .5), val) - step(1.1 * pow(float(ballCount), .5), val);
    col = col / colVal;


    return vec4(col * on, 1.0 );
}

void main( void ) {

    vec2 coord = gl_FragCoord.xy;

    vec4 col0 = color(coord + vec2(-.25, -.25));
    vec4 col1 = color(coord + vec2(-.25, .25));
    vec4 col2 = color(coord + vec2(.25, -.25));
    vec4 col3 = color(coord + vec2(.25, .25));

    gl_FragColor = (col0+col1+col2+col3)/4.;
    //gl_FragColor = col0;

}