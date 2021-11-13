package god.allah.api.shader

import org.apache.commons.io.IOUtils
import org.lwjgl.opengl.ARBFragmentShader
import org.lwjgl.opengl.ARBShaderObjects
import java.io.InputStream


/**
 *
 * Coded by Felix1337 at 09.11.2021 started development : 16:53
 *
 */

abstract class Shader(shaderName: String) {

    private var programId = 0

    private var resourceLocation: InputStream?

    init {

        resourceLocation = javaClass.getResourceAsStream("/god/allph/api/shadersystem$shaderName")
        this.programId = ARBShaderObjects.glCreateProgramObjectARB() //setting up shaderProgramID ad start.
    }

    //function to compile the shader: idk if it works
    fun compileShader(type: ShaderType): Any {
        val shaderObject = ARBShaderObjects.glCreateShaderObjectARB(type.SHADER_TYPE)
        val fragID : Int = ShaderUtil.Companion.createShader(IOUtils.toString(resourceLocation), ARBFragmentShader.GL_FRAGMENT_SHADER_ARB)
        IOUtils.closeQuietly(resourceLocation)
        ARBShaderObjects.glCompileShaderARB(shaderObject)
        if (ARBShaderObjects.glGetObjectParameteriARB(shaderObject, ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == 0) {
            println("Unable to compile shader $shaderObject")
        } else {
            ARBShaderObjects.glAttachObjectARB(programId, shaderObject)
            ARBShaderObjects.glAttachObjectARB(programId, fragID)

        }
        return this
    }

    annotation class Info (val name: String) {}
}