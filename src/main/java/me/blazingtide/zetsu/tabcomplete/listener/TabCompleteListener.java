package me.blazingtide.zetsu.tabcomplete.listener;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import me.blazingtide.zetsu.Zetsu;
import me.blazingtide.zetsu.adapters.ParameterAdapter;
import me.blazingtide.zetsu.processor.CommandProcessor;
import me.blazingtide.zetsu.schema.CachedCommand;
import me.blazingtide.zetsu.schema.CachedTabComplete;
import me.blazingtide.zetsu.schema.annotations.parameter.Completable;
import me.blazingtide.zetsu.schema.annotations.parameter.Default;
import me.blazingtide.zetsu.tabcomplete.TabCompleteHandler;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Parameter;
import java.util.*;

@AllArgsConstructor
public class TabCompleteListener implements TabCompleter {

    private final @NotNull Map<CachedCommand, Map<Integer, CachedTabComplete>> completionsCachce = Maps.newHashMap(); //Instead of looping every time, we store the constant args ONCE

    private final @NotNull Zetsu zetsu;
    private final @NotNull TabCompleteHandler handler;
    private final @NotNull CommandProcessor processor;

    @Override
    @Nullable
    public List<String> onTabComplete(@NotNull CommandSender sender, @Nullable Command ignored, @NotNull String label, @NotNull String[] args) {
        final CachedCommand command = processor.find(label.trim(), args);

        if (command == null) {
            return null; //should not happen but just incase
        }

        List<String> toReturn = handler.requestSubcommands(args.length != 0 ? label + Zetsu.CMD_SPLITTER + StringUtils.join(args, Zetsu.CMD_SPLITTER) : label);

        if (toReturn == null) {
            toReturn = Lists.newArrayList();
        }

        int start = args.length - command.getArgs().size();

        Map<Integer, CachedTabComplete> cache = completionsCachce.computeIfAbsent(command, value -> {
            Map<Integer, CachedTabComplete> completions = Maps.newHashMap();

            int i = 1;
            for (Parameter parameter : value.getMethod().getParameters()) {
                if (parameter.getType() == CommandSender.class) continue;

                ParameterAdapter<?> parameterAdapter = zetsu.getParameterAdapters().get(parameter.getType());

                List<String> complete = new ArrayList<>();

                /* Values can change so we cannot do this. EX Bukkit.getOnlinePlayers();
                if (parameterAdapter != null && parameterAdapter.processTabComplete() != null) {
                    complete.addAll(parameterAdapter.processTabComplete());
                }*/

                if (parameter.isAnnotationPresent(Completable.class)) {
                    Completable completable = parameter.getAnnotation(Completable.class);

                    complete.addAll(Arrays.asList(completable.value()));
                }

                if (zetsu.isUseDefaultsInTabComplete() && parameter.isAnnotationPresent(Default.class)) {
                    complete.add(parameter.getAnnotation(Default.class).value());
                }

                completions.put(i++, new CachedTabComplete(parameterAdapter, complete));
            }

            return completions;
        });

        if (!cache.isEmpty() && cache.size() >= start) {
            CachedTabComplete complete = cache.get(start);

            if (complete != null) {
                toReturn.addAll(complete.getConstant());

                if (complete.getParameterAdapter() != null) {
                    List<String> tabComplete = complete.getParameterAdapter().processTabComplete(sender, command);

                    if (tabComplete != null) {
                        toReturn.addAll(tabComplete);
                    }
                }
            }
        }

        if (!toReturn.isEmpty()) {
            Collections.sort(toReturn);
        }

        return toReturn.isEmpty() ? null : toReturn;
    }
}
