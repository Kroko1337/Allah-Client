package god.allah.api.shader

import org.lwjgl.opengl.ARBFragmentShader
import org.lwjgl.opengl.ARBVertexShader

enum class ShaderType(val SHADER_TYPE: Int) {
    VERTEX_SHADER(ARBVertexShader.GL_VERTEX_SHADER_ARB), FRAGMENT_SHADER(ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);
}