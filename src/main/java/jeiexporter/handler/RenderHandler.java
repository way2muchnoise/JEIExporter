package jeiexporter.handler;

import jeiexporter.config.Settings;
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

        long lastUpdate = 0;
        int size = 100;
        for (int i = 0; i < size; i++)
        {
            if (Minecraft.getSystemTime()-lastUpdate > 33) // 30 FPS
            {
                Loading.render(I18n.format("Rendering %s items", size), I18n.format("Rendered %s/%s", i, size), (i*1F) / size);
                lastUpdate = Minecraft.getSystemTime();
            }
            try { Thread.sleep(10); } catch (InterruptedException ignored) {}
        }
    }
}
