package me.blazingtide.zetsu.processor.bukkit;

import me.blazingtide.zetsu.processor.impl.SpigotProcessor;
import me.blazingtide.zetsu.tabcomplete.TabCompleteHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BukkitCommand extends Command {

    private final @NotNull SpigotProcessor processor;
    private final @NotNull TabCompleteHandler handler;

    public BukkitCommand(String name, @NotNull SpigotProcessor processor, @NotNull TabCompleteHandler handler) {
        super(name);
        this.processor = processor;
        this.handler = handler;
    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] strings) {
        return processor.onCommand(commandSender, this, s, strings);
    }

    @Override
    @Nullable
    public List<String> tabComplete(@NotNull CommandSender sender,
                                    @NotNull String alias,
                                    @NotNull String[] args) throws IllegalArgumentException {
        //It's easier to separate chunks of code into different classes.
        return handler.getListener().onTabComplete(sender, this, alias, args);
    }
}
