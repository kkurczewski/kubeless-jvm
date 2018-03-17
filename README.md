# kubeless-jvm
Jvm runtime for [Kubeless](https://github.com/kubeless/kubeless) based on [Spark](http://sparkjava.com/).

## Prerequisite
* kubeless installed

# Kotlin script

## Deploy

```kubeless function deploy --runtime-image kurczews/kubeless-kotlin:1.0 --from-file Test.kts --handler Test.run --trigger-http hello-kotlin```

__Test.kts__

```kotlin
import spark.Request

fun run(request: Request): String {
    if (request.requestMethod() == "GET") {
        return "Send me POST"
    }
    return request.body()
}
```

## Execute
```kubeless function call hello-kotlin```

```kubeless function call hello-kotlin --data '{"hello":"world"}'```

# Java class

## Deploy

```kubeless function deploy --runtime-image kurczews/kubeless-java:1.0 --from-file Test.java --handler Test.run --trigger-http hello-java```

__Test.java__

```java
import spark.Request;

public class Test {

    public String run(Request request) {
        if(request.requestMethod().equals("GET")) {
            return "Send me POST";
        }
        return request.body();
    }
}
```

## Execute
```kubeless function call hello-java```

```kubeless function call hello-java --data '{"hello":"world"}'```

