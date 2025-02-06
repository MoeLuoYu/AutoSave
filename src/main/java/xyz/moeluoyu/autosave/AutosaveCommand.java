package xyz.moeluoyu.autosave;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AutosaveCommand implements CommandExecutor, TabCompleter {
    private final Main plugin;

    public AutosaveCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sendUsage(sender);
            return true;
        }

        if (args[0].equalsIgnoreCase("time")) {
            if (args.length == 1) {
                sender.sendMessage(ChatColor.YELLOW + "当前自动保存间隔为: " + plugin.getSaveInterval() + " 分钟。");
                return true;
            }
            try {
                int interval = Integer.parseInt(args[1]);
                if (interval > 0) {
                    plugin.setSaveInterval(interval);
                    sender.sendMessage(ChatColor.GREEN + "自动保存间隔已设置为: " + interval + " 分钟。");
                } else {
                    sender.sendMessage(ChatColor.RED + "保存间隔必须大于 0。");
                }
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "请输入一个有效的整数。");
            }
            return true;
        } else if (args[0].equalsIgnoreCase("now")) {
            sender.sendMessage(ChatColor.YELLOW + "正在保存所有世界...");
            // 执行 save-all 命令
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "save-all");
            // 使用 Bukkit 方法保存所有世界
            // List<World> worlds = plugin.getServer().getWorlds();
            // for (World world : worlds) {
            //     world.save();
            // }
            sender.sendMessage(ChatColor.GREEN + "所有世界已保存。");
            return true;
        } else if (args[0].equalsIgnoreCase("help")) {
            sendUsage(sender);
            return true;
        }

        sender.sendMessage(ChatColor.RED + "未知子命令，请使用 /autosave help 查看可用命令。");
        return false;
    }

    private void sendUsage(CommandSender sender) {
        sender.sendMessage(ChatColor.YELLOW + "============ AutoSave 帮助 ============");
        sender.sendMessage(ChatColor.YELLOW + "/autosave time [分钟数] - 设置自动保存间隔");
        sender.sendMessage(ChatColor.YELLOW + "/autosave now - 立即保存所有世界");
        sender.sendMessage(ChatColor.YELLOW + "/autosave help - 显示帮助信息");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("time", "now", "help");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("time")) {
            return Collections.singletonList("<分钟数>");
        }
        return new ArrayList<>();
    }
}