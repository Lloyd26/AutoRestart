package me.lloyd26.autorestart;

import co.aikar.commands.PaperCommandManager;
import me.lloyd26.autorestart.commands.AutoRestartCommand;
import me.lloyd26.autorestart.config.ConfigManager;
import me.lloyd26.autorestart.tasks.RestartTask;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public final class AutoRestart extends JavaPlugin {

    ConfigManager configManager;

    List<String> announceTimes;

    private List<Integer> taskIdList = new ArrayList<>();

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        configManager.saveDefaultConfig("config.yml");
        configManager.saveDefaultConfig("messages.yml");

        loadRestartTasks();

        PaperCommandManager commandManager = new PaperCommandManager(this);
        commandManager.enableUnstableAPI("help");
        commandManager.registerCommand(new AutoRestartCommand());
    }

    @Override
    public void onDisable() {
        cancelRestartTasks();
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public List<String> getAnnounceTimes() {
        return announceTimes;
    }

    public void loadRestartTasks() {
        announceTimes = configManager.getConfig("config.yml").getStringList("announce");
        List<String> restartTimes = configManager.getConfig("config.yml").getStringList("restart");

        String restartTaskLoadedMessage = ChatColor.translateAlternateColorCodes('&',configManager.getConfig("messages.yml").getString("plugin.restart-loaded"));

        for (String restartTime : restartTimes) {
            RestartTask restartTask = new RestartTask(this, restartTime);
            BukkitTask task = restartTask.runTaskTimer(this, 0, 1);
            int taskId = task.getTaskId();
            taskIdList.add(taskId);

            getLogger().log(Level.INFO, restartTaskLoadedMessage.replace("{RESTART_TIME}", restartTime));
        }
    }

    public void cancelRestartTasks() {
        taskIdList.forEach(Bukkit.getScheduler()::cancelTask);
    }
}
