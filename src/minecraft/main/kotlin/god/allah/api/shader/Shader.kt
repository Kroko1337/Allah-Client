package god.allah.api.shader

import net.minecraft.client.Minecraft
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL20
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.RuntimeException

open class Shader {
    private var programId: Int = 0
    private var vertexShaderId: Int = 0
    private var fragmentShaderId: Int = 0

    private var fragmentName: String = ""
    private var vertexName: String = ""

    constructor() {
        val info = this::class.java.getAnnotation(Info::class.java)
        this.fragmentName = info.fragment
        this.vertexName = info.vertex
        init()
    }

    constructor(fragment: String, vertex: String = "vertex") {
        this.fragmentName = fragment
        this.vertexName = vertex
        init()
    }

    private fun init() {
        programId = GL20.glCreateProgram()

        fragmentShaderId = loadShader(GL20.GL_FRAGMENT_SHADER, "fragment/$fragmentName")
        vertexShaderId = loadShader(GL20.GL_VERTEX_SHADER, "vertex/$vertexName")

        GL20.glAttachShader(programId, fragmentShaderId)
        GL20.glAttachShader(programId, vertexShaderId)

        GL20.glLinkProgram(programId)
        GL20.glValidateProgram(programId)
    }

    fun getUniform(uniformName: String): Int {
        return GL20.glGetUniformLocation(programId, uniformName)
    }

    fun use() {
        GL20.glUseProgram(programId)
    }

    fun disuse() {
        GL20.glUseProgram(0)
    }

    fun cleanUp() {
        disuse()
        GL20.glDetachShader(programId, vertexShaderId)
        GL20.glDetachShader(programId, fragmentShaderId)

        GL20.glDeleteShader(vertexShaderId)
        GL20.glDeleteShader(fragmentShaderId)
        GL20.glDeleteProgram(programId)
    }

    fun loadShader(type: Int, name: String): Int {
        val stringBuilder = StringBuilder()
        try {
            val reader = BufferedReader(
                InputStreamReader(
                    Minecraft.getMinecraft().defaultResourcePack.getInputStream(ResourceLocation("allah/shader/$name.glsl"))
                )
            )
            while (reader.ready()) {
                stringBuilder.append(reader.readLine()).append("\n")
            }
            reader.close()
        } catch (exception: IOException) {
            exception.printStackTrace()
        }
        val id = GL20.glCreateShader(type)
        GL20.glShaderSource(id, stringBuilder)
        GL20.glCompileShader(id)

        if (GL20.glGetShaderi(id, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE)
            throw RuntimeException("Failed to compile shader: ${GL20.glGetShaderInfoLog(id, Int.MAX_VALUE)}")
        return id
    }

    @Target(AnnotationTarget.TYPE)
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Info(val fragment: String, val vertex: String = "vertex")
}