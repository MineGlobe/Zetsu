package me.blazingtide.zetsu;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import me.blazingtide.zetsu.adapters.ParameterAdapter;
import me.blazingtide.zetsu.adapters.defaults.*;
import me.blazingtide.zetsu.model.CachedCommand;
import me.blazingtide.zetsu.model.annotations.Command;
import me.blazingtide.zetsu.permission.PermissionAttachment;
import me.blazingtide.zetsu.permission.impl.PermissionNode;
import me.blazingtide.zetsu.permission.impl.PermissionNodeAttachment;
import me.blazingtide.zetsu.processor.bukkit.BukkitCommand;
import me.blazingtide.zetsu.processor.impl.SpigotProcessor;
import me.blazingtide.zetsu.tabcomplete.TabCompleteHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
public class Zetsu {
    public static String CMD_SPLITTER = " "; //Splitter for commands / arguments

    //Storing labels && commands associated with the label is faster than looping through all of the labels for no reason.
    private final Map<String, List<CachedCommand>> labelMap = Maps.newHashMap();
    private final Map<Class<?>, ParameterAdapter<?>> parameterAdapters = Maps.newConcurrentMap(); //Multithreading :D
    private final Map<Class<? extends Annotation>, PermissionAttachment<? extends Annotation>> permissibleAttachments = Maps.newConcurrentMap();
    private final SpigotProcessor processor = new SpigotProcessor(this);
    private final TabCompleteHandler tabCompleteHandler = new TabCompleteHandler(this);
    private final JavaPlugin plugin;
    private CommandMap commandMap = getCommandMap();
    @Setter
    private String fallbackPrefix = "zetsu";

    public Zetsu(JavaPlugin plugin) {
        this.plugin = plugin;

        registerParameterAdapter(String.class, new StringTypeAdapter());
        registerParameterAdapter(Player.class, new PlayerTypeAdapter());
        registerParameterAdapter(Integer.class, new IntegerTypeAdapter());
        registerParameterAdapter(Double.class, new DoubleTypeAdapter());
        registerParameterAdapter(Boolean.class, new BooleanTypeAdapter());

        registerPermissibleAttachment(PermissionNode.class, new PermissionNodeAttachment());
    }

    public List<CachedCommand> registerCommands(Object... objects) {
        final ArrayList<CachedCommand> toReturn = Lists.newArrayList();


        for (Object object : objects) {
            registerCommand(object);
        }

        return toReturn;
    }

    public <T> void registerParameterAdapter(Class<T> clazz, ParameterAdapter<T> adapter) {
        parameterAdapters.putIfAbsent(clazz, adapter);
    }

    public <T extends Annotation> void registerPermissibleAttachment(Class<T> clazz, PermissionAttachment<T> attachment) {
        permissibleAttachments.putIfAbsent(clazz, attachment);
    }

    private List<CachedCommand> registerCommand(Object object) {
        final ArrayList<CachedCommand> toReturn = Lists.newArrayList();

        if (commandMap == null) {
            commandMap = getCommandMap();
        }

        for (Method method : object.getClass().getMethods()) {
            if (!method.isAnnotationPresent(Command.class)) {
                continue;
            }

            List<CachedCommand> commands = CachedCommand.of(method.getAnnotation(Command.class), method, object);

            for (CachedCommand command : commands) {
                org.bukkit.command.Command cmd = commandMap.getCommand(command.getLabel());

                if (cmd == null) {
                    BukkitCommand bukkitCommand = new BukkitCommand(command.getLabel(), processor, tabCompleteHandler);
                    bukkitCommand.setDescription(command.getDescription());

                    commandMap.register(fallbackPrefix, bukkitCommand);
                }

                labelMap.putIfAbsent(command.getLabel(), new ArrayList<>());
                labelMap.get(command.getLabel()).add(command);
                labelMap.get(command.getLabel()).sort((o1, o2) -> o2.getMethod().getParameterCount() - o1.getMethod().getParameterCount());
                toReturn.add(command);
            }
        }

        return toReturn;
    }

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
