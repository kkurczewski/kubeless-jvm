package pl.kurczews.server

import pl.kurczews.executors.FunctionExecutor
import java.util.*

fun main(args: Array<String>) {

    val runtimeList = ServiceLoader.load(FunctionExecutor::class.java).toList()

    if (runtimeList.size > 1) {
        System.err.println("WARN: Found more than one function runtime!")
    }

    val runtime = runtimeList.first()
    println("Active runtime: ${runtime.javaClass}")

    Server(runtime).start()

    println("Check health at: http://localhost:8080/healthz")
}
