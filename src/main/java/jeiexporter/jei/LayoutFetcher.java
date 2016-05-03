package jeiexporter.jei;

import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.gui.Focus;
import mezz.jei.gui.IRecipeGuiLogic;
import mezz.jei.gui.RecipeGuiLogic;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LayoutFetcher
{
    private static LayoutFetcher instance;
    private IRecipeGuiLogic logic;

    public static LayoutFetcher getInstance()
    {
        if (instance == null)
            instance = new LayoutFetcher();
        return instance;
    }

    private LayoutFetcher()
    {
        this.logic = new RecipeGuiLogic();
    }

    public List<IRecipeLayout> getRecipes(ItemStack itemStack)
    {
        List<IRecipeLayout> list = new ArrayList<>();
        Focus focus = new Focus(itemStack);
        focus.setMode(Focus.Mode.OUTPUT);
        this.logic.setFocus(focus);
        this.logic.setRecipesPerPage(1);
        String startCategory = this.logic.getRecipeCategory().getUid();
        do
        {
            do
            {
                list.addAll(this.logic.getRecipeWidgets(0, 0, 0));
                this.logic.nextPage();
            } while (!this.logic.getPageString().startsWith("1/"));
            this.logic.nextRecipeCategory();
        } while (!this.logic.getRecipeCategory().getUid().equals(startCategory));
        return list;
    }

    public Map<IRecipeCategory, List<IRecipeLayout>> fetchAll()
    {
        Map<IRecipeCategory, List<IRecipeLayout>> map = new HashMap<>();
        this.logic.setCategoryFocus(JEIConfig.recipeCategoryUids());
        this.logic.setRecipesPerPage(1);
        String startCategory = this.logic.getRecipeCategory().getUid();
        do
        {
            List<IRecipeLayout> layouts = new ArrayList<>();
            do
            {
                layouts.addAll(this.logic.getRecipeWidgets(0, 0, 0));
                this.logic.nextPage();
            } while (!this.logic.getPageString().startsWith("1/"));
            map.put(this.logic.getRecipeCategory(), layouts);
            this.logic.nextRecipeCategory();
        } while (!this.logic.getRecipeCategory().getUid().equals(startCategory));
        return map;
    }
}
