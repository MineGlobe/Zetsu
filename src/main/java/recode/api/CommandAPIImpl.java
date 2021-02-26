package recode.api;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.PluginManager;
import recode.model.CommandModel;

import java.lang.reflect.Field;
import java.util.List;

public class CommandAPIImpl implements CommandAPI {

    protected static Field COMMAND_MAP_FIELD;
    private CommandMap commandMap;

    @Override
    public List<CommandModel> registerCommands(Object... objects) {
        return null;
    }

    @Override
    public CommandMap getCommandMap() {
        final PluginManager manager = Bukkit.getPluginManager();

        try {
            if (COMMAND_MAP_FIELD == null) {
                COMMAND_MAP_FIELD = Bukkit.getPluginManager().getClass().getDeclaredField("commandMap");
                COMMAND_MAP_FIELD.setAccessible(true);
            }

            return (CommandMap) COMMAND_MAP_FIELD.get(manager);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }

        return null;
    }
}
