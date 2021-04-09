package me.blazingtide.zetsu.adapters;

import me.blazingtide.zetsu.schema.CachedCommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ParameterAdapter<T> {

    /**
     * Called when it tries to get a object for a param
     *
     * @param input The input you receive like "hey"
     * @return T
     */
    @Nullable
    T process(@NotNull String input);

    /**
     * Called when there is a error like prasing error.
     *
     * @param sender    - Command Sender
     * @param given     - The input from proccess
     * @param exception - The error provided.
     */
    void processException(@NotNull CommandSender sender, @NotNull String given, @NotNull Exception exception);

    // Will not be called anymore.
    @Deprecated
    @Nullable //Label is not required idk what i was thinking because CachedCommand has it.
    default List<String> processTabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull CachedCommand command) {
        return null;
    }

    /**
     * Called when a sender tab's after a argument
     *
     * @param sender  The sender that tabbed
     * @param command The command its calling from
     * @return The results from the tab complete, can be null.
     */
    @Nullable
    default List<String> processTabComplete(@NotNull CommandSender sender, @NotNull CachedCommand command) {
        return null;
    }
}
