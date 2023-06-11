package deercloud.livebot;

import org.bukkit.entity.Player;

import org.bukkit.Location;

public class XPlayer {
    private final String name;
    private int x;
    private int y;
    private int z;
    private Boolean dont_follow = false;

    public XPlayer(Player player) {
        name = player.getName();
        x = player.getLocation().getBlockX();
        y = player.getLocation().getBlockY();
        z = player.getLocation().getBlockZ();
    }

    private void updateLocation() {
        Player player = getPlayer();
        x = player.getLocation().getBlockX();
        y = player.getLocation().getBlockY();
        z = player.getLocation().getBlockZ();
    }

    public void dontFollow() {
        dont_follow = true;
    }

    public void follow() {
        dont_follow = false;
    }

    public Boolean isDontFollow() {
        return dont_follow;
    }

    public String getName() {
        return name;
    }

    public Player getPlayer(){
        return LiveBot.getInstance().getServer().getPlayer(name);
    }

    public boolean isAFK() {
        Player player = getPlayer();
        if (player == null) {
            return false;
        }
        if (x == player.getLocation().getBlockX() && y == player.getLocation().getBlockY() && z == player.getLocation().getBlockZ()) {
            return true;
        }
        updateLocation();
        return false;
    }
}
