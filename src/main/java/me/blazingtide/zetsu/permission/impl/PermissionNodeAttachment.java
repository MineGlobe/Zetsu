package me.blazingtide.zetsu.permission.impl;

import me.blazingtide.zetsu.permission.PermissionAttachment;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class PermissionNodeAttachment implements PermissionAttachment<PermissionNode> {

    @Override
    public boolean test(PermissionNode annotation, CommandSender sender) {
        return sender.hasPermission(annotation.value());
    }

    @Override
    public void onFail(CommandSender sender, PermissionNode annotation) {
        sender.sendMessage(ChatColor.RED + "I'm sorry, but you do not have permission to perform this command. Please contact the server administrators if you believe that this is in error.");
    }
}
