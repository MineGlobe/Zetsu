package me.blazingtide.zetsu.adapters;

import me.blazingtide.zetsu.schema.CachedCommand;
import org.bukkit.command.CommandSender;

import java.util.List;

public interface ParameterAdapter<T> {

    T process(String input);

    void processException(CommandSender sender, String given, Exception exception);

    default List<String> processTabComplete(CommandSender sender, String label, CachedCommand command) {
        return null;
    }
}
