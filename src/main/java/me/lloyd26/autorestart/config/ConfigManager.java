package me.lloyd26.autorestart.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class ConfigManager {
    private final Map<String, CustomConfig> configMap;

    private final JavaPlugin plugin;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        configMap = new HashMap<>();
    }

    public void createConfig(String filePath, String configName) {
        configName = !configName.endsWith(".yml") ? configName + ".yml" : configName;

        File configFile = new File(plugin.getDataFolder() + filePath, configName);

        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            saveResource(filePath, configName);
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        CustomConfig customConfig = new CustomConfig(configFile, config);
        configMap.put(configName, customConfig);
    }

    public void createConfig(String configName) {
        createConfig("", configName);
    }

    public FileConfiguration getConfig(String configName) {
        configName = !configName.endsWith(".yml") ? configName + ".yml" : configName;

        if (configMap.containsKey(configName)) return configMap.get(configName).getFileConfig();
        return null;
    }

    public void saveDefaultConfig(String filePath, String configName) {
        configName = !configName.endsWith(".yml") ? configName + ".yml" : configName;

        File configFile = new File(plugin.getDataFolder() + filePath, configName);
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        configMap.put(configName, new CustomConfig(configFile, config));

        if (!configFile.exists()) saveResource(filePath, configName);
    }

    public void saveDefaultConfig(String configName) {
        saveDefaultConfig("", configName);
    }

    private void saveResource(String path, String resourcePath) {
        resourcePath = resourcePath.replace('\\', '/');
        InputStream in = plugin.getResource(resourcePath);
        if (in == null) return;

        File outFile = new File(plugin.getDataFolder() + path, resourcePath);
        File outDir = new File(plugin.getDataFolder() + path);
        if (!outDir.exists()) outDir.mkdirs();

        try {
            OutputStream out = new FileOutputStream(outFile);
            byte[] buffer = new byte[1024];

            int len;
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }

            out.close();
            in.close();
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save config file \"" + outFile.getName() + "\".", e);
        }
    }

    public void reloadConfigs() {
        for (CustomConfig config : configMap.values()) {
            config.setFileConfig(YamlConfiguration.loadConfiguration(config.getFile()));
        }
    }
}
