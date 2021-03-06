package com.worldciv.the60th;

import com.worldciv.commands.News;
import com.worldciv.commands.PartyCommand;
import com.worldciv.commands.Toggle;
import com.worldciv.events.player.*;
import com.worldciv.scoreboard.scoreboardManager;
import com.worldciv.utility.CraftingRecipes;
import com.worldciv.utility.FurnaceRecipes;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;
import java.util.logging.Logger;

import static com.worldciv.utility.utilityStrings.worldciv;

public class MainTorch extends JavaPlugin implements Listener{


    public static scoreboardManager scoreboardManager;
    public static Plugin plugin;


    public void onEnable() {

        getConfig().options().copyDefaults(true);

        if (getConfig().getString("newsmessage") == null) {
            getConfig().set("newsmessage", "          " + ChatColor.GRAY + "This must be a new server. Set a news message with /news set <message>");
        }
        saveConfig();

        scoreboardManager = new scoreboardManager();
        plugin = this;
        PluginDescriptionFile pdfFile = this.getDescription();
        Logger logger = Logger.getLogger("Minecraft");

        logger.info(pdfFile.getName()
                + "has successfully enabled. The current version is: "
                + pdfFile.getVersion());

        registerEvents();
        registerCommands();

        //Check time of day!
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            public void run(){
                Server server = getServer();
                long time = server.getWorld("world").getTime();

                if(time >= 13200 && time <=13239 ){
                    Bukkit.broadcastMessage(worldciv + ChatColor.GRAY + " It's getting dark...");
                } else if (time >= 22390 && time <= 22429){ //2399 is last tick or 2400? use 2399 for safety
                    Bukkit.broadcastMessage(worldciv + ChatColor.GRAY + " It appears morning is arising.");

                }

                for (Player players : Bukkit.getOnlinePlayers()) {
                    Location loc = players.getLocation();
                    World world = players.getWorld();
                    Biome biome = world.getBiome(loc.getBlockX(), loc.getBlockZ());

                    if (world.hasStorm()) {

                        if (!(biome == Biome.DESERT || biome == Biome.DESERT_HILLS | biome == Biome.MUTATED_DESERT || biome == Biome.MESA || biome == Biome.MESA_CLEAR_ROCK
                                || biome == Biome.MESA_ROCK || biome == Biome.MUTATED_MESA || biome == Biome.MUTATED_MESA_CLEAR_ROCK || biome == Biome.MUTATED_MESA_ROCK
                                || biome == Biome.SAVANNA || biome == Biome.SAVANNA_ROCK || biome == Biome.MUTATED_SAVANNA || biome == Biome.MUTATED_SAVANNA_ROCK)) {



                            if (world.getHighestBlockYAt(loc.getBlockX(), loc.getBlockZ()) < players.getLocation().getBlockY() + 1) {


                                ItemStack currentItem = players.getInventory().getItemInMainHand();
                                ItemStack offHandItem = players.getInventory().getItemInOffHand();

                                if (currentItem.getType() == Material.TORCH) {
                                    Random r = new Random();
                                    int chance = r.nextInt(1200000);

                                    if (chance < 150000) {
                                        if (currentItem.getAmount() > 1) {
                                            currentItem.setAmount(currentItem.getAmount() - 1);
                                            players.sendMessage(worldciv + ChatColor.GRAY + " The storm has made one of your torches useless!");
                                        } else {
                                            currentItem.setAmount(-1);
                                            players.sendMessage(worldciv + ChatColor.GRAY + " Your last torch  in your main hand was used!");
                                        }

                                    }
                                } else if (offHandItem.getType() == Material.TORCH) {
                                    Random r = new Random();
                                    int chance = r.nextInt(1200000);

                                    if (chance < 150000) {
                                        if (offHandItem.getAmount() > 1) {
                                            offHandItem.setAmount(offHandItem.getAmount() - 1);
                                            players.sendMessage(worldciv + ChatColor.GRAY + " The storm has made one of your torches useless!");
                                        } else {
                                            offHandItem.setAmount(-1);
                                            players.sendMessage(worldciv + ChatColor.GRAY + " Your last torch in your offhand was used!");
                                        }

                                    }
                                }
                            }
                        }
                    }
                }

            }
        }, 0, 40); //every 2s, try not to jam the server!

        for (Player p : Bukkit.getOnlinePlayers()){
            p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard()); //REMOVES CURRENT SB if at all any.
            scoreboardManager.setScoreboard(p);
        }

        CraftingRecipes.registerRecipes();
        FurnaceRecipes.registerFurnaceRecipes();

        Bukkit.broadcastMessage(worldciv + ChatColor.GRAY + " Refreshing plugin data.");
    }

    public void onDisable() {
        plugin = null;
        PluginDescriptionFile pdfFile = getDescription();
        Logger logger = Logger.getLogger("Minecraft");
        logger.info(pdfFile.getName() + "has successfully disabled.");
    }

    public void registerCommands(){
        getCommand("toggle").setExecutor(new Toggle());
        getCommand("news").setExecutor(new News());
        getCommand("party").setExecutor(new PartyCommand());
        getCommand("p").setExecutor(new PartyCommand());
        getCommand("t").setExecutor(new Toggle());
    }

    public void registerEvents(){
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new quit(), this);
        pm.registerEvents(new join(), this);
        pm.registerEvents(new commandPreprocess(), this);
        pm.registerEvents(new weatherChangeEvent(), this);
        pm.registerEvents(new anvilCreate(), this);
        pm.registerEvents(new craftCreate(), this);
        pm.registerEvents(new furnaceCreate(), this);

    }



    public static Plugin getPlugin() {
        return plugin;
    }

    public static scoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }



}







