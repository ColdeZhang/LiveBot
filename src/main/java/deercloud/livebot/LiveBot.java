package deercloud.livebot;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class LiveBot extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("LiveBot正在启动。");
        m_instance = this;
        // Plugin startup logic
        m_config_manager = new ConfigManager(this);

        Bukkit.getPluginManager().registerEvents(new Events(), this);
        Objects.requireNonNull(Bukkit.getPluginCommand("livebot")).setExecutor(new Commands());
        Objects.requireNonNull(Bukkit.getPluginCommand("livebot")).setTabCompleter(new Commands());

        m_cache = new PlayerCache(m_instance.getServer().getOnlinePlayers());
        m_main_thread = new BotMainThread();
        restartBot();

        getLogger().info("LiveBot启动完成。");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        stopBot();
        getLogger().info("LiveBot已经关闭。");
    }


    public ConfigManager getConfigManager() {
        return m_config_manager;
    }

    public PlayerCache getCache() {
        return m_cache;
    }

    public static LiveBot getInstance() {
        return m_instance;
    }

    private static LiveBot m_instance;

    private ConfigManager m_config_manager;

    private PlayerCache m_cache;

    private BotMainThread m_main_thread;

    public void restartBot() {
        try {
            if (!m_main_thread.isCancelled()) {
                m_main_thread.cancel();
            }
        } catch (Exception ignored) {
        }
        m_main_thread.runTaskTimer(this, 0, m_config_manager.getFocusTime() * 20L);
    }

    public void stopBot() {
        try {
            m_main_thread.cancel();
        } catch (Exception ignored) {
        }
    }


}
