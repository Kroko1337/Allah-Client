package god.allah.api.shader

import org.lwjgl.opengl.ARBShaderObjects

import org.lwjgl.opengl.GL11

class ShaderUtil {

    //Diggah kroko einfach kotlin pro einfach companion object
    companion object {

        fun createShader(shaderCode: String?, type: Int): Int {
            val shader = ARBShaderObjects.glCreateShaderObjectARB(type)
            if (shader == 0) return 0
            ARBShaderObjects.glShaderSourceARB(shader, shaderCode)
            ARBShaderObjects.glCompileShaderARB(shader)
            if (ARBShaderObjects.glGetObjectParameteriARB(
                    shader,
                    ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB
                ) == GL11.GL_FALSE
            ) {
                println("Cant create shader: " + shaderLog(shader))
                ARBShaderObjects.glDeleteObjectARB(shader)
                return shader
            }
            return shader
        }

        private fun shaderLog(i: Int): String? {
            return ARBShaderObjects.glGetInfoLogARB(i, ARBShaderObjects.glGetObjectParameteriARB(i, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB))
        }

    }


}