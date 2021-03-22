package me.blazingtide.zetsu.tabcomplete.listener;

import com.google.common.collect.Maps;
import jdk.internal.joptsimple.internal.Strings;
import lombok.AllArgsConstructor;
import me.blazingtide.zetsu.Zetsu;
import me.blazingtide.zetsu.adapters.ParameterAdapter;
import me.blazingtide.zetsu.processor.CommandProcessor;
import me.blazingtide.zetsu.schema.CachedCommand;
import me.blazingtide.zetsu.schema.CachedTabComplete;
import me.blazingtide.zetsu.schema.annotations.parameter.Completable;
import me.blazingtide.zetsu.tabcomplete.TabCompleteHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.lang.reflect.Parameter;
import java.util.*;

@AllArgsConstructor
public class TabCompleteListener implements TabCompleter {

    private final Map<CachedCommand, Map<Integer, CachedTabComplete>> completionsCachce = Maps.newHashMap(); //Instead of looping every time, we store the constant args ONCE

    private final Zetsu zetsu;
    private final TabCompleteHandler handler;
    private final CommandProcessor processor;

    @Override
    public List<String> onTabComplete(CommandSender sender, Command ignored, String label, String[] args) {
        final CachedCommand command = processor.find(label.trim(), args);

        if (command == null) {
            return null; //should not happen but just incase
        }

        final List<String> toReturn = handler.requestSubcommands(args.length != 0 ? label + Zetsu.CMD_SPLITTER + Strings.join(args, Zetsu.CMD_SPLITTER) : label);

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

                completions.put(i++, new CachedTabComplete(parameterAdapter, complete));
            }

            return completions;
        });

        if (!cache.isEmpty() && cache.size() >= start) {
            CachedTabComplete complete = cache.get(start);

            if (complete != null){
                toReturn.addAll(complete.getConstant());

                if (complete.getParameterAdapter() != null){
                    List<String> tabComplete = complete.getParameterAdapter().processTabComplete(sender, label, command);

                    if (tabComplete != null){
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
