package jeiexporter.render;

import com.google.common.io.Files;
import jeiexporter.config.ConfigHandler;
import mezz.jei.api.gui.IDrawable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class RenderIDrawable
{
    public static String render(IDrawable drawable, String bgName)
    {
        int mul = 2;
        RenderHelper.setupRenderStateWithMul(mul);
        String filename = bgName.replaceAll(":", "_") + ".png";
        GlStateManager.pushMatrix();
        GlStateManager.clearColor(0, 0, 0, 0);
        GlStateManager.clear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        drawable.draw(Minecraft.getMinecraft());
        GlStateManager.popMatrix();
        try
        {
            File f = new File(ConfigHandler.getConfigDir(), "bg/" + filename);
            //if (f.exists()) return bgName;
            /*
			 * We need to flip the image over here, because again, GL Y-zero is
			 * the bottom, so it's "Y-up". Minecraft's Y-zero is the top, so it's
			 * "Y-down". Since readPixels is Y-up, our Y-down render is flipped.
			 * It's easier to do this operation on the resulting image than to
			 * do it with GL transforms. Not faster, just easier.
			 */
            BufferedImage img = RenderHelper.createFlipped(RenderHelper.readPixels(drawable.getWidth() * mul, drawable.getHeight() * mul));
            Files.createParentDirs(f);
            f.createNewFile();
            ImageIO.write(img, "PNG", f);
            return bgName;
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        RenderHelper.tearDownRenderState();
        return null;
    }
}
