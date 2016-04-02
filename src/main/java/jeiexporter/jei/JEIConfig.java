package jeiexporter.jei;

import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategory;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

@JEIPlugin
public class JEIConfig extends BlankModPlugin
{
    private static IJeiRuntime jeiRuntime;

    @Override
    public void onRuntimeAvailable(@Nonnull IJeiRuntime jeiRuntime)
    {
        JEIConfig.jeiRuntime = jeiRuntime;
    }

    public static IJeiRuntime getJeiRuntime()
    {
        return jeiRuntime;
    }

    public static List<String> recipeCategoryUids()
    {
        List<String> list = new ArrayList<>();
        for (IRecipeCategory category : jeiRuntime.getRecipeRegistry().getRecipeCategories())
            list.add(category.getUid());
        return list;
    }
}
