package jeiexporter.proxy;

import jeiexporter.config.Settings;
import jeiexporter.handler.RenderHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy
{
    @Override
    public void registerKeyBindings()
    {
        super.registerKeyBindings();
        ClientRegistry.registerKeyBinding(Settings.openUI);
    }

    @Override
    public void registerHandlers()
    {
        super.registerHandlers();
        MinecraftForge.EVENT_BUS.register(new RenderHandler());
    }
}
