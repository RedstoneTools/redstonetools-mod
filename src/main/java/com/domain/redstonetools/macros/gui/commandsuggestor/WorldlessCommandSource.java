package com.domain.redstonetools.macros.gui.commandsuggestor;

import com.google.common.collect.Lists;
import net.minecraft.client.MinecraftClient;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Stream;

public class WorldlessCommandSource extends ServerCommandSource {

    private static final RecipeManager recipeManager = new RecipeManager();


    public WorldlessCommandSource() {
        super(null,null,null,null,0,null,null, null,null);
    }

    @Override
    public boolean hasPermissionLevel(int level) {
        return true;
    }

    @Override
    public Collection<String> getPlayerNames() {
        return Lists.newArrayList();
    }

    @Override
    public Collection<String> getTeamNames() {
        return Lists.newArrayList();
    }

    @Override
    public Collection<Identifier> getSoundIds() {
        return MinecraftClient.getInstance().getSoundManager().getKeys();
    }

    @Override
    public Stream<Identifier> getRecipeIds() {
        return recipeManager.keys();
    }

    @Override
    public Set<RegistryKey<World>> getWorldKeys() {
        return Set.of();
    }


}
