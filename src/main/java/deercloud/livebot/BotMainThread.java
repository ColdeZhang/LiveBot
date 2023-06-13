package deercloud.livebot;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class BotMainThread extends BukkitRunnable {
    /**
     * When an object implementing interface {@code Runnable} is used
     * to create a thread, starting the thread causes the object's
     * {@code run} method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method {@code run} is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        Player bot = LiveBot.getInstance().getServer().getPlayer(LiveBot.getInstance().getConfigManager().getBotName());
        if (bot == null) {
            return;
        }
        Player target = LiveBot.getInstance().getCache().getNextPlayer();
        if (target == null) {
            return;
        }
        bot.setGameMode(GameMode.CREATIVE);
        bot.teleport(target.getLocation());
        bot.setGameMode(GameMode.SPECTATOR);
        bot.setSpectatorTarget(target);
    }
}
