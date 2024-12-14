package me.lloyd26.autorestart;

import co.aikar.commands.MessageType;
import co.aikar.commands.PaperCommandManager;
import me.lloyd26.autorestart.commands.AutoRestartCommand;
import me.lloyd26.autorestart.config.ConfigManager;
import me.lloyd26.autorestart.tasks.RestartTask;
import me.lloyd26.autorestart.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public final class AutoRestart extends JavaPlugin {

    private static AutoRestart instance;

    ConfigManager configManager;

    List<String> announceTimes;

    private List<Integer> taskIdList = new ArrayList<>();

    private ChatColor primaryColor;
    private ChatColor accentColor;

    @Override
    public void onEnable() {
        instance = this;

        configManager = new ConfigManager(this);
        configManager.saveDefaultConfig("config.yml");
        configManager.saveDefaultConfig("messages.yml");

        primaryColor = ChatColor.valueOf(configManager.getConfig("messages.yml").getString("theme.color-primary").toUpperCase());
        accentColor = ChatColor.valueOf(configManager.getConfig("messages.yml").getString("theme.color-accent").toUpperCase());

        loadRestartTasks();

        PaperCommandManager commandManager = new PaperCommandManager(this);
        commandManager.setFormat(MessageType.SYNTAX, primaryColor, accentColor);
        commandManager.setFormat(MessageType.HELP, primaryColor, accentColor, primaryColor);
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

        String restartTaskLoadedMessage = MessageUtil.colorize(configManager.getConfig("messages.yml").getString("plugin.restart-loaded"));

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

    public static AutoRestart getInstance() {
        return instance;
    }
}
