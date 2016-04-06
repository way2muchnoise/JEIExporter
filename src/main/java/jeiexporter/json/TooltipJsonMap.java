package jeiexporter.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;
import jeiexporter.config.ConfigHandler;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TooltipJsonMap
{
    private static Map<String, String> twoWayMap = new HashMap<>();

    public static void clear()
    {
        twoWayMap.clear();
    }

    public static void add(ItemStack itemStack)
    {
        String regName = itemStack.getItem().getRegistryName() + ":" + itemStack.getMetadata();
        String dispName = itemStack.getDisplayName();
        twoWayMap.put(regName, dispName);
        twoWayMap.put(dispName, regName);
    }

    public static void add(FluidStack fluidStack)
    {
        String regName = fluidStack.getFluid().getName();
        String dispName = fluidStack.getLocalizedName();
        twoWayMap.put(regName, dispName);
        twoWayMap.put(dispName, regName);
    }

    public static void asJson(File location) throws IOException
    {
        FileWriter writer = new FileWriter(location);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        writer.write(gson.toJson(twoWayMap));
        writer.flush();
        writer.close();
        clear();
    }

    public static void saveAsJson() throws IOException
    {
        asJson(new File(ConfigHandler.getConfigDir() + "/exports/tooltipMap.json"));
    }
}
