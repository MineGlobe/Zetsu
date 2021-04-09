package me.blazingtide.zetsu.schema;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import me.blazingtide.zetsu.Zetsu;
import me.blazingtide.zetsu.schema.annotations.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

@ToString
@Getter
public class CachedCommand {

    private final @NotNull String label;
    private final @NotNull List<String> args;

    private final @NotNull String description;
    private final boolean async;
    private final @NotNull Method method;
    private final @NotNull Object object;
    private final boolean playersOnly;

    public CachedCommand(@NotNull String label,
                         @NotNull List<String> args,
                         @NotNull String description,
                         boolean async,
                         @NotNull Method method,
                         @NotNull Object object) {
        this.label = label;
        this.args = args;
        this.description = description;
        this.async = async;
        this.method = method;
        this.object = object;
        this.playersOnly = method.getParameters()[0].getType() == Player.class;
    }

    //TODO: Add default parameters
    @NotNull
    public static List<CachedCommand> of(@NotNull Command annotation, @NotNull Method method, @NotNull Object object) {
        final List<CachedCommand> commands = Lists.newArrayList();

        for (String label : annotation.labels()) {
            final String[] split = label.split(Zetsu.CMD_SPLITTER);

            commands.add(new CachedCommand(
                    split[0],
                    Arrays.asList(split).subList(1, split.length),
                    annotation.description(),
                    annotation.async(),
                    method,
                    object)
            );
        }

        return commands;
    }
}
