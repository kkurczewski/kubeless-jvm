package pl.kurczews.executors;

import org.jetbrains.annotations.NotNull;
import spark.Request;
import spark.Response;

import javax.tools.ToolProvider;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;

public class JavaFunctionExecutor extends FunctionExecutor {

    private static final int SUCCESS_CODE = 0;

    private Object instance;

    public JavaFunctionExecutor() {
        File newPath = tryAddExtension(super.getFunctionDir(), super.getClassName());
        tryCompile(newPath);
        instance = tryLoadInstance(super.getFunctionDir(), super.getClassName());
    }

    @NotNull
    @Override
    public String execute(@NotNull Request request, @NotNull Response response) {
        try {
            long start = System.nanoTime();
            String result = (String) instance
                    .getClass()
                    .getMethod(super.getMethod(), Request.class)
                    .invoke(instance, request);
            long elapsed = System.nanoTime() - start;
            System.out.println("Function execution took " + elapsed + " ns");
            return result;
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private File tryAddExtension(File functionDir, String className) {
        Path path = functionDir.toPath();

        File oldPath = path.resolve(className).toFile();
        if (!oldPath.exists()) {
            throw new RuntimeException("File doesn't exist: " + oldPath.toString());
        }

        File newPath = path.resolve(className + ".java").toFile();
        if (!oldPath.renameTo(newPath)) {
            System.err.println("WARN: Renaming class failed: " + oldPath.toString() + " -> " + newPath.toString());
        }
        return newPath;
    }

    private void tryCompile(File file) {
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        if (!compile(file, err)) {
            String cause = new String(err.toByteArray());
            throw new RuntimeException(cause);
        }
    }

    private boolean compile(File file, OutputStream errorPipe) {
        int statusCode = ToolProvider
                .getSystemJavaCompiler()
                .run(null, null, errorPipe, file.getPath());
        return statusCode == SUCCESS_CODE;
    }

    private Object tryLoadInstance(File functionDir, String className) {
        try {
            ClassLoader classLoader = URLClassLoader.newInstance(new URL[]{functionDir.toURI().toURL()});
            return classLoader.loadClass(className).newInstance();
        } catch (IllegalAccessException | InstantiationException | ClassNotFoundException | MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}