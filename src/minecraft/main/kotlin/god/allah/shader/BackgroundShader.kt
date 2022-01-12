package god.allah.shader

import god.allah.api.Wrapper
import god.allah.api.shader.Shader
import net.minecraft.client.Minecraft
import org.lwjgl.BufferUtils
import org.lwjgl.input.Mouse
import org.lwjgl.opengl.ARBShaderObjects
import org.lwjgl.opengl.Display
import org.lwjgl.opengl.GL20


class BackgroundShader(var type: Type) {

    var draggedX = 0

    fun use() {
        type.shader.use()
        GL20.glUniform1f(
            type.shader.getUniform("time"),
            (System.currentTimeMillis() - Wrapper.initTime) / 1000f
        )
        GL20.glUniform2f(
            type.shader.getUniform("resolution"), Display.getWidth().toFloat() + draggedX,
            Display.getHeight().toFloat()
        )

        if (type == Type.FISH)
            GL20.glUniform2f(
                type.shader.getUniform("mouse"), 0.5F,
                0.5F
            )
    }

    fun disuse() {
        type.shader.disuse()
    }

    fun changeTo(type: Type) {
        this.type.shader.disuse()
        this.type = type
    }

    enum class Type(val shader: Shader) {
        FISH(Shader("fish-background")), LAVA(Shader("lava-background")), PENIS(Shader("penis-background")), COLORS(
            Shader("colors-background")
        ),
        KOKS(Shader("koks-background")), SWASTIKA(Shader("swastika-background")), FOG(Shader("fog-background")), LIQUID(
            Shader("liquid-background")
        ),
        SUN(Shader("sun-background")), SNAKE(Shader("snake-background")), DRAWN(Shader("drawn-background")), HOLE(
            Shader(
                "hole-background"
            )
        ),
        PEPPA(Shader("peppa-background")), AMONGUS(Shader("amongus-background")), CLOUDS(Shader("cloud-background")), RETRO_SUN(
            Shader("retrosun-background")
        ),
        VORONOI(Shader("voronoi-background")), BLOCKS(Shader("blocks-background")), BUBBLES(Shader("bubbles-background"));

        fun getNext(): Type {
            var next = ordinal + 1
            if (next >= values().size) {
                next = 0
            }
            return values()[next]
        }

        fun getBefore(): Type {
            var next = ordinal - 1
            if (next < 0) {
                next = values().size - 1
            }
            return values()[next]
        }
    }
}