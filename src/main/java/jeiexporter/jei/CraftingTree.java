package jeiexporter.jei;

import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.gui.ingredients.IGuiIngredient;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CraftingTree
{
    private CraftingTree parent;
    private List<CraftingTree> children;
    private IRecipeLayout craft;

    public CraftingTree(CraftingTree parent, ItemStack itemStack)
    {
        this.parent = parent;
        this.craft = null;
        int inputCount = 0, outputCount = 99;
        for (IRecipeLayout layout : LayoutFetcher.getInstance().getRecipes(itemStack))
        {
            if (hasOutputStack(layout, itemStack) && (this.craft == null || outputCount > getOutputCount(layout) || inputCount < getInputCount(layout)))
            {
                this.craft = layout;
                inputCount = getInputCount(layout);
                outputCount = getOutputCount(layout);
            }
        }
        this.children = new ArrayList<>();
        if (this.craft != null)
        {
            for (IGuiIngredient<ItemStack> ingredient : this.craft.getItemStacks().getGuiIngredients().values())
                if (ingredient.isInput() && ingredient.getAllIngredients().size() > 0)
                    this.children.add(new CraftingTree(this, ingredient.getAllIngredients().get(0)));
        }
    }

    public CraftingTree(ItemStack itemStack)
    {
        this(null, itemStack);
    }

    private static int getInputCount(IRecipeLayout layout)
    {
        int count = 0;
        for (IGuiIngredient<ItemStack> ingredient : layout.getItemStacks().getGuiIngredients().values())
            if (ingredient.isInput())
                count++;
        return count;
    }

    private static int getOutputCount(IRecipeLayout layout)
    {
        int count = 0;
        for (IGuiIngredient<ItemStack> ingredient : layout.getItemStacks().getGuiIngredients().values())
            if (!ingredient.isInput())
                count += ingredient.getAllIngredients().get(0).stackSize;
        return count;
    }

    private static boolean hasOutputStack(IRecipeLayout layout, ItemStack itemStack)
    {
        for (IGuiIngredient<ItemStack> ingredient : layout.getItemStacks().getGuiIngredients().values())
            if (!ingredient.isInput()
                    && ingredient.getAllIngredients().size() > 0
                    && ItemStack.areItemsEqual(ingredient.getAllIngredients().get(0), itemStack))
                return true;
        return false;
    }
}
