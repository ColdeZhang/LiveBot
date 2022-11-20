package deercloud.livebot;

import deercloud.livebot.LiveBot;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.logging.Logger;

public class ConfigManager {

    ConfigManager(LiveBot plugin) {
        this.m_plugin = plugin;
        m_plugin.saveDefaultConfig();
        m_logger = m_plugin.getLogger();
        reload();
    }

    public void reload() {
        m_plugin.reloadConfig();
        m_config_file = m_plugin.getConfig();
        m_bot_name = m_config_file.getString("Bot.Name", "Steve");
        m_focus_time = m_config_file.getInt("Setting.FocusTime", 5);
        m_change_pattern = m_config_file.getString("Setting.ChangingPattern", "ORDER");
        if (!m_change_pattern.equals("ORDER") && !m_change_pattern.equals("RANDOM")) {
            m_change_pattern = "ORDER";
            setChangePattern(m_change_pattern);
            m_logger.warning("ChangingPattern 配置错误，已重置为默认值 ORDER");
        }
        m_can_be_moved = m_config_file.getBoolean("Setting.CanBeMoved", true);
        m_is_nagging = m_config_file.getBoolean("Setting.IsNagging", true);
        m_logger.info("配置文件当前内容");
        m_logger.info("   -  直播机器人名称   ：" + m_bot_name);
        m_logger.info("   -  切换频率        ：" + m_focus_time + "秒");
        m_logger.info("   -  切换模式        ：" + m_change_pattern);
    }


    private final LiveBot m_plugin;
    private FileConfiguration m_config_file;
    private final Logger m_logger;

    private String m_bot_name;
    private int m_focus_time;
    private String m_change_pattern;
    private boolean m_can_be_moved;
    private boolean m_is_nagging;



    public String getBotName() {
        return m_bot_name;
    }

    public void setBotName(String name) {
        m_bot_name = name;
        m_config_file.set("Bot.Name", name);
        m_plugin.saveConfig();
    }

    public int getFocusTime() {
        return m_focus_time;
    }

    public void setFocusTime(int time) {
        m_focus_time = time;
        m_config_file.set("Setting.FocusTime", time);
        m_plugin.saveConfig();
    }

    public String getChangePattern() {
        return m_change_pattern;
    }

    public void setChangePattern(String pattern) {
        m_change_pattern = pattern;
        m_config_file.set("Setting.ChangingPattern", pattern);
        m_plugin.saveConfig();
    }

    public boolean getCanBeMoved() {
        return m_can_be_moved;
    }

    public void setCanBeMoved(boolean can_be_moved) {
        m_can_be_moved = can_be_moved;
        m_config_file.set("Setting.CanBeMoved", can_be_moved);
        m_plugin.saveConfig();
    }

    public boolean getIsNagging() {
        return m_is_nagging;
    }

    public void setIsNagging(boolean is_nagging) {
        m_is_nagging = is_nagging;
        m_config_file.set("Setting.IsNagging", is_nagging);
        m_plugin.saveConfig();
    }



}
