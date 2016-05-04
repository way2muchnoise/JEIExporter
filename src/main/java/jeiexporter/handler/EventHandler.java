package jeiexporter.handler;

import jeiexporter.config.Settings;
import jeiexporter.jei.CraftingTree;
import jeiexporter.jei.JEIConfig;
import jeiexporter.jei.JEIExporter;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class EventHandler
{
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onFrameStart(TickEvent.RenderTickEvent event)
    {
        if (event.phase != TickEvent.Phase.START || !Settings.exportAll.isPressed()) return;
            JEIExporter.exportAll();
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event)
    {
        if (Settings.craftTree.isPressed())
        {
            CraftingTree tree = new CraftingTree(new ItemStack(Items.COMPARATOR));
            tree.getCraftItems();
            tree.getBaseItems();
            CraftingTree.clearCache();
        }
    }
}
