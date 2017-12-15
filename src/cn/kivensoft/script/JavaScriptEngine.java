package cn.kivensoft.script;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class JavaScriptEngine {

	public static void main(String[] args) throws Exception {

		if (args.length < 2) { usage(); return; }

		CharSequence cs = loadFile(args[0]);
		if (cs == null) return;
		
		DynamicCompile dynamicCompile = new DynamicCompile();
		Class<?> cls = dynamicCompile.compileToClass(args[1], cs);
		if (cls == null) return;
		
		Method method = cls.getMethod("main", String[].class);
		int modifiers = method == null ? 0 : method.getModifiers();
		if (method == null || !Modifier.isPublic(modifiers)
				|| !Modifier.isStatic(modifiers)
				|| method.getReturnType() != void.class
				|| method.getParameterTypes().length != 1
				|| !method.getParameterTypes()[0].isArray()
				|| method.getParameterTypes()[0].getComponentType() != String.class) {
			System.out.println("can't find public static void main(String[] args) in " + args[1]);
			return;
		}
		method.invoke(null, new Object[] {args});
	}

	private static CharSequence loadFile(String fileName) {
		File file = new File(fileName);
		if (!file.exists()) {
			System.out.printf("error, %s not exists.\n", file.getAbsolutePath());
			return null;
		}
		
		char[] buf = new char[65536];
		StringBuilder sb = new StringBuilder(16384);
		FileReader fr = null;
		try {
			fr = new FileReader(file);
			int count;
			while ((count = fr.read(buf)) != -1) sb.append(buf, 0, count);
			return sb;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		finally {
			try { if (fr != null) fr.close(); } catch (IOException e) { }
		}
		
	}
	
	private static void usage() {
		System.out.println(""
				+ "Java Script Engine -- Java 脚本引擎 v1.0\n"
				+ "CopyLeft Kivensoft 2017-12-16\n"
				+ "-----------------------------\n"
				+ "\n"
				+ "Usage: java -cp jse.jar JavaScriptEngine <script file> <class name> [parameter...]\n"
				+ "\n"
				+ "    script file        compile and run this script file\n"
				+ "    class name         class name in script file\n"
				+ "    parameter...       script parameters\n"
				);
	}
}