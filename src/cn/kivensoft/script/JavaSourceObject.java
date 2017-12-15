package cn.kivensoft.script;

import java.net.URI;

import javax.tools.SimpleJavaFileObject;

public class JavaSourceObject extends SimpleJavaFileObject {

	private CharSequence code;

	public JavaSourceObject(String className, CharSequence code) {
		super(URI.create("string:///" + className.replace('.', '/')
				+ Kind.SOURCE.extension), Kind.SOURCE);
		this.code = code;
	}

	@Override
	public CharSequence getCharContent(boolean ignoreEncodingErrors) {
		return code;
	}
}