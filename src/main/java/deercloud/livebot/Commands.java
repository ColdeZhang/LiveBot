package deercloud.livebot;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

public class Commands implements TabExecutor {

    @Override
    @ParametersAreNonnullByDefault
    public boolean onCommand(org.bukkit.command.CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (Objects.equals(args[0], "reload")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.isOp()) {
                    m_config_manager.reload();
                    player.sendMessage("配置文件已重新加载。");
                    m_work_func.restart();
                } else {
                    player.sendMessage(ChatColor.RED + "你没有权限执行此命令。");
                }
            } else {
                m_config_manager.reload();
                sender.sendMessage("配置文件已重新加载。");
                m_work_func.restart();
            }
        } else if (Objects.equals(args[0], "setBot")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.isOp()) {
                    m_config_manager.setBotName(args[1]);
                    player.sendMessage("机器人已设置为" + args[1]);
                    m_work_func.reFindBot();
                } else {
                    player.sendMessage(ChatColor.RED + "你没有权限执行此命令。");
                }
            } else {
                m_config_manager.setBotName(args[1]);
                sender.sendMessage("机器人已设置为" + args[1]);
                m_work_func.reFindBot();
            }
        } else if (Objects.equals(args[0], "away")) {
            if (sender instanceof org.bukkit.entity.Player) {
                org.bukkit.entity.Player player = (org.bukkit.entity.Player) sender;
                m_work_func.removePlayer(player);
                player.sendMessage("机器人已经离开了你，本次登录不会再被选中。");
                m_work_func.restart();
            } else {
                sender.sendMessage("这个命令需要由玩家执行。");
            }
        } else if (Objects.equals(args[0], "setTime")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.isOp()) {
                    m_config_manager.setFocusTime(Integer.parseInt(args[1]));
                    player.sendMessage("聚焦时间已设置为" + args[1]);
                    m_work_func.restart();
                } else {
                    player.sendMessage(ChatColor.RED + "你没有权限执行此命令。");
                }
            } else {
                m_config_manager.setFocusTime(Integer.parseInt(args[1]));
                sender.sendMessage("聚焦时间已设置为" + args[1]);
                m_work_func.restart();
            }
        } else if (Objects.equals(args[0], "setPattern")) {
            if (!Objects.equals(args[1], "ORDER") && !Objects.equals(args[1], "RANDOM")) {
                sender.sendMessage("模式只能为ORDER或RANDOM。");
                return true;
            }
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.isOp()) {
                    m_config_manager.setChangePattern(args[1]);
                    player.sendMessage("切换方式设置为：" + args[1]);
                    m_work_func.restart();
                } else {
                    player.sendMessage(ChatColor.RED + "你没有权限执行此命令。");
                }
            } else {
                m_config_manager.setChangePattern(args[1]);
                sender.sendMessage("切换方式设置为：" + args[1]);
                m_work_func.restart();
            }
        }
        return true;
    }

    @Override
    @ParametersAreNonnullByDefault
    public List<String> onTabComplete(org.bukkit.command.CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("reload", "setBot", "away", "setTime", "setPattern");
        } else if (args.length == 2) {
            if (Objects.equals(args[0], "setBot")) {
                ArrayList<Player> playerList = new ArrayList<>(Bukkit.getOnlinePlayers());
                playerList.remove(m_work_func.getBot());
                ArrayList<String> playerNames = new ArrayList<>();
                for (Player player : playerList) {
                    playerNames.add(player.getName());
                }
                return playerNames;
            } else if (Objects.equals(args[0], "setTime")) {
                return Collections.singletonList("请输入时间(单位秒)");
            } else if (Objects.equals(args[0], "setPattern")) {
                return Arrays.asList("ORDER", "RANDOM");
            }
            else {
                return Collections.emptyList();
            }
        } else {
            return Collections.emptyList();
        }
    }



    protected ConfigManager m_config_manager = LiveBot.getInstance().getConfigManager();
    protected WorkFunc m_work_func = LiveBot.getInstance().getWorkFunc();
}
