package me.lloyd26.autorestart.tasks;

import me.lloyd26.autorestart.AutoRestart;
import me.lloyd26.autorestart.utils.MessageUtil;
import me.lloyd26.autorestart.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class RestartTask extends BukkitRunnable {

    private final AutoRestart plugin;

    private final LocalTime target;
    private final List<String> timeAnnounceList;

    private final Set<String> announcedTimes = new HashSet<>();

    private final List<DateTimeFormatter> formattersList = Arrays.asList(
            DateTimeFormatter.ofPattern("h:mma"),
            DateTimeFormatter.ofPattern("HH:mm")
    );

    public RestartTask(AutoRestart plugin, String targetTime) {
        this.plugin = plugin;
        this.timeAnnounceList = plugin.getAnnounceTimes();
        TimeUtils.sortTimeAnnounceList(timeAnnounceList);
        this.target = TimeUtils.parseTime(targetTime, formattersList);
    }

    @Override
    public void run() {
        LocalTime now = LocalTime.now();
        long delay = target.toNanoOfDay() - now.toNanoOfDay();

        if (delay < 0) {
            delay += TimeUnit.DAYS.toNanos(1);
        }

        long delayMillis = TimeUnit.NANOSECONDS.toMillis(delay);

        for (String announceTime : timeAnnounceList) {
            long announceMillis = TimeUtils.convertToMillis(announceTime);

            if (!announcedTimes.contains(announceTime) && Math.abs(delayMillis - announceMillis) <= 500) {
                announcedTimes.add(announceTime);

                String announceTimeMessage = MessageUtil.colorize(
                        plugin.getConfigManager().getConfig("messages.yml").getString("messages.time-announce")
                                .replace("{TIME_REMAINING}", TimeUtils.convertToTimeString(announceTime)
                        )
                );

                Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(announceTimeMessage));
                plugin.getLogger().log(Level.INFO, announceTimeMessage);
            }
        }

        if (delayMillis <= 500) {
            String restartMessage = MessageUtil.colorize(plugin.getConfigManager().getConfig("messages.yml").getString("messages.restart-reason"));

            Bukkit.getOnlinePlayers().forEach(p -> p.kickPlayer(restartMessage));
            plugin.getLogger().log(Level.INFO, restartMessage);

            Bukkit.shutdown();
            cancel();
        }
    }
}