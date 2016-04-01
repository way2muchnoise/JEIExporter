package jeiexporter.jei;

import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.JEIPlugin;

import javax.annotation.Nonnull;

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
}
