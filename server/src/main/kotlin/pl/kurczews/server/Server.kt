package pl.kurczews.server

import pl.kurczews.executors.FunctionExecutor
import spark.Spark.*
import spark.Spark.exception

class Server(private val functionExecutor: FunctionExecutor) {

    fun start() {
        port(8080)
        get("/healthz") { _, _ -> "OK" }
        get("/", { request, response -> functionExecutor.execute(request, response) })
        post("/", { request, response -> functionExecutor.execute(request, response) })

        exception(Exception::class.java) { ex, _, _ -> System.err.println("ERROR: $ex") }
    }
}