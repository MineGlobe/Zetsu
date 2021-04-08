package me.blazingtide.zetsu.adapters.defaults;

import me.blazingtide.zetsu.adapters.ParameterAdapter;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class StringTypeAdapter implements ParameterAdapter<String> {

    @Override
    public String process(@NotNull String str) {
        return str;
    }

    @Override
    public void processException(@NotNull CommandSender sender, @NotNull String given, @NotNull Exception exception) {
        //Never
    }
}
