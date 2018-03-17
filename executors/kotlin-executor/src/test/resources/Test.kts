import spark.Request

fun run(request: Request): String {
    if (request.requestMethod() == "GET") {
        return "Send me POST"
    }
    return request.body()
}