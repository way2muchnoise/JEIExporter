package jeiexporter.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jeiexporter.config.ConfigHandler;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TooltipJsonMap
{
    private static Map<String, String> regToDisp = new HashMap<>();
    private static Map<String, String> dispToReg = new HashMap<>();
    private static Map<String, List<ItemStack>> doubles = new HashMap<>();

    public static void clear()
    {
        regToDisp.clear();
        dispToReg.clear();
        doubles.clear();
    }

    public static String add(ItemStack itemStack)
    {
        String regName = createRegName(itemStack);
        String dispName = itemStack.getDisplayName();
        regToDisp.put(regName, dispName);
        dispToReg.put(dispName, regName);
        return regName;
    }

    private static String createRegName(ItemStack itemStack)
    {
        String regName = itemStack.getItem().getRegistryName() + ":" + itemStack.getMetadata();
        List<ItemStack> stacks = doubles.get(regName);
        if(stacks == null) stacks = new ArrayList<>();
        String result = regName + ":" + stacks.size();
        boolean isIn = false;
        for (ItemStack stack : stacks)
            if (ItemStack.areItemStacksEqual(itemStack, stack))
                isIn = true;
        if (!isIn) stacks.add(itemStack);
        doubles.put(regName, stacks);
        return result;
    }

    public static String add(FluidStack fluidStack)
    {
        String regName = "fluid:" + fluidStack.getFluid().getName();
        String dispName = fluidStack.getLocalizedName();
        regToDisp.put(regName, dispName);
        dispToReg.put(dispName, regName);
        return regName;
    }

    public static void asJson(File location, Map<String, String> map) throws IOException
    {
        FileWriter writer = new FileWriter(location);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        writer.write(gson.toJson(map));
        writer.flush();
        writer.close();
    }

    public static void saveAsJson() throws IOException
    {
        asJson(new File(ConfigHandler.getConfigDir() + "/exports/tooltipMap.json"), regToDisp);
        asJson(new File(ConfigHandler.getConfigDir() + "/exports/lookupMap.json"), dispToReg);
        clear();
    }
}
