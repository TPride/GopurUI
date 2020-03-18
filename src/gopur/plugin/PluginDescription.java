package gopur.plugin;

import gopur.utils.PluginException;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import java.util.*;

public class PluginDescription implements DescriptionKey {
    /**
     * 每一个插件的描述
     */

    private String name; //Plugin Name
    private String main; //Plugin Main Path
    private String description; //Plugin Description
    private String version; //Plugin Version
    private final List<String> authors = new ArrayList<>();

    public PluginDescription(String yamlString) {
        DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Yaml yaml = new Yaml(dumperOptions);
        try {
            loadMap(yaml.loadAs(yamlString, LinkedHashMap.class));
        } catch (PluginException e) {

        }
    }

    private void loadMap(Map<String, Object> map) throws PluginException{
        name = (String.valueOf(map.get(NAME))).replaceAll("[^A-Za-z0-9 _.-]", "");
        if (name.equals(""))
            throw new PluginException("插件名称错误");
        name = name.replace(" ", "_");
        main = String.valueOf(map.get(MAIN));
        description = String.valueOf(map.get(DESCRIPTION));
        version = String.valueOf(map.get(VERSION));
        String author = String.valueOf(map.get(AUTHOR));
        if (author.contains(","))
            authors.addAll(new ArrayList<>(author.split(",").length));
        else
            authors.add(author);
    }

    /**
     * 返回插件的完整名称
     * @return 插件的完整名称
     */
    public String getFullName() {
        return name + " v" + version;
    }

    public String getName() {
        return name;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public String getDescription() {
        return description;
    }

    public String getMain() {
        return main;
    }

    public String getVersion() {
        return version;
    }
}
