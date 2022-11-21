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
                if (m_config_manager.getCanBeMoved()) {
                    m_work_func.removePlayer(player);
                    player.sendMessage(ChatColor.GREEN + "机器人已经离开了你，本次登录不会再被选中。");
                    m_work_func.restart();
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
                        m_config_manager.setFocusTime(Integer.parseInt(args[1]));
                        player.sendMessage("聚焦时间已设置为" + args[1]);
                        m_work_func.restart();
                    }
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
                    m_work_func.stop();
                } else {
                    player.sendMessage(ChatColor.RED + "你没有权限执行此命令。");
                }
            } else {
                sender.sendMessage(ChatColor.GREEN + "机器人已停止。");
                m_work_func.stop();
            }
        } else if (Objects.equals(args[0], "start")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.isOp()) {
                    player.sendMessage(ChatColor.GREEN + "机器人已启动。");
                    m_work_func.reFindBot();
                } else {
                    player.sendMessage(ChatColor.RED + "你没有权限执行此命令。");
                }
            } else {
                sender.sendMessage("机器人已启动。");
                m_work_func.reFindBot();
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
        } else if (Objects.equals(args[0], "setAfkTime")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.isOp()) {
                    if (Integer.parseInt(args[1]) <= 10) {
                        player.sendMessage(ChatColor.RED + "时间间隔不能小于10秒。");
                    } else {
                        m_config_manager.setAfkTime(Integer.parseInt(args[1]));
                        player.sendMessage("挂机判断时间" + args[1]);
                        m_work_func.restart();
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "你没有权限执行此命令。");
                }
            } else {
                m_config_manager.setAfkTime(Integer.parseInt(args[1]));
                sender.sendMessage("挂机判断时间" + args[1]);
                m_work_func.restart();
            }
        } else if (Objects.equals(args[0], "status")) {
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
        sender.sendMessage(ChatColor.GREEN + "| 当前被直播玩家：" + ChatColor.YELLOW + m_work_func.getCurrentPlayerName());
        sender.sendMessage(ChatColor.GREEN + "| 机器人名：" + ChatColor.YELLOW + m_config_manager.getBotName());
        sender.sendMessage(ChatColor.GREEN + "| 聚焦时间：" + ChatColor.YELLOW + m_config_manager.getFocusTime());
        sender.sendMessage(ChatColor.GREEN + "| 切换方式：" + ChatColor.YELLOW + m_config_manager.getChangePattern());
        sender.sendMessage(ChatColor.GREEN + "| 玩家是否可以拒绝：" + ChatColor.YELLOW + m_config_manager.getCanBeMoved());
        sender.sendMessage(ChatColor.GREEN + "| 是否跳过挂机玩家：" + ChatColor.YELLOW + m_config_manager.getSkipAFK());
        sender.sendMessage(ChatColor.GREEN + "| 挂机判断时间：" + ChatColor.YELLOW + m_config_manager.getAFKTime());
        sender.sendMessage(ChatColor.GREEN + "====================");
    }

    private void updateSkipAFK(CommandSender sender, String[] args) {
        if (Objects.equals(args[1], "true")) {
            m_config_manager.setSkipAFK(true);
            sender.sendMessage(ChatColor.GREEN + "允许玩家不被跟随。");
        } else if (Objects.equals(args[1], "false")) {
            m_config_manager.setSkipAFK(false);
            sender.sendMessage(ChatColor.GREEN + "玩家现在无法摆脱机器人。");
        } else {
            sender.sendMessage(ChatColor.RED + "参数只能为true或false。");
        }
    }

    private void updateCanBeMoved(CommandSender sender, String[] args) {
        if (Objects.equals(args[1], "true")) {
            m_config_manager.setCanBeMoved(true);
            sender.sendMessage(ChatColor.GREEN + "允许玩家不被跟随。");
        } else if (Objects.equals(args[1], "false")) {
            m_config_manager.setCanBeMoved(false);
            sender.sendMessage(ChatColor.GREEN + "玩家现在无法摆脱机器人。");
        } else {
            sender.sendMessage(ChatColor.RED + "参数只能为true或false。");
        }
    }

    @Override
    public List<String> onTabComplete(org.bukkit.command.CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("reload", "setBot", "away", "setTime", "setPattern", "setCanBeMoved", "stop", "start", "skipAFK", "setAfkTime", "status");
        } else if (args.length == 2) {
            if (Objects.equals(args[0], "setBot")) {
                ArrayList<Player> playerList = new ArrayList<>(Bukkit.getOnlinePlayers());
                playerList.remove(m_work_func.getBot());
                ArrayList<String> playerNames = new ArrayList<>();
                for (Player player : playerList) {
                    playerNames.add(player.getName());
                }
                return playerNames;
            } else if (Objects.equals(args[0], "setTime") || Objects.equals(args[0], "setAfkTime")) {
                return Collections.singletonList("请输入时间(单位秒)");
            } else if (Objects.equals(args[0], "setPattern")) {
                return Arrays.asList("ORDER", "RANDOM");
            } else if (Objects.equals(args[0], "setCanBeMoved") || Objects.equals(args[0], "skipAFK")) {
                return Arrays.asList("true", "false");
            } else {
                return Collections.emptyList();
            }
        } else {
            return Collections.emptyList();
        }
    }



    protected ConfigManager m_config_manager = LiveBot.getInstance().getConfigManager();
    protected WorkFunc m_work_func = LiveBot.getInstance().getWorkFunc();
}
