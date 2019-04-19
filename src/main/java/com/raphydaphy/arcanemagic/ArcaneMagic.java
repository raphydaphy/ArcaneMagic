package com.raphydaphy.arcanemagic;

import com.raphydaphy.arcanemagic.init.ModRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

public class ArcaneMagic implements ModInitializer {
    public static final String DOMAIN = "arcanemagic";
    public static final String PREFIX = DOMAIN + ":";
    public static final Random RANDOM = new Random();
    private static final Logger LOGGER = LogManager.getLogger();
    public static ItemGroup GROUP;

    public static Logger getLogger() {
        return LOGGER;
    }

    @Override
    public void onInitialize() {
        GROUP = FabricItemGroupBuilder.create(new Identifier(ArcaneMagic.DOMAIN, "items")).icon(() -> new ItemStack(ModRegistry.GOLDEN_SCEPTER)).build();
        ModRegistry.init();
    }
}
