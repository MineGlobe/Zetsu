# Zetsu

Extensive Command API for the Spigot API

### Installation

1. Retrieve the jar in ``/target/`` or compile the plugin via git and maven.
2. Add the project as a maven dependency or put it in your build path.
3. Start using the API!

You can also use maven:

```xml

<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependency>
    <groupId>me.blazingtide</groupId>
    <artifactId>Zetsu</artifactId>
    <version>1.1-SNAPSHOT</version>
    <scope>compile</scope>
</dependency>
```

### Usage

```java
import org.bukkit.plugin.java.JavaPlugin;

public class Plugin extends JavaPlugin {

    private Zetsu zetsu;

    @Override
    public void onEnable() {
        zetsu = new Zetsu(this);

        zetzu.registerParameterAdapter(Object.class, new ObjectTypeAdapter());
        zetsu.registerCommands(this);
    }

    @Command(labels = {"example"})
    public void execute(CommandSender sender, @Param("message") /* You do not need @Param, but is used for help messages or it uses the class name. */ String exampleString) {
        sender.sendMessage(exampleString);
    }

    @Command(labels = {"example subcommand"})
    @Permissible("permission.admin")
    //Player requires the permission node "permission.admin" to perform this command
    public void executeSubCommand(Player player) { //Player only command
        player.sendMessage("You're a player!");
    }

    @Command(labels = {"example completable"})
    public void execute(CommandSender sender, /* Allows for "Hey!" to be tab completable */ @Completable({"Hey!"}) String exampleString) {
        sender.sendMessage(exampleString);
    }

    @Command(labels = {"example object"})
    public void execute(CommandSender sender, Object example) { //Object paraam type
        sender.sendMessage(example);
    }

    //Register Custom Type Adapters
    public class ObjectTypeAdapter implements ParameterAdapter<Object> {

        @Override
        public Object process(String str) {
            return str;
        }

        @Override
        public void processException(CommandSender sender, String given, Exception exception) {
            sender.sendMessage(ChatColor.RED + "'" + given + "' is not a valid object.");
        }
    }
}
```

### Contact

**Telegram**: @BlazingTide
**Discord**:  BlazingTide#0001
