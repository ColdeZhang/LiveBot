package deercloud.livebot;

import deercloud.livebot.Commands;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import deercloud.livebot.events.*;

import java.util.Objects;

public final class LiveBot extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("LiveBot正在启动。");
        m_instance = this;
        // Plugin startup logic
        m_config_manager = new ConfigManager(this);
        m_work_func = new WorkFunc(this);

        Bukkit.getPluginManager().registerEvents(new playerEvent(), this);
        Objects.requireNonNull(Bukkit.getPluginCommand("livebot")).setExecutor(new Commands());
        Objects.requireNonNull(Bukkit.getPluginCommand("livebot")).setTabCompleter(new Commands());

        m_work_func.reFindBot();
        getLogger().info("LiveBot启动完成。");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        m_work_func.stop();
        getLogger().info("LiveBot已经关闭。");
    }


    public ConfigManager getConfigManager() {
        return m_config_manager;
    }

    public WorkFunc getWorkFunc() {
        return m_work_func;
    }

    public static LiveBot getInstance() {
        return m_instance;
    }

    private static LiveBot m_instance;

    private ConfigManager m_config_manager;

    private WorkFunc m_work_func;

}
