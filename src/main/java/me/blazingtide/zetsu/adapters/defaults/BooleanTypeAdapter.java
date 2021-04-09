package me.blazingtide.zetsu.adapters.defaults;

import me.blazingtide.zetsu.adapters.ParameterAdapter;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class BooleanTypeAdapter implements ParameterAdapter<Boolean> {

    @Override
    public Boolean process(@NotNull String str) {
        String lowered = str.toLowerCase();
        if (lowered.equals("yes")) {
            return true;
        } else if (lowered.equals("no")) {
            return false;
        }

        return lowered.equals("true");
    }

    @Override
    public void processException(@NotNull CommandSender sender, @NotNull String given, @NotNull Exception exception) {
        sender.sendMessage(ChatColor.RED + "'" + given + "' is not a valid boolean.");
    }
}
