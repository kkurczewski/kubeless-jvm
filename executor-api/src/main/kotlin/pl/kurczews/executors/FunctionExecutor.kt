package pl.kurczews.executors

import spark.Request
import spark.Response
import java.io.File

const val FUNCTION_DIR = "MOD_ROOT_PATH"
const val FUNCTION_CLASS = "MOD_NAME"
const val FUNCTION_METHOD = "FUNC_HANDLER"

@FunctionalInterface
abstract class FunctionExecutor {

    val functionDir: File = File(System.getenv(FUNCTION_DIR) ?: "/kubeless")
    val className: String
    val method: String

    abstract fun execute(request: Request, response: Response): String

    init {
        val missingEnvs = emptySet<String>().toMutableSet()
        val validator = { env: String -> System.getenv(env) ?: onEnvMissing(missingEnvs, env) }

        className = validator.invoke(FUNCTION_CLASS)
        method = validator.invoke(FUNCTION_METHOD)

        if (missingEnvs.isNotEmpty()) {
            throw MissingEnvException("Missing envs: ${missingEnvs.joinToString(", ")}")
        }

        println("$FUNCTION_DIR = $functionDir")
        println("$FUNCTION_CLASS = $className")
        println("$FUNCTION_METHOD = $method")
    }

    private fun onEnvMissing(missingEnvs: MutableSet<String>, env: String) =
            { missingEnvs.add(env); "" }.invoke()
}