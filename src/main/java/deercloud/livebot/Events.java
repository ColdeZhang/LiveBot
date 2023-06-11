package deercloud.livebot;

import deercloud.livebot.ConfigManager;
import deercloud.livebot.LiveBot;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.logging.Logger;

public class Events implements Listener {

    public Events(){
        LiveBot m_plugin = LiveBot.getInstance();
        m_config_manager = m_plugin.getConfigManager();
        m_logger = m_plugin.getLogger();
    }

    @EventHandler
    public void onJoin(org.bukkit.event.player.PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!player.getName().equals(m_config_manager.getBotName())) {
            LiveBot.getInstance().getCache().addPlayer(player);
        }
    }

    @EventHandler
    public void onQuit(org.bukkit.event.player.PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (!player.getName().equals(m_config_manager.getBotName())) {
            LiveBot.getInstance().getCache().removePlayer(player);
        }
    }

    // 玩家死亡
    @EventHandler
    public void onDeath(org.bukkit.event.entity.PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (player.getName().equals(LiveBot.getInstance().getCache().getCurrentFollowingPlayerName())) {
            LiveBot.getInstance().restartBot();
            m_logger.info("检测到玩家" + player.getName() + "死亡，更换视角。");
        }
    }

    @EventHandler
    public void OnPlayerChangeWorld(org.bukkit.event.player.PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        if (player.getName().equals(LiveBot.getInstance().getCache().getCurrentFollowingPlayerName())) {
            LiveBot.getInstance().restartBot();
            m_logger.info("检测到玩家" + player.getName() + "更换世界，更换视角。");
        }
    }

    private final ConfigManager m_config_manager;
    private final Logger m_logger;


}
