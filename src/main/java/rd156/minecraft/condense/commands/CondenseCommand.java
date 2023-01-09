package rd156.minecraft.condense.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import rd156.minecraft.condense.CondensePlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class CondenseCommand implements CommandExecutor {
    private CondensePlugin main;
    public CondenseCommand(CondensePlugin condensePlugin) {
        this.main = condensePlugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            Player player = (Player)sender;
            if(command.getName().equalsIgnoreCase("condense"))
            {
                int value = condense(player);
                String message = main.getConfig().getString("message.condense.resume").replace("[number]", String.valueOf(value));
                player.sendMessage(message);
                return true;
            }
        }
        return false;
    }

    private int condense(Player player)
    {
        Inventory inv = player.getInventory();
        Map ItemQty = new HashMap();
        for (ItemStack item : inv.getContents()) {
            if (item == null){
                continue;
            }
            Integer value = (Integer) ItemQty.get(item.getType());
            if (value == null)
            {
                ItemQty.put(item.getType(), item.getAmount());
            }
            else {
                ItemQty.put(item.getType(), value + item.getAmount());
            }
        }

        int nb_condense = condense_list(player, ItemQty);

        player.updateInventory();
        return (nb_condense);
    }
    private int condense_list(Player player, Map ItemQty)
    {
        AtomicInteger nb_condense = new AtomicInteger();
        main.getConfig().getConfigurationSection("condense").getKeys(false).forEach(key -> {
            Material material_in = Material.getMaterial(key.toUpperCase());
            String name_out = main.getConfig().getString("condense." + key + ".output");
            if (name_out != null)
            {
                Material material_out = Material.getMaterial(name_out.toUpperCase());
                Integer ratio_in = main.getConfig().getInt("condense." + key + ".ratio_in");
                Integer ratio_out = main.getConfig().getInt("condense." + key + ".ratio_out");
                if (material_in != null && material_out != null)
                {
                    nb_condense.addAndGet(change_item(player, material_in, (Integer) ItemQty.get(material_in), material_out, ratio_in, ratio_out));
                }
            }
        });
        return (nb_condense.get());
    }
    private int change_item(Player player, Material input, Integer qtt_input, Material output, Integer ratio_input, Integer ratio_output)
    {
        if (qtt_input == null)
        {
            //item doesn't in inventory
            return (0);
        }
        if (ratio_input == 0 || ratio_output == 0)
        {
            String item1 = " " + input;
            String message = main.getConfig().getString("message.error.ratio_zero").replace("[item1]", item1);
            player.sendMessage(message);
            return (0);
        }
        Integer qtt_rest = qtt_input % ratio_input;
        Integer qtt_out = qtt_input / ratio_input;
        Integer item_overflow = 0;

        if (qtt_out == 0)
        {
            return (0);
        }

        //Remove item Input
        player.getInventory().removeItem(new ItemStack(input, qtt_out * ratio_input));

        //Try to give item Output
        HashMap<Integer, ItemStack> errorItemStackHashMap = player.getInventory().addItem(new ItemStack(output, qtt_out));
        for (Map.Entry<Integer, ItemStack> entry : errorItemStackHashMap.entrySet()) {
           ItemStack value = entry.getValue();
           if (value != null){
               item_overflow += value.getAmount();
           }
        }
        // Overflow
        if (item_overflow > 0)
        {
            player.getInventory().removeItem(new ItemStack(output, qtt_out - item_overflow));
            player.getInventory().addItem(new ItemStack(input, qtt_out * ratio_input));
            if (main.getConfig().getBoolean("display.list"))
            {
                String item1 = (qtt_input - qtt_rest) + " " + input;
                String item2 = qtt_out + " " + output;
                String message = main.getConfig().getString("message.error.inventory_full").replace("[item1]", item1).replace("[item2]", item2);
                player.sendMessage(message);
            }
            return (0);
        }
        else
        {
            //Make stack of Item
            player.getInventory().removeItem(new ItemStack(input, qtt_rest));
            player.getInventory().addItem(new ItemStack(input, qtt_rest));
            if (main.getConfig().getBoolean("display.list"))
            {
                String item1 = (qtt_input - qtt_rest) + " " + input;
                String item2 = qtt_out + " " + output;
                String message = main.getConfig().getString("message.condense.item").replace("[item1]", item1).replace("[item2]", item2);
                player.sendMessage(message);
            }
            return (qtt_out);
        }
    }

}
