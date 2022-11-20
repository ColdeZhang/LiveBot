package deercloud.livebot;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.logging.Logger;

public class WorkFunc {

    public WorkFunc(LiveBot plugin){
        m_plugin = plugin;
        m_config_manager = m_plugin.getConfigManager();
        m_logger = m_plugin.getLogger();
    }

    private class task extends BukkitRunnable {
        @Override
        public void run() {
            if (!is_running) {
                this.cancel();
            }
            if (!m_is_bot_online || m_players.size() == 0) {
                m_logger.info("机器人不在线或者没有玩家，本次切换跳过。");
                return;
            }

            m_logger.info("切换玩家视角。");

            if (m_config_manager.getChangePattern().equals("ORDER")) {
                if (m_index >= m_players.size()) {
                    m_index = 0;
                }
                Player player = m_players.get(m_index);
                if (player.isDead()) {
                    m_logger.info("玩家" + player.getName() + "已经死亡，本次切换跳过。");
                    m_index++;
                    return;
                }
                m_logger.info("切换到玩家：" + player.getName());
                m_bot.setSpectatorTarget(Bukkit.getPlayer(player.getName()));
                m_current_player_name = player.getName();
                player.sendMessage(ChatColor.GOLD + "你被直播机器人选中了，如果不想被直播可以使用/livebot away 在本次登录不再被选中。被直播有助于给服务器增加人气哦～");
                m_index++;
            } else if (m_config_manager.getChangePattern().equals("RANDOM")) {
                int random_index = (int) (Math.random() * m_players.size());
                Player player = m_players.get(random_index);
                m_logger.info("切换到玩家：" + player.getName());
                m_bot.setSpectatorTarget(Bukkit.getPlayer(player.getName()));
                m_current_player_name = player.getName();
                player.sendMessage(ChatColor.GOLD + "你被直播机器人选中了，如果不想被直播可以使用/livebot away 在本次登录不再被选中。被直播有助于给服务器增加人气哦～");
            }
        }
    };



    public void start(long delay,long time) {
        if (!is_running) {
            is_running = true;
            m_task = new task();
            m_task.runTaskTimer(m_plugin,20L*delay,20L*time);
            return;
        }
        m_logger.info("直播机器人已经在运行了。");
    }

    public void reFindBot(){
        m_logger.info("重新寻找直播机器人。");
        botOffline();
        m_players.clear();
        m_players.addAll(Bukkit.getOnlinePlayers());
        for (Player player : m_players) {
            if (player.getName().equals(m_config_manager.getBotName())) {
                m_players.remove(player);
                botOnline(player);
                m_logger.info("找到直播机器人：" + player.getName());
                return;
            }
        }
        m_logger.warning("未找到直播机器人，序列不会启动。");
    }

    public void restart() {
        m_logger.info("重启序列。");
        stop();
        start(0,m_config_manager.getFocusTime());
    }

    public void stop() {
        if (is_running) {
            m_index = 0;
            is_running = false;
            m_task.cancel();
            m_task = null;
            m_logger.warning("序列停止。");
            return;
        }
        m_logger.info("序列已经停止了。");
    }

    public Player getBot() {
        return m_bot;
    }

    private Player m_bot;
    private final ArrayList<Player> m_players = new ArrayList<>();
    private boolean m_is_bot_online = false;
    private int m_index = 0;
    private task m_task = null;
    private final LiveBot m_plugin;
    private final ConfigManager m_config_manager;
    private final Logger m_logger;
    private String m_current_player_name = "";
    private boolean is_running = false;

    public String getCurrentPlayerName() {
        return m_current_player_name;
    }

    public void botOnline(Player bot) {
        bot.setGameMode(GameMode.SPECTATOR);
        m_bot = bot;
        m_is_bot_online = true;
        start(10,m_config_manager.getFocusTime());
    }

    public void botOffline() {
        stop();
        m_is_bot_online = false;
        m_bot = null;
    }

    public void addPlayer(Player player) {
        m_players.add(player);
    }

    public void removePlayer(Player player) {
        m_players.remove(player);
    }
}
