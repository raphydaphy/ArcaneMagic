package com.raphydaphy.arcanemagic.item;

import com.raphydaphy.arcanemagic.ArcaneMagic;
import com.raphydaphy.arcanemagic.api.scepter.ScepterPart;
import com.raphydaphy.arcanemagic.api.scepter.ScepterRegistry;
import com.raphydaphy.arcanemagic.client.IHasModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;

import javax.annotation.Nonnull;

/**
 * Created by Xander V on 29/09/2017.
 */
public class ItemTip extends ItemBase {
    public ItemTip() {
        super("sceptre_tip", Type.values().length);
        ScepterRegistry.registerAll(Type.IRON, Type.GOLD, Type.THAUMIUM);
    }

    @Override
    public void initModels(ModelRegistryEvent e) {
        int variants = Type.values().length;
        for (int i = 0; i < variants; i++)
        {
            IHasModel.sMRL("tips", this, i, "tip=" + Type.values()[i].getName() );
        }
    }

    public enum Type implements ScepterPart
    {
        IRON("iron"),
        GOLD("gold"),
        THAUMIUM("thaumium"),
        ;
    
        private final String UNLOC_NAME;
        private final ResourceLocation TEXTURE;
        private final ResourceLocation REGNAME;
        private final String name;
    
        Type(String name)
        {
            this(name, ArcaneMagic.MODID + ".tip." + name, new ResourceLocation(ArcaneMagic.MODID, name), new ResourceLocation(ArcaneMagic.MODID, "items/scepter/tip_" + name));
        }

        Type(String name, String unlocName, ResourceLocation regName, ResourceLocation tex){
            this.name = name;
            UNLOC_NAME = unlocName;
            REGNAME = regName;
            TEXTURE = tex;
        }

        public String getName() {
            return name;
        }

        @Override
        public String getUnlocalizedName()
        {
            return UNLOC_NAME;
        }
    
        @Override
        public @Nonnull
        ResourceLocation getTexture()
        {
            return TEXTURE;
        }
    
        @Override
        public ResourceLocation getRegistryName()
        {
            return REGNAME;
        }
    
        @Override
        public PartCategory getType() {
            return PartCategory.TIP;
        }
    }
}
