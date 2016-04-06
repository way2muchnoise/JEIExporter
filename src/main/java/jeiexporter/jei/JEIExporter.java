package jeiexporter.jei;

import jeiexporter.json.JEIJsonWriter;
import jeiexporter.json.TooltipJsonMap;
import jeiexporter.render.Loading;
import jeiexporter.util.LogHelper;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class JEIExporter
{
    //TODO add single exports

    public static void exportAll()
    {
        export(LayoutFetcher.getInstance().fetchAll());
    }

    private static void export(Map<IRecipeCategory, List<IRecipeLayout>> map)
    {
        long lastUpdate = 0;
        int size = map.size();
        int index = 0;
        for (Map.Entry<IRecipeCategory, List<IRecipeLayout>> entry : map.entrySet())
        {
            if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) break;
            index++;
            List<IRecipeLayout> layouts = entry.getValue();
            int layoutsSize = layouts.size();
            try
            {
                JEIJsonWriter writer = new JEIJsonWriter(entry.getKey().getUid().replaceAll("[\\.\\s]", "_"));
                writer.writeTitle(entry.getKey());
                for (int i = 0; i < layoutsSize; i++)
                {
                    if (Minecraft.getSystemTime() - lastUpdate > 33) // 30 FPS
                    {
                        Loading.render(
                                I18n.format("Exporting all JEI categories"),
                                I18n.format("Exporting %s (%s/%s)", entry.getKey().getTitle(), index, size),
                                (index * 1F) / size,
                                I18n.format("%s/%s", i, layoutsSize),
                                (i * 1F) / layoutsSize
                        );
                        lastUpdate = Minecraft.getSystemTime();
                    }
                    writer.writeLayout(layouts.get(i));
                }
                writer.close();
                LogHelper.info("Saved category: " + entry.getKey().getTitle());
            } catch (IOException e)
            {
                e.printStackTrace();
                LogHelper.warn("Failed writing category: " + entry.getKey().getTitle());
            }
        }
        try
        {
            TooltipJsonMap.saveAsJson();
        } catch (IOException e)
        {
            e.printStackTrace();
            LogHelper.warn("Failed writing tooltip map");
        }
    }
}
