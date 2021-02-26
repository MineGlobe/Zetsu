package recode.api;

import org.bukkit.command.CommandMap;
import recode.model.CommandModel;

import java.util.List;

public interface CommandAPI {

    List<CommandModel> registerCommands(Object... objects);

    CommandMap getCommandMap();

}
