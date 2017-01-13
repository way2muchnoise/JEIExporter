package jeiexporter.jei;

import jeiexporter.json.TooltipJsonMap;
import mezz.jei.api.gui.IGuiIngredient;
import mezz.jei.api.gui.IRecipeLayout;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CraftingTree
{
    private static Map<String, CraftingTree> cacheMap = new HashMap<>();

    public static void clearCache()
    {
        cacheMap.clear();
    }

    // TODO compress data structure
    private Map<String, Integer> count;
    private Map<String, CraftingTree> children;
    private Map<String, ItemStack> items;
    private IRecipeLayout craft;
    private ItemStack result;

    public CraftingTree(ItemStack itemStack)
    {
        this.result = itemStack;
        this.children = new HashMap<>();
        this.count = new HashMap<>();
        this.items = new HashMap<>();
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
        if (this.craft != null)
        {
            for (IGuiIngredient<ItemStack> ingredient : this.craft.getItemStacks().getGuiIngredients().values())
            {
                if (ingredient.isInput() && ingredient.getAllIngredients().size() > 0)
                {
                    String key = TooltipJsonMap.createRegName(ingredient.getAllIngredients().get(0));
                    Integer c = this.count.get(key);
                    if (c == null)
                    {
                        c = 0;
                        CraftingTree child = cacheMap.get(key);
                        if (child == null)
                        {
                            child = new CraftingTree(ingredient.getAllIngredients().get(0));
                            cacheMap.put(key, child);
                        }
                        this.children.put(key, child);
                        this.items.put(key, ingredient.getAllIngredients().get(0));
                    }
                    this.count.put(key, c+1);
                }
            }
        }
    }

    public List<ItemStack> getBaseItems()
    {
        List<ItemStack> stacks = new ArrayList<>();
        if (craft == null || this.children.size() < 1)
        {
            stacks.add(this.result);
            return stacks;
        }


        for (CraftingTree child : this.children.values())
            stacks.addAll(child.getBaseItems());
        return stacks;
    }

    public List<ItemStack> getCraftItems()
    {
        List<ItemStack> stacks = new ArrayList<>();
        for (Map.Entry<String, ItemStack> entry : this.items.entrySet())
        {
            ItemStack stack = entry.getValue().copy();
            stack.setCount(this.count.get(entry.getKey()));
            stacks.add(stack);
        }
        return stacks;
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
                count += ingredient.getAllIngredients().get(0).getCount();
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

    public String getResultRegName()
    {
        return TooltipJsonMap.createRegName(this.result);
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getResultRegName()).append(" ->");
        for (Map.Entry<String, Integer> entry : this.count.entrySet())
            sb.append(" ").append(entry.getValue()).append("x").append(this.children.get(entry.getKey()).getResultRegName()).append(",");
        sb.setLength(sb.length()-1);
        return sb.toString();
    }
}
