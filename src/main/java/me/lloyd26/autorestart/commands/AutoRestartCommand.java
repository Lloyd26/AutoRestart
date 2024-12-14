package me.lloyd26.autorestart.commands;


import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import me.lloyd26.autorestart.AutoRestart;
import me.lloyd26.autorestart.utils.MessageUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@CommandAlias("autorestart")
public class AutoRestartCommand extends BaseCommand {

    @Dependency
    private AutoRestart plugin;

    @Default
    @HelpCommand
    public void onHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }

    @Subcommand("reload")
    @Description("Reload the plugin")
    public void onReloadCommand(CommandSender sender) {
        plugin.cancelRestartTasks();
        plugin.getConfigManager().reloadConfigs();
        plugin.loadRestartTasks();

        String reloadMessage = MessageUtil.colorize(plugin.getConfigManager().getConfig("messages.yml").getString("plugin.plugin-reloaded"));

        sender.sendMessage(reloadMessage);
    }
}
