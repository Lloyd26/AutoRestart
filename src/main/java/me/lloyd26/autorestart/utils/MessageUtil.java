package me.lloyd26.autorestart.utils;

import me.lloyd26.autorestart.AutoRestart;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Stack;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageUtil {

    private static AutoRestart plugin = AutoRestart.getInstance();

    /**
     * Replaces custom color codes with the corresponding color codes from the messages.yml file.
     *
     * @param message The message to colorize.
     * @return The colorized message.
     */
    public static String colorize(String message) {
        ConfigurationSection theme = plugin.getConfigManager().getConfig("messages.yml").getConfigurationSection("theme");
        Stack<String> colorStack = new Stack<>();

        Pattern pattern = Pattern.compile("<\\/c\\d+>|<c(\\d+)>");
        Matcher matcher = pattern.matcher(message);
        StringBuffer coloredMessage = new StringBuffer();

        System.out.println("message = " + message);
        while (matcher.find()) {
            if (matcher.group(0).startsWith("</c")) {
                if (!colorStack.isEmpty()) {
                    colorStack.pop();
                    String colorCode = colorStack.isEmpty() ? ChatColor.RESET.toString() : colorStack.peek();
                    matcher.appendReplacement(coloredMessage, colorCode);
                } else {
                    matcher.appendReplacement(coloredMessage, ChatColor.RESET.toString());
                }
            } else {
                String colorTag = matcher.group(1);
                String colorCode = getColorCode(theme, colorTag);
                colorStack.push(colorCode);
                matcher.appendReplacement(coloredMessage, colorCode);
            }
        }

        matcher.appendTail(coloredMessage);

        String colored = ChatColor.translateAlternateColorCodes('&', coloredMessage.toString());

        return colored;
    }

    /**
     * Gets the color code based on the theme configuration.
     *
     * @param theme    The theme configuration section.
     * @param colorTag The color tag.
     * @return The corresponding color code.
     */
    private static String getColorCode(ConfigurationSection theme, String colorTag) {
        try {
            switch (colorTag) {
                case "0":
                    return ChatColor.valueOf(theme.getString("color-primary").toUpperCase()).toString();
                case "1":
                    return ChatColor.valueOf(theme.getString("color-accent").toUpperCase()).toString();
                default:
                    return "";
            }
        } catch (IllegalArgumentException e) {
            AutoRestart.getInstance().getLogger().log(Level.SEVERE, "Invalid color: " + colorTag);
            return "";
        }
    }
}