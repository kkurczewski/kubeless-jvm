package pl.kurczews.executors

import spark.Request
import spark.Response
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager
import kotlin.system.measureNanoTime

class KotlinFunctionExecutor : FunctionExecutor() {

    companion object {
        const val SCRIPT_ARG_NAME = "request"
        const val SCRIPT_ARG_CLASS = "Request"
    }

    private val engine: ScriptEngine = ScriptEngineManager().getEngineByExtension("kts")
    private val script: String

    init {
        val lines: MutableList<String> = functionDir
                .resolve(className)
                .readLines()
                .toMutableList()

        lines.add("""$method((bindings["$SCRIPT_ARG_NAME"] as $SCRIPT_ARG_CLASS))""")
        script = lines.joinToString(System.lineSeparator())
    }

    override fun execute(request: Request, response: Response): String {

        engine.put(SCRIPT_ARG_NAME, request)

        var result: String? = null
        val time = measureNanoTime {
            result = engine.eval(script).toString()
        }
        println("Function execution took $time ns")
        return result ?: throw RuntimeException("Script returned no response")
    }

}