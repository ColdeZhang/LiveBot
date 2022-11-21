package deercloud.livebot;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class WorkFunc {

    public WorkFunc(LiveBot plugin){
        m_plugin = plugin;
        m_config_manager = m_plugin.getConfigManager();
        m_logger = m_plugin.getLogger();
    }

    private class mainTask extends BukkitRunnable {
        @Override
        public void run() {
            if (!is_running) {
                this.cancel();
            }
            if (!m_is_bot_online || m_players.size() == 0) {
                m_logger.info("机器人不在线或者没有玩家，本次切换跳过。");
                return;
            }

            m_logger.info("准备切换玩家视角。");

            Player player = getNext();
            int total_players = m_players.size();
            if (player == null || total_players == 0) {
                m_logger.info("没有玩家，本次切换跳过。");
                return;
            }
            int counter = 0;
            while (counter < total_players) {
                counter++;
                if (player.isDead()) {
                    m_logger.info("玩家" + player.getName() + "已经死亡，跳过此玩家。");
                    player = getNext();
                    continue;
                }
                if (isAFK(player) && m_config_manager.getSkipAFK()) {
                    m_logger.info("玩家" + player.getName() + "可能在挂机，跳过此玩家。");
                    player = getNext();
                    continue;
                }
                break;
            }
            m_logger.info("切换到玩家：" + player.getName());
            m_bot.setGameMode(GameMode.SPECTATOR);
            m_bot.setSpectatorTarget(player);
            m_current_player_name = player.getName();
            player.sendMessage(ChatColor.GOLD + "你被直播机器人选中了，如果不想被直播可以使用/livebot away 在本次登录不再被选中。被直播有助于给服务器增加人气哦～");
        }
    }

    private class updateLocationTask extends BukkitRunnable {
        @Override
        public void run() {
            for (Map.Entry<Player, Location> entry : m_players.entrySet()) {
                Player player = entry.getKey();
                m_players.put(player, player.getLocation());
            }
        }
    }


    private boolean isAFK(Player player) {
        Location location_1 =  player.getLocation();
        if (!m_players.containsKey(player)){
            m_logger.warning("玩家" + player.getName() + "不在玩家列表中，无法判断是否在挂机。");
            return true;
        }
        Location location_2 = m_players.get(player);
        if (location_2 == null) {
            m_logger.warning("玩家" + player.getName() + "上一次位置为空，无法判断是否在挂机。");
            return true;
        }
        return location_1.getX() == location_2.getX() && location_1.getY() == location_2.getY() && location_1.getZ() == location_2.getZ();
    }

    private Player getNext() {
        if (m_config_manager.getChangePattern().equals("ORDER")) {
            m_index++;
            if (m_index >= m_players.size()) {
                m_index = 0;
            }
            return get_m_player(m_index);
        } else if (m_config_manager.getChangePattern().equals("RANDOM")) {
            int random_index = (int) (Math.random() * m_players.size());
            return get_m_player(random_index);
        }
        return null;
    }

    private Player get_m_player(int index) {
        int i = 0;
        for (Map.Entry<Player, Location> entry : m_players.entrySet()) {
            if (i == index) {
                return entry.getKey();
            }
            i++;
        }
        return null;
    }

    public void start(long delay,long time) {
        if (!is_running) {
            is_running = true;
            m_mainTask = new mainTask();
            m_updateLocationTask = new updateLocationTask();
            m_mainTask.runTaskTimer(m_plugin,20L*delay,20L*time);
            m_updateLocationTask.runTaskTimer(m_plugin,20L*delay,m_config_manager.getAFKTime()*20L);
            return;
        }
        m_logger.info("直播机器人已经在运行了。");
    }

    public void reFindBot(){
        m_logger.info("正在寻找直播机器人。");
        botOffline();
        updatePlayerList();
        // 获取对应名字的玩家
        Player bot = Bukkit.getPlayer(m_config_manager.getBotName());
        if (bot == null) {
            m_logger.warning("未找到直播机器人，序列不会启动。");
            return;
        }
        if (m_players.containsKey(Bukkit.getPlayer(m_config_manager.getBotName()))) {
            m_players.remove(bot);
            botOnline(bot);
            m_logger.info("找到直播机器人：" + bot.getName());
        }
    }

    public void updatePlayerList(){
        m_logger.info("更新玩家列表。");
        m_players.clear();
        ArrayList<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        for (Player player : players) {
            m_players.put(player, player.getLocation());
        }
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
            m_mainTask.cancel();
            m_updateLocationTask.cancel();
            m_mainTask = null;
            m_updateLocationTask = null;
            m_logger.warning("序列停止。");
            return;
        }
        m_logger.info("序列已经停止了。");
    }

    public Player getBot() {
        return m_bot;
    }

    private Player m_bot;
//    private final ArrayList<Player> m_players = new ArrayList<>();
    private final Map<Player, Location> m_players = new HashMap<>();
    private boolean m_is_bot_online = false;
    private int m_index = 0;
    private mainTask m_mainTask = null;
    private updateLocationTask m_updateLocationTask = null;
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
        if (m_players.containsKey(player)) {
            return;
        }
        m_players.put(player, player.getLocation());
    }

    public void removePlayer(Player player) {
        if (!m_players.containsKey(player)) {
            return;
        }
        m_players.remove(player);
    }
}
