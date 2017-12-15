# jse
## java script engine
java 脚本引擎，使用java自带的编译器进行脚本编译并自动调用main函数，脚本必须是合法的java源文件。


### 使用方法
```
Usage: java -cp jse.jar JavaScriptEngine <script file> <class name> [parameter...]

    script file        compile and run this script file
    class name         class name in script file
    parameter...       script parameters
```

### 使用范例
```
java -Dfile.encoding=utf8 -jar java-script-engine.jar Main.java quicktest.Main
```

### 脚本范例
```
package quicktest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class Main {
    public static class X {
        int a;
        public X(int i) { a = i}
        public int getA() {
            return a;
        }
    }

	public static void main(String[] args) throws Exception {
		if (args.length > 0) System.out.println("arg0 = " + args[0]);
		List<X> x = new ArrayList<X>();
		for (int i = 0; i < 10; i++) x.add(new X(2 * i));
		boolean first = true;
		System.out.print("输出数组: [ ");
		for (X item : x) {
			if (first) first = false;
			else System.out.print(", ");
			System.out.printf("%d", item.getA());
		}
		System.out.println(" ]");
		x.forEach(v -> {System.out.println(v.getA() + "," + v.getB());});
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(sdf.format(new Date()));
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		System.out.println(sdf.format(new Date()));
	}

}
