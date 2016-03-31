package jeiexporter;

import jeiexporter.proxy.CommonProxy;
import jeiexporter.reference.MetaData;
import jeiexporter.reference.Reference;
import jeiexporter.util.LogHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Reference.ID, name = Reference.NAME, version = Reference.VERSION, guiFactory = "jeiexporter.gui.ModGuiFactory", dependencies = "after:JEI@[2.27.0,);", clientSideOnly = true)
public class JEIExporter
{
    @Mod.Metadata(Reference.ID)
    public static ModMetadata metadata;

    @SidedProxy(clientSide = "jeiexporter.proxy.ClientProxy", serverSide = "jeiexporter.proxy.CommonProxy")
    public static CommonProxy PROXY;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        LogHelper.info("Updating ModMetaData...");
        metadata = MetaData.init(metadata);
    }
}
