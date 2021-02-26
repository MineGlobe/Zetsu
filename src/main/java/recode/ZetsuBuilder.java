package recode;

import org.bukkit.plugin.java.JavaPlugin;

public interface ZetsuBuilder {

    ZetsuBuilder fallbackPrefix(String fallbackPrefix);

    Zetsu create();

    ZetsuBuilder plugin(JavaPlugin plugin);

}
