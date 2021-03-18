package me.blazingtide.zetsu.adapters.defaults;

import me.blazingtide.zetsu.adapters.ParameterAdapter;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class LongTypeAdapter implements ParameterAdapter<Long> {

    @Override
    public Long process(String str) {
        return Long.valueOf(str);
    }

    @Override
    public void processException(CommandSender sender, String given, Exception exception) {
        sender.sendMessage(ChatColor.RED + "'" + given + "' is not a valid number.");
    }

}
