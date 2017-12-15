package cn.kivensoft.script;


public class DynamicClassLoader extends ClassLoader {
	private ClassFileManager classFileManager;
	
    public DynamicClassLoader(ClassLoader parent, ClassFileManager classFileManager) {
        super(parent);
        this.classFileManager = classFileManager;
    }
    
    
    @Override
    protected Class<?> findClass(String fullName) {
    	JavaClassObject jf = classFileManager.getJavaClassObject(fullName);
        byte[] classData = jf.getBytes();
        return this.defineClass(fullName, classData, 0, classData.length);
    }
}