package me.blazingtide.zetsu;

import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import me.blazingtide.zetsu.adapters.ParameterAdapter;
import me.blazingtide.zetsu.adapters.defaults.*;
import me.blazingtide.zetsu.permissible.PermissibleAttachment;
import me.blazingtide.zetsu.permissible.impl.permissible.BukkitPermissionAttachment;
import me.blazingtide.zetsu.permissible.impl.permissible.Permissible;
import me.blazingtide.zetsu.processor.bukkit.BukkitCommand;
import me.blazingtide.zetsu.processor.impl.SpigotProcessor;
import me.blazingtide.zetsu.schema.CachedCommand;
import me.blazingtide.zetsu.schema.annotations.Command;
import me.blazingtide.zetsu.tabcomplete.TabCompleteHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
public class Zetsu {
    public static @NotNull String CMD_SPLITTER = " "; //Splitter for commands / arguments

    // Storing labels & commands associated with the label is faster
    // than looping through all of the labels for no reason.
    private final Map<String, List<CachedCommand>> labelMap = Maps.newHashMap();
    private final Map<Class<?>, ParameterAdapter<?>> parameterAdapters = Maps.newConcurrentMap(); //Multithreading :D
    private final Map<Class<? extends Annotation>, PermissibleAttachment<? extends Annotation>> permissibleAttachments
            = Maps.newConcurrentMap();
    private final SpigotProcessor processor = new SpigotProcessor(this);
    private final TabCompleteHandler tabCompleteHandler = new TabCompleteHandler(this);
    private final JavaPlugin plugin;
    private @Nullable CommandMap commandMap = getCommandMap();

    @Setter
    private @NotNull String fallbackPrefix = "zetsu";

    @Setter
    @Getter
    private boolean useDefaultsInTabComplete = false;

    public Zetsu(@NotNull JavaPlugin plugin) {
        this.plugin = plugin;

        registerParameterAdapter(String.class, new StringTypeAdapter());
        registerParameterAdapter(Player.class, new PlayerTypeAdapter());
        registerParameterAdapter(Integer.class, new IntegerTypeAdapter());
        registerParameterAdapter(Double.class, new DoubleTypeAdapter());
        registerParameterAdapter(Boolean.class, new BooleanTypeAdapter());
        registerParameterAdapter(Long.class, new LongTypeAdapter());

        registerPermissibleAttachment(Permissible.class, new BukkitPermissionAttachment());
    }

    /**
     * Register multiple commands using object instances.
     */
    public void registerCommands(@Nullable Object... objects) {
        for (Object object : objects) {
            registerCommand(object);
        }
    }

    /**
     * Register a ParameterAdapter
     *
     * @param clazz   The class for the adapter
     * @param adapter The adapter
     * @param <T>     Type
     */

    public <T> void registerParameterAdapter(@NotNull Class<T> clazz, @NotNull ParameterAdapter<T> adapter) {
        parameterAdapters.putIfAbsent(clazz, adapter);
    }

    /**
     * Register a PermissibleAttachment
     *
     * @param clazz      The class for the attachment
     * @param attachment The attachment
     * @param <T>        Type
     */
    public <T extends Annotation> void registerPermissibleAttachment(@NotNull Class<T> clazz,
                                                                     @NotNull PermissibleAttachment<T> attachment) {
        permissibleAttachments.putIfAbsent(clazz, attachment);
    }

    /**
     * Register a command using a object instance.
     */
    private void registerCommand(@Nullable Object object) {
        if (object == null) {
            return;
        }

        if (commandMap == null) {
            commandMap = getCommandMap();
        }

        for (Method method : object.getClass().getMethods()) {
            if (!method.isAnnotationPresent(Command.class)) {
                continue;
            }

            List<CachedCommand> commands = CachedCommand.of(method.getAnnotation(Command.class), method, object);

            for (CachedCommand command : commands) {
                if (commandMap != null) {
                    org.bukkit.command.Command cmd = commandMap.getCommand(command.getLabel());

                    if (cmd == null) {
                        BukkitCommand bukkitCommand = new BukkitCommand(
                                command.getLabel(),
                                processor,
                                tabCompleteHandler
                        );
                        bukkitCommand.setDescription(command.getDescription());

                        commandMap.register(fallbackPrefix, bukkitCommand);
                    }

                    labelMap.putIfAbsent(command.getLabel(), new ArrayList<>());
                    labelMap.get(command.getLabel()).add(command);
                    labelMap.get(command.getLabel()).sort((o1, o2) ->
                            o2.getMethod().getParameterCount() - o1.getMethod().getParameterCount());
                }
            }
        }
    }

    /**
     * Removes a command and unregisters it. WIP
     *
     * @param label Command Top Level Name so "/command test test1" would be "command"
     */
    //TODO: Make it object based.
    private boolean removeCommand(@NotNull String label) {
        if (commandMap != null) {
            labelMap.remove(label);
            return commandMap.getCommand(label).unregister(commandMap);
        }

        return false;
    }

    @Nullable
    private CommandMap getCommandMap() {
        final PluginManager manager = Bukkit.getPluginManager();

        try {
            Field field = manager.getClass().getDeclaredField("commandMap");

            field.setAccessible(true);

            return (CommandMap) field.get(manager);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }

        return null;
    }

}
