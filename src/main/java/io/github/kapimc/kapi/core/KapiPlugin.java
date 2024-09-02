/*
 * Copyright (c) 2024 Kyren223
 * Licensed under the AGPLv3 license. See LICENSE or https://www.gnu.org/licenses/agpl-3.0 for details.
 */

package io.github.kapimc.kapi.core;

import io.github.kapimc.kapi.annotations.Kapi;
import io.github.kapimc.kapi.utility.Log;
import io.github.kapimc.kapi.utility.TaskBuilder;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.Nullable;

/**
 * This class should be extended by the main class of your plugin.
 * Extend this instead of {@link JavaPlugin}
 * <p>
 * Write your initialization code in {@link #onPluginLoad()}
 * and your unloading code in {@link #onPluginUnload()}.
 */
@Kapi
public abstract class KapiPlugin extends JavaPlugin {
    
    private static @Nullable KapiPlugin plugin;
    
    @Override
    public final void onEnable() {
        Log.info("Kapi has fully loaded!");
        try {
            onPluginPreload();
        } catch (RuntimeException e) {
            Log.error("An error occurred while preloading the plugin!");
            throw e;
        }
        TaskBuilder.create(() -> {
            try {
                onPluginLoad();
            } catch (RuntimeException e) {
                Log.error("An error occurred while loading the plugin!");
                throw e;
            }
        }).schedule();
    }
    
    @Override
    public final void onDisable() {
        try {
            onPluginUnload();
        } catch (RuntimeException e) {
            Log.error("An error occurred while unloading the plugin!");
            e.printStackTrace();
            Log.warn("Attempting to unload Kapi anyway...");
        }
        plugin = null;
        Log.info("Kapi has been unloaded!");
    }
    
    /**
     * Called immediately after Kapi has been fully loaded,
     * in the same server tick as the onEnable method.
     * <p>
     * For initializations, it's recommended to use {@link #onPluginLoad()} instead of this method,
     * this is due to how Bukkit/Spigot works, some methods like {@link Bukkit#broadcastMessage(String)}
     * will not work if called in the onEnable method.
     *
     * @since 0.1.0
     */
    @Kapi
    public void onPluginPreload() {
    }
    
    /**
     * Called one server tick after Kapi loads and {@link #onPluginPreload()} finishes.
     * This method should be used for initialization of the plugin.
     */
    @Kapi
    public abstract void onPluginLoad();
    
    /**
     * Called when the plugin is about to be disabled.
     * Kapi unloads only after this method finishes,
     * so you can still use Kapi methods here.
     */
    @Kapi
    public abstract void onPluginUnload();
    
    /**
     * @return The instance of the plugin
     * @throws IllegalStateException If the plugin has not been enabled yet
     */
    @Kapi
    public static KapiPlugin get() {
        if (plugin == null) {
            throw new IllegalStateException("Kapi has not been enabled yet!");
        }
        return plugin;
    }
    
    /**
     * Registers the given listener.
     *
     * @param listener An instance of a class that implements Listener
     */
    @Kapi
    public void registerEvent(Listener listener) {
        getServer().getPluginManager().registerEvents(listener, this);
    }
    
    /**
     * Registers a command.
     *
     * @param name      The name of the command
     * @param executor  The executor for the command
     * @param completer The tab completer for the command or null for no tab completer
     */
    @Kapi
    public void registerCommand(String name, CommandExecutor executor, @Nullable TabCompleter completer) {
        PluginCommand command = getCommand(name);
        if (command == null) {
            throw new IllegalArgumentException(
                "Command " + name + " does not exist! (did you add it to the plugin.yml file?)");
        }
        
        command.setExecutor(executor);
        if (completer != null) command.setTabCompleter(completer);
    }
}