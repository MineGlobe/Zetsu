package me.blazingtide.zetsu.processor.bukkit;

import me.blazingtide.zetsu.Zetsu;
import me.blazingtide.zetsu.processor.impl.SpigotProcessor;
import me.blazingtide.zetsu.tabcomplete.TabCompleteHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class BukkitCommand extends Command {

    private final @NotNull SpigotProcessor processor;
    private final @NotNull TabCompleteHandler handler;

    private final String fallbackPrefix;

    public BukkitCommand(String name, @NotNull SpigotProcessor processor, @NotNull TabCompleteHandler handler, String fallbackPrefix) {
        super(name);
        this.processor = processor;
        this.handler = handler;
        this.fallbackPrefix = fallbackPrefix;
    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] strings) {
        if (s.startsWith(fallbackPrefix)) {
            s = s.replace(fallbackPrefix + ":", "");
        }

        return processor.onCommand(commandSender, this, s, strings);
    }
}
