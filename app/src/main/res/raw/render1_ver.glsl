#version 300 es

precision highp float;

layout(location = 0) in vec4 aPostion;
layout(location = 1) in vec2 aUv;

out vec2 vUv;

void main() {
    vUv = aUv;
    gl_Position = aPostion;
}