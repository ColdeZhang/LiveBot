package deercloud.livebot.events;

import deercloud.livebot.ConfigManager;
import deercloud.livebot.LiveBot;
import deercloud.livebot.WorkFunc;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class playerEvent implements Listener {

    public playerEvent(){
        LiveBot m_plugin = LiveBot.getInstance();
        m_config_manager = m_plugin.getConfigManager();
        m_work_func = m_plugin.getWorkFunc();
    }

    @EventHandler
    public void onJoin(org.bukkit.event.player.PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (player.getName().equals(m_config_manager.getBotName())) {
            System.out.println("配置的直播机器人已经上线，开始执行序列。");
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
            System.out.println("配置的直播机器人已经下线，停止执行序列。");
            m_work_func.botOffline();
        }else{
            m_work_func.removePlayer(player);
        }

    }

    private final ConfigManager m_config_manager;
    private final WorkFunc m_work_func;


}
