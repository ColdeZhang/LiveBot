package deercloud.livebot.events;

import deercloud.livebot.ConfigManager;
import deercloud.livebot.LiveBot;
import deercloud.livebot.WorkFunc;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.logging.Logger;

public class playerEvent implements Listener {

    public playerEvent(){
        LiveBot m_plugin = LiveBot.getInstance();
        m_config_manager = m_plugin.getConfigManager();
        m_work_func = m_plugin.getWorkFunc();
        m_logger = m_plugin.getLogger();
    }

    @EventHandler
    public void onJoin(org.bukkit.event.player.PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (player.getName().equals(m_config_manager.getBotName())) {
            m_logger.info("检测到机器人上线。");
            m_work_func.botOnline(player);
            player.setGameMode(org.bukkit.GameMode.SPECTATOR);
        }else{
            m_work_func.addPlayer(player);
        }

    }

    @EventHandler
    public void onQuit(org.bukkit.event.player.PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (player.getName().equals(m_config_manager.getBotName())) {
            m_logger.info("检测到机器人下线。");
            m_work_func.botOffline();
        }else{
            m_work_func.removePlayer(player);
        }
    }

    // 玩家死亡
    @EventHandler
    public void onDeath(org.bukkit.event.entity.PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (player.getName().equals(m_work_func.getCurrentPlayerName())) {
            m_work_func.restart();
            m_logger.info("检测到玩家死亡，更换视角。");
        }
    }

    private final ConfigManager m_config_manager;
    private final WorkFunc m_work_func;

    private final Logger m_logger;


}
