package cn.kivensoft.script;

import javax.tools.SimpleJavaFileObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;


public class JavaClassObject extends SimpleJavaFileObject {

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    public JavaClassObject(String name, Kind kind) {
        super(URI.create("string:///" + name.replace('.', '/') + kind.extension), kind);
    }

    public byte[] getBytes() {
        return outputStream.toByteArray();
    }

    @Override
    public OutputStream openOutputStream() throws IOException {
        return outputStream;
    }
}