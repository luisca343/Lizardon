package es.allblue.lizardon.crafting;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.brewing.IBrewingRecipe;

import javax.annotation.Nonnull;


public class BrewingRecipe implements IBrewingRecipe {
    private ItemStack input;
    private ItemStack ingredient;
    private ItemStack output;

    public BrewingRecipe(ItemStack input, ItemStack ingredient, ItemStack output) {
        this.input = input;
        this.ingredient = ingredient;
        this.output = output;
    }

    @Override
    public boolean isInput(@Nonnull ItemStack input) {
        return input == this.input;
    }

    @Override
    public boolean isIngredient(@Nonnull ItemStack ingredient) {
        return ingredient == this.ingredient;
    }

    @Nonnull
    @Override
    public ItemStack getOutput(@Nonnull ItemStack input, @Nonnull ItemStack ingredient) {
        return this.output.copy();
    }
}
