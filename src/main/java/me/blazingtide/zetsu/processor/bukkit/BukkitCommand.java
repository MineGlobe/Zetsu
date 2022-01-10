package me.blazingtide.zetsu.processor.bukkit;

import me.blazingtide.zetsu.processor.impl.SpigotProcessor;
import me.blazingtide.zetsu.tabcomplete.TabCompleteHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

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
}
