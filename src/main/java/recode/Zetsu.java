package recode;

import lombok.Data;
import org.bukkit.plugin.java.JavaPlugin;

@Data
public class Zetsu {

    private final JavaPlugin plugin;
    private final String fallbackPrefix;

    public static ZetsuBuilder of(JavaPlugin plugin) {
        return new ZetsuBuilderImpl().plugin(plugin);
    }

}
