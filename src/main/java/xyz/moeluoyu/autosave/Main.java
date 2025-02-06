package xyz.moeluoyu.autosave;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class Main extends JavaPlugin {
    private int saveInterval;
    private int taskId;

    @Override
    public void onEnable() {
        // 加载配置文件
        saveDefaultConfig();
        FileConfiguration config = getConfig();
        saveInterval = config.getInt("save-interval", 5); // 默认 5 分钟

        AutosaveCommand commandExecutor = new AutosaveCommand(this);
        // 注册命令和 TabCompleter
        getCommand("autosave").setExecutor(commandExecutor);
        getCommand("autosave").setTabCompleter(commandExecutor);

        // 启动自动保存任务
        startAutoSaveTask();

        getLogger().info("AutoSave 已启用!");
        getLogger().info("定制插件找落雨，买插件上速德优，速德优（北京）网络科技有限公司出品，落雨QQ：1498640871");
    }

    @Override
    public void onDisable() {
        // 保存所有世界
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "save-all");
        // 停止自动保存任务
        Bukkit.getScheduler().cancelTask(taskId);
        getLogger().info("AutoSave 已禁用!");
    }

    public void startAutoSaveTask() {
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            getLogger().info("正在保存所有世界...");
            // 执行 save-all 命令
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "save-all");
            // List<World> worlds = Bukkit.getWorlds();
            // for (World world : worlds) {
            //     world.save();
            // }
            getLogger().info("所有世界已保存。");
        }, 0L, saveInterval * 60L * 20L);
    }

    public int getSaveInterval() {
        return saveInterval;
    }

    public void setSaveInterval(int interval) {
        this.saveInterval = interval;
        getConfig().set("save-interval", interval);
        saveConfig();
        Bukkit.getScheduler().cancelTask(taskId);
        startAutoSaveTask();
    }
}