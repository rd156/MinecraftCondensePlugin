package rd156.minecraft.condense;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin ;
import rd156.minecraft.condense.commands.CondenseCommand;

public final class CondensePlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        saveDefaultConfig();
        System.out.println("Plugin Condense is started");
        this.getCommand("condense").setExecutor(new CondenseCommand(this));
    }

    @Override
    public void onDisable() {
        System.out.println("Plugin Condense is stopped");
    }
}
