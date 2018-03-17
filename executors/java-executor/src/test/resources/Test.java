import spark.Request;

public class Test {

    public String run(Request request) {
        if(request.requestMethod().equals("GET")) {
            return "Send me POST";
        }
        return request.body();
    }
}

