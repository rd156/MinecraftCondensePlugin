package rd156.minecraft.condense.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import rd156.minecraft.condense.CondensePlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        int nb_condense = 0;
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

        nb_condense = condense_list(player, ItemQty);

        player.updateInventory();
        return (nb_condense);
    }
    private int condense_list(Player player, Map ItemQty)
    {
        int nb_condense = 0;

        //nugget
        if (main.getConfig().getBoolean("condense.iron_nugget") == true) {
            nb_condense += change_item(player, Material.IRON_NUGGET, (Integer) ItemQty.get(Material.IRON_NUGGET), Material.IRON_INGOT, 9, 1);
        }
        if (main.getConfig().getBoolean("condense.gold_nugget") == true) {
            nb_condense += change_item(player, Material.GOLD_NUGGET, (Integer) ItemQty.get(Material.GOLD_NUGGET), Material.GOLD_INGOT, 9, 1);
        }
        //ingot
        if (main.getConfig().getBoolean("condense.copper_ingot") == true) {
            nb_condense += change_item(player, Material.COPPER_INGOT, (Integer) ItemQty.get(Material.COPPER_INGOT), Material.COPPER_BLOCK, 9, 1);
        }
        if (main.getConfig().getBoolean("condense.iron_ingot") == true) {
            nb_condense += change_item(player, Material.IRON_INGOT, (Integer) ItemQty.get(Material.IRON_INGOT), Material.IRON_BLOCK, 9, 1);
        }
        if (main.getConfig().getBoolean("condense.raw_iron") == true) {
            nb_condense += change_item(player, Material.RAW_IRON, (Integer) ItemQty.get(Material.RAW_IRON), Material.RAW_IRON_BLOCK, 9, 1);
        }
        if (main.getConfig().getBoolean("condense.gold_ingot") == true) {
            nb_condense += change_item(player, Material.GOLD_INGOT, (Integer) ItemQty.get(Material.GOLD_INGOT), Material.GOLD_BLOCK, 9, 1);
        }
        if (main.getConfig().getBoolean("condense.netherite_ingot") == true) {
            nb_condense += change_item(player, Material.NETHERITE_INGOT, (Integer) ItemQty.get(Material.NETHERITE_INGOT), Material.NETHERITE_BLOCK, 9, 1);
        }
        if (main.getConfig().getBoolean("condense.diamond") == true) {
            nb_condense += change_item(player, Material.DIAMOND, (Integer) ItemQty.get(Material.DIAMOND), Material.DIAMOND_BLOCK, 9, 1);
        }
        if (main.getConfig().getBoolean("condense.emerald") == true) {
            nb_condense += change_item(player, Material.EMERALD, (Integer) ItemQty.get(Material.EMERALD), Material.EMERALD_BLOCK, 9, 1);
        }
        if (main.getConfig().getBoolean("condense.redstone") == true) {
            nb_condense += change_item(player, Material.REDSTONE, (Integer) ItemQty.get(Material.REDSTONE), Material.REDSTONE_BLOCK, 9, 1);
        }
        if (main.getConfig().getBoolean("condense.lapis_lazuli") == true) {
            nb_condense += change_item(player, Material.LAPIS_LAZULI, (Integer) ItemQty.get(Material.LAPIS_LAZULI), Material.LAPIS_BLOCK, 9, 1);
        }
        //Autre
        if (main.getConfig().getBoolean("condense.slime") == true) {
            nb_condense += change_item(player, Material.SLIME_BALL, (Integer) ItemQty.get(Material.SLIME_BALL), Material.SLIME_BLOCK, 9, 1);
        }
        if (main.getConfig().getBoolean("condense.snow") == true) {
            nb_condense += change_item(player, Material.SNOWBALL, (Integer) ItemQty.get(Material.SNOWBALL), Material.SNOW_BLOCK, 4, 1);
        }
        if (main.getConfig().getBoolean("condense.bone_meal") == true) {
            nb_condense += change_item(player, Material.BONE_MEAL, (Integer) ItemQty.get(Material.BONE_MEAL), Material.BONE_BLOCK, 9, 1);
        }
        if (main.getConfig().getBoolean("condense.melon_slice") == true) {
            nb_condense += change_item(player, Material.MELON_SLICE, (Integer) ItemQty.get(Material.MELON_SLICE), Material.MELON, 9, 1);
        }
        if (main.getConfig().getBoolean("condense.wheat") == true) {
            nb_condense += change_item(player, Material.WHEAT, (Integer) ItemQty.get(Material.WHEAT), Material.HAY_BLOCK, 9, 1);
        }
        return (nb_condense);
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
           String item = String.valueOf(value.getType());
        }
        // Overflow
        if (item_overflow > 0)
        {
            player.getInventory().removeItem(new ItemStack(output, qtt_out - item_overflow));
            player.getInventory().addItem(new ItemStack(input, qtt_out * ratio_input));
            if (main.getConfig().getBoolean("display.list"))
            {
                String item1 = String.valueOf(qtt_input - qtt_rest) + " " + input;
                String item2 = String.valueOf(qtt_out) + " " + output;
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
                String item1 = String.valueOf(qtt_input - qtt_rest) + " " + input;
                String item2 = String.valueOf(qtt_out) + " " + output;
                String message = main.getConfig().getString("message.condense.item").replace("[item1]", item1).replace("[item2]", item2);
                player.sendMessage(message);
            }
            return (qtt_out);
        }
    }

}
