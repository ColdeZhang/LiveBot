package deercloud.livebot;

import org.bukkit.entity.Player;

import javax.crypto.interfaces.PBEKey;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public class PlayerCache {
    PlayerCache(Collection<?> players) {
        for (Object player : players) {
            if (player instanceof Player) {
                Player p = (Player) player;
                if (!p.getName().equals(LiveBot.getInstance().getConfigManager().getBotName())) {
                    m_players.add(new XPlayer(p));
                }
            }
        }
    }

    public void addPlayer(Player player) {
        m_players.add(new XPlayer(player));
    }

    public void removePlayer(Player player) {
        for (XPlayer key : m_players) {
            if (key.getName().equals(player.getName())) {
                m_players.remove(key);
                return;
            }
        }
    }

    private final ArrayList<XPlayer> m_players = new ArrayList<>();

    private String current_following_player;    // 当前跟随的玩家

    public String getCurrentFollowingPlayerName() {
        return current_following_player;
    }

    // 获取下一个被跟踪的玩家
    public Player getNextPlayer() {
        if (m_players.size() == 0) {
            return null;
        }
        if (current_following_player == null) {
            current_following_player = m_players.iterator().next().getName();
            return LiveBot.getInstance().getServer().getPlayer(current_following_player);
        }
        boolean found = false;
        for (XPlayer key : m_players) {
            if (key.isDontFollow() || !key.getPlayer().isOnline()) {
                LiveBot.getInstance().getLogger().info("跳过不想被跟随玩家 : " + key.getName());
                continue;
            }
            // 跳过挂机的玩家
            if (key.isAFK() && LiveBot.getInstance().getConfigManager().getSkipAFK()) {
                LiveBot.getInstance().getLogger().info("跳过挂机玩家 : " + key.getName());
                continue;
            }
            if (found) {
                current_following_player = key.getName();
                return key.getPlayer();
            }
            if (key.getName().equals(current_following_player)) {
                found = true;
            }
        }
        current_following_player = m_players.iterator().next().getName();
        return LiveBot.getInstance().getServer().getPlayer(current_following_player);
    }

    public XPlayer getPlayer(Player player) {
        for (XPlayer key : m_players) {
            if (key.getName().equals(player.getName())) {
                return key;
            }
        }
        addPlayer(player);
        return getPlayer(player);
    }
}
