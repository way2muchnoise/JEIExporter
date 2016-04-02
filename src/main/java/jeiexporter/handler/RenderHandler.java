package jeiexporter.handler;

import jeiexporter.config.Settings;
import jeiexporter.jei.JEIExporter;
import jeiexporter.render.Loading;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

public class RenderHandler
{
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onFrameStart(TickEvent.RenderTickEvent event)
    {
        if (event.phase != TickEvent.Phase.START || !Keyboard.isKeyDown(Settings.openUI.getKeyCode())) return;
        JEIExporter.exportAll();
    }
}
