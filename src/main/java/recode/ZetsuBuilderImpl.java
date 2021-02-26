package recode;

import org.bukkit.plugin.java.JavaPlugin;

public class ZetsuBuilderImpl implements ZetsuBuilder {

    private JavaPlugin plugin;
    private String fallbackPrefix;

    protected ZetsuBuilderImpl() {
    }

    @Override
    public ZetsuBuilder fallbackPrefix(String fallbackPrefix) {
        this.fallbackPrefix = fallbackPrefix;
        return this;
    }

    @Override
    public ZetsuBuilder plugin(JavaPlugin plugin) {
        this.plugin = plugin;
        return this;
    }

    @Override
    public Zetsu create() {
        return new Zetsu(plugin, fallbackPrefix);
    }
}
