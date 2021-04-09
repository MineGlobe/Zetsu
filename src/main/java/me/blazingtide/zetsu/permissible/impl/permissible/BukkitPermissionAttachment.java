package me.blazingtide.zetsu.permissible.impl.permissible;

import me.blazingtide.zetsu.permissible.PermissibleAttachment;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class BukkitPermissionAttachment implements PermissibleAttachment<Permissible> {

    @Override
    public boolean test(Permissible annotation, @NotNull CommandSender sender) {
        return sender.hasPermission(annotation.value());
    }

    @Override
    public void onFail(@NotNull CommandSender sender, Permissible annotation) {
        sender.sendMessage(ChatColor.RED + "I'm sorry, but you do not have permission to perform this command. Please contact the server administrators if you believe that this is in error.");
    }
}
