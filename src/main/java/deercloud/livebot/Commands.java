package deercloud.livebot;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.*;

public class Commands implements TabExecutor {

    @Override
    public boolean onCommand(org.bukkit.command.CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (Objects.equals(args[0], "reload")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.isOp()) {
                    LiveBot.getInstance().getConfigManager().reload();
                    player.sendMessage("配置文件已重新加载。");
                    LiveBot.getInstance().restartBot();
                } else {
                    player.sendMessage(ChatColor.RED + "你没有权限执行此命令。");
                }
            } else {
                LiveBot.getInstance().getConfigManager().reload();
                sender.sendMessage("配置文件已重新加载。");
                LiveBot.getInstance().restartBot();
            }
        } else if (Objects.equals(args[0], "setBot")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.isOp()) {
                    LiveBot.getInstance().getConfigManager().setBotName(args[1]);
                    player.sendMessage("机器人已设置为" + args[1]);
                    LiveBot.getInstance().restartBot();
                } else {
                    player.sendMessage(ChatColor.RED + "你没有权限执行此命令。");
                }
            } else {
                LiveBot.getInstance().getConfigManager().setBotName(args[1]);
                sender.sendMessage("机器人已设置为" + args[1]);
                LiveBot.getInstance().restartBot();
            }
        } else if (Objects.equals(args[0], "away")) {
            if (sender instanceof org.bukkit.entity.Player) {
                org.bukkit.entity.Player player = (org.bukkit.entity.Player) sender;
                if (LiveBot.getInstance().getConfigManager().getCanBeMoved()) {
                    LiveBot.getInstance().getCache().getPlayer(player).dontFollow();
                    player.sendMessage(ChatColor.GREEN + "机器人已经离开了你，本次登录不会再被选中。");
                } else {
                    player.sendMessage(ChatColor.YELLOW + "服主设置了强制跟随，你无法摆脱机器人，请联系服主。");
                }
            } else {
                sender.sendMessage("这个命令需要由玩家执行。");
            }
        } else if (Objects.equals(args[0], "setTime")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.isOp()) {
                    if (Integer.parseInt(args[1]) <= 10) {
                        player.sendMessage(ChatColor.RED + "时间间隔不能小于10秒。");
                    } else {
                        LiveBot.getInstance().getConfigManager().setFocusTime(Integer.parseInt(args[1]));
                        player.sendMessage("聚焦时间已设置为" + args[1]);
                        LiveBot.getInstance().restartBot();
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "你没有权限执行此命令。");
                }
            } else {
                LiveBot.getInstance().getConfigManager().setFocusTime(Integer.parseInt(args[1]));
                sender.sendMessage("聚焦时间已设置为" + args[1]);
                LiveBot.getInstance().restartBot();
            }
        } else if (Objects.equals(args[0], "setCanBeMoved")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.isOp()) {
                    updateCanBeMoved(sender, args);
                }
            } else {
                updateCanBeMoved(sender, args);
            }
        } else if (Objects.equals(args[0], "stop")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.isOp()) {
                    player.sendMessage(ChatColor.GREEN + "机器人已停止。");
                    LiveBot.getInstance().stopBot();
                } else {
                    player.sendMessage(ChatColor.RED + "你没有权限执行此命令。");
                }
            } else {
                sender.sendMessage(ChatColor.GREEN + "机器人已停止。");
                LiveBot.getInstance().stopBot();
            }
        } else if (Objects.equals(args[0], "start")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.isOp()) {
                    player.sendMessage(ChatColor.GREEN + "机器人已启动。");
                    LiveBot.getInstance().restartBot();
                } else {
                    player.sendMessage(ChatColor.RED + "你没有权限执行此命令。");
                }
            } else {
                sender.sendMessage("机器人已启动。");
                LiveBot.getInstance().restartBot();
            }
        } else if (Objects.equals(args[0], "skipAFK")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.isOp()) {
                    updateSkipAFK(sender, args);
                } else {
                    player.sendMessage(ChatColor.RED + "你没有权限执行此命令。");
                }
            } else {
                updateSkipAFK(sender, args);
            }
        }  else if (Objects.equals(args[0], "status")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.isOp()) {
                    printStatus(sender);
                } else {
                    player.sendMessage(ChatColor.RED + "你没有权限执行此命令。");
                }
            } else {
                printStatus(sender);
            }
        }
        return true;
    }

    public void printStatus(CommandSender sender) {
        sender.sendMessage(ChatColor.GREEN + "====================");
        sender.sendMessage(ChatColor.GREEN + "| LiveBot 状态报告");
        sender.sendMessage(ChatColor.GREEN + "| 当前被直播玩家：" + ChatColor.YELLOW + LiveBot.getInstance().getCache().getCurrentFollowingPlayerName());
        sender.sendMessage(ChatColor.GREEN + "| 机器人名：" + ChatColor.YELLOW + LiveBot.getInstance().getConfigManager().getBotName());
        sender.sendMessage(ChatColor.GREEN + "| 聚焦时间：" + ChatColor.YELLOW + LiveBot.getInstance().getConfigManager().getFocusTime());
        sender.sendMessage(ChatColor.GREEN + "| 玩家是否可以拒绝：" + ChatColor.YELLOW + LiveBot.getInstance().getConfigManager().getCanBeMoved());
        sender.sendMessage(ChatColor.GREEN + "| 是否跳过挂机玩家：" + ChatColor.YELLOW + LiveBot.getInstance().getConfigManager().getSkipAFK());
        sender.sendMessage(ChatColor.GREEN + "====================");
    }

    private void updateSkipAFK(CommandSender sender, String[] args) {
        if (Objects.equals(args[1], "true")) {
            LiveBot.getInstance().getConfigManager().setSkipAFK(true);
            sender.sendMessage(ChatColor.GREEN + "允许玩家不被跟随。");
        } else if (Objects.equals(args[1], "false")) {
            LiveBot.getInstance().getConfigManager().setSkipAFK(false);
            sender.sendMessage(ChatColor.GREEN + "玩家现在无法摆脱机器人。");
        } else {
            sender.sendMessage(ChatColor.RED + "参数只能为true或false。");
        }
    }

    private void updateCanBeMoved(CommandSender sender, String[] args) {
        if (Objects.equals(args[1], "true")) {
            LiveBot.getInstance().getConfigManager().setCanBeMoved(true);
            sender.sendMessage(ChatColor.GREEN + "允许玩家不被跟随。");
        } else if (Objects.equals(args[1], "false")) {
            LiveBot.getInstance().getConfigManager().setCanBeMoved(false);
            sender.sendMessage(ChatColor.GREEN + "玩家现在无法摆脱机器人。");
        } else {
            sender.sendMessage(ChatColor.RED + "参数只能为true或false。");
        }
    }

    @Override
    public List<String> onTabComplete(org.bukkit.command.CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("reload", "setBot", "away", "setTime", "setCanBeMoved", "stop", "start", "skipAFK", "status");
        } else if (args.length == 2) {
            if (Objects.equals(args[0], "setBot")) {
                ArrayList<Player> playerList = new ArrayList<>(Bukkit.getOnlinePlayers());
                ArrayList<String> playerNames = new ArrayList<>();
                for (Player player : playerList) {
                    playerNames.add(player.getName());
                }
                playerNames.remove(LiveBot.getInstance().getConfigManager().getBotName());
                return playerNames;
            } else if (Objects.equals(args[0], "setTime")) {
                return Collections.singletonList("请输入时间(单位秒)");
            }  else if (Objects.equals(args[0], "setCanBeMoved") || Objects.equals(args[0], "skipAFK")) {
                return Arrays.asList("true", "false");
            } else {
                return Collections.emptyList();
            }
        } else {
            return Collections.emptyList();
        }
    }
}
