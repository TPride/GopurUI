package gopur.plugin;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PluginClassLoader extends URLClassLoader {
    private JarPluginLoader loader;

    private final Map<String, Class> classMap = new HashMap<>();

    public PluginClassLoader(JarPluginLoader loader, ClassLoader classLoader, File file) throws MalformedURLException {
        super(new URL[]{file.toURI().toURL()}, classLoader);
        this.loader = loader;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return findClass(name, true);
    }

    protected Class<?> findClass(String name, boolean checkGlobal) throws ClassNotFoundException {
        if (name.startsWith("gopur."))
            throw new ClassNotFoundException(name);
        Class<?> result = classMap.get(name);
        if (result == null) {
            if (checkGlobal)
                result = loader.getClassByName(name);
            if (result == null) {
                result = super.findClass(name);
                if (result != null)
                    loader.setClass(name, result);
            }
        }
        classMap.put(name, result);
        return result;
    }

    public Set<String> getClasses() {
        return classMap.keySet();
    }
}
