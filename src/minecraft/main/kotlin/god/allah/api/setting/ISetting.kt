package god.allah.api.setting

import com.github.drapostolos.typeparser.GenericType
import com.github.drapostolos.typeparser.TypeParser
import god.allah.api.executors.Module
import java.lang.ClassCastException

open class ISetting<T> {
    lateinit var module: Module
    lateinit var name: String
    lateinit var displayName: String

    fun getFields(): HashMap<String, *> {
        val hashMap = HashMap<String, Any>()
        this.javaClass.declaredFields.forEach { field ->
            field.isAccessible = true
            val setting = field.get(this)
            hashMap[field.name] = setting
        }
        return hashMap
    }

    fun getField(name: String): Any? {
        getFields().forEach { (fieldName, value) ->
            if (fieldName.equals(name, true))
                return value
        }
        return null
    }

    fun tryChangeField(name: String, value: String) {
        val changed = castString(name, value)
        if (changed != null)
            changeField(name, changed)
        else
            throw ClassCastException()
    }

    private fun castString(name: String, value: String): T? {
        getFields().forEach { (fieldName, current) ->
            if (fieldName.equals(name, true)) {
                val field = this.javaClass.getDeclaredField(fieldName)
                field.isAccessible = true
                val setting = field.get(this)
                val clazz = Class.forName(setting.javaClass.typeName)
                val parser = TypeParser.newBuilder().build()
                val parsed = parser.parse(value, clazz)
                return parsed as T
            }
        }
        return null
    }

    fun changeField(name: String, value: T) {
        getFields().forEach { (fieldName, current) ->
            if (fieldName.equals(name, true)) {
                val field = this.javaClass.getDeclaredField(fieldName)
                field.isAccessible = true
                field.set(this, value)
            }
        }
    }
}