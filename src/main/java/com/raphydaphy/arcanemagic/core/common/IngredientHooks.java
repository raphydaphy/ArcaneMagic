package com.raphydaphy.arcanemagic.core.common;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Ingredient.class)
public interface IngredientHooks {
    @Accessor("stackArray")
    ItemStack[] getCachedStackArray();

    @Invoker("createStackArray")
    void validateStackArray();
}
