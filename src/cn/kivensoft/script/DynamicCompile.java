package cn.kivensoft.script;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;

public class DynamicCompile {
	
	private URLClassLoader parentClassLoader;
	private String classpath;

	public DynamicCompile() {
		this.parentClassLoader = (URLClassLoader)this.getClass().getClassLoader();
		this.buildClassPath();// 存在动态安装的问题，需要动态编译类路径
	}

	private void buildClassPath() {
		this.classpath = null;
		String ps = File.pathSeparator;
		StringBuilder sb = new StringBuilder();
		for (URL url : this.parentClassLoader.getURLs()) {
			String p = url.getFile();
			sb.append(p).append(ps); // 路径分割符linux为:window系统为;
		}
		this.classpath = sb.toString();
	}

	/**
	 * 编译出类
	 * @param fullClassName 全路径的类名
	 * @param javaCode java代码
	 * @return 目标类
	 */
	public Class<?> compileToClass(String fullClassName,
			CharSequence javaCode) throws Exception {
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		DiagnosticCollector<JavaFileObject> diagnostics
				= new DiagnosticCollector<JavaFileObject>();
		ClassFileManager fileManager = new ClassFileManager(
				compiler.getStandardFileManager(diagnostics, null, null));

		CompilationTask task = compiler.getTask(null, fileManager, diagnostics,
				Arrays.asList("-encoding", "UTF-8", "-cp", this.classpath), null,
				Arrays.asList(new JavaSourceObject(fullClassName, javaCode)));
		boolean success = task.call();

		if (success) {
			return new DynamicClassLoader(parentClassLoader, fileManager)
					.loadClass(fullClassName);
		} else {
			throw new Exception(compileError(diagnostics));
		}
	}

	private String compileError(DiagnosticCollector<JavaFileObject> diagnostics) {
		StringBuilder res = new StringBuilder();
		boolean first = true;
		for (Diagnostic<? extends JavaFileObject> diagnostic :
				diagnostics.getDiagnostics()) {
			if (first) first = false; else res.append('\n');
			res.append("Line: ").append(diagnostic.getLineNumber()).append(", ");
			res.append("Column: ").append(diagnostic.getColumnNumber()).append(", ");
			res.append("Message: ").append(diagnostic.getMessage(null));
		}
		return res.toString();
	}
}