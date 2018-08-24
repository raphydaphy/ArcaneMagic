package com.raphydaphy.arcanemagic.core;

import org.dimdev.riftloader.listener.InitializationListener;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

public class ArcaneMagicCore implements InitializationListener
{
    @Override
    public void onInitialization()
    {
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.arcanemagic.json");
    }
}
