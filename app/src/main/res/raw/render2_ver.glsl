#version 300 es
in vec3 aPosition;
in vec2 aTexCoor;
out vec2 vTexCoor;
void main()
{
    gl_Position = vec4(aPosition, 1.0);
    vTexCoor = aTexCoor;
}