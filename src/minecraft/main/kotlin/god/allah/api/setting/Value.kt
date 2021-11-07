package god.allah.api.setting

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class Value(val name: String, val displayName: String = "")
