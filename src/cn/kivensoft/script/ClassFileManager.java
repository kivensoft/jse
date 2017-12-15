package cn.kivensoft.script;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;

import java.io.IOException;
import java.util.HashMap;

public class ClassFileManager extends ForwardingJavaFileManager<StandardJavaFileManager> {
	private HashMap<String, JavaClassObject> classObjectMap =
			new HashMap<String, JavaClassObject>();

	public ClassFileManager(StandardJavaFileManager standardManager) {
		super(standardManager);
	}

	public JavaClassObject getJavaClassObject(String className) {
		return classObjectMap.get(className);
	}

	@Override
	public JavaFileObject getJavaFileForOutput(Location location,
			String className, JavaFileObject.Kind kind, FileObject sibling)
			throws IOException {
		JavaClassObject jc = new JavaClassObject(className, kind);
		classObjectMap.put(className, jc);
		return jc;
	}
}