package jeiexporter.render;

import com.google.common.io.Files;
import jeiexporter.config.ConfigHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class RenderItem
{
    public static final int ITEM_SIZE = 16 * 2;

    public static String render(ItemStack itemStack)
    {
        RenderHelper.setupRenderState(ITEM_SIZE);
        String itemName = itemStack.getItem().getRegistryName() + ":" + itemStack.getMetadata();
        String filename = itemName.replaceAll(":", "_") + ".png";
        GlStateManager.pushMatrix();
        GlStateManager.clearColor(0, 0, 0, 0);
        GlStateManager.clear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(itemStack, 0, 0);
        GlStateManager.popMatrix();
        try
        {
            File f = new File(ConfigHandler.getConfigDir(), "items/" + filename);
            if (f.exists()) return itemName;
            /*
			 * We need to flip the image over here, because again, GL Y-zero is
			 * the bottom, so it's "Y-up". Minecraft's Y-zero is the top, so it's
			 * "Y-down". Since readPixels is Y-up, our Y-down render is flipped.
			 * It's easier to do this operation on the resulting image than to
			 * do it with GL transforms. Not faster, just easier.
			 */
            BufferedImage img = RenderHelper.createFlipped(RenderHelper.readPixels(ITEM_SIZE, ITEM_SIZE));
            Files.createParentDirs(f);
            f.createNewFile();
            ImageIO.write(img, "PNG", f);
            return itemName;
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        RenderHelper.tearDownRenderState();
        return null;
    }
}
