package me.lloyd26.autorestart.config;

import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public class CustomConfig {

    private File customConfigFile;
    private FileConfiguration customConfig;

    public CustomConfig(File file, FileConfiguration fileConfig) {
        this.customConfigFile = file;
        this.customConfig = fileConfig;
    }

    public File getFile() {
        return this.customConfigFile;
    }

    public FileConfiguration getFileConfig() {
        return this.customConfig;
    }

    public void setFile(File file) {
        this.customConfigFile = file;
    }

    public void setFileConfig(FileConfiguration fileConfig) {
        this.customConfig = fileConfig;
    }

    /*public void create(String fileName) {
        create("", fileName);
    }

    public void create(String filePath, String fileName) {
        fileName = !fileName.endsWith(".yml") ? fileName + ".yml" : fileName;
        customConfigFile = new File(plugin.getDataFolder() + filePath, fileName);

        if (!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            saveResource(plugin, filePath, fileName);
        }

        customConfig = YamlConfiguration.loadConfiguration(customConfigFile);
    }

    public static void saveDefault(String filePath, String fileName) {
        fileName = !fileName.endsWith(".yml") ? fileName + ".yml" : fileName;
        saveResource(plugin, filePath, fileName);

        customConfigFile = new File(plugin.getDataFolder() + filePath, fileName);
        customConfig = YamlConfiguration.loadConfiguration(customConfigFile);
    }

    public void saveDefault(String fileName) {
        fileName = !fileName.endsWith(".yml") ? fileName + ".yml" : fileName;
        saveResource(plugin, "", fileName);
    }*/
}
