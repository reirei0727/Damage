package damage.damage;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Damage extends JavaPlugin {
    public static Damage plugin;
    public Bar bar;

    @Override
    public void onEnable() {

        // Plugin startup logic
        plugin = this;
        getServer().getPluginManager().registerEvents(new onDamegeEvent(),this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
