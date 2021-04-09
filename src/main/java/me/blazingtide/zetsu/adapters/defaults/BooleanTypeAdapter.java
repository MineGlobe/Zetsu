package me.blazingtide.zetsu.adapters.defaults;

import me.blazingtide.zetsu.adapters.ParameterAdapter;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class BooleanTypeAdapter implements ParameterAdapter<Boolean> {

    @Override
    public Boolean process(@NotNull String str) {
        if (str.equalsIgnoreCase("yes")) {
            return true;
        } else if (str.equalsIgnoreCase("no")) {
            return false;
        }
        return Boolean.valueOf(str);
    }

    @Override
    public void processException(@NotNull CommandSender sender, @NotNull String given, @NotNull Exception exception) {
        sender.sendMessage(ChatColor.RED + "'" + given + "' is not a valid boolean.");
    }

}
