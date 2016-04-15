package jeiexporter.render;

import com.google.common.io.Files;
import jeiexporter.config.ConfigHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class RenderFluid
{
    public static final int FLUID_SIZE = 16;

    public static String render(FluidStack fluidStack)
    {
        RenderHelper.setupRenderState(FLUID_SIZE);
        String fluidName = fluidStack.getFluid().getName();
        String filename = fluidName.replaceAll(":", "_") + ".png";
        GlStateManager.pushMatrix();
        GlStateManager.clearColor(0, 0, 0, 0);
        GlStateManager.clear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        drawFluid(fluidStack, 0, 0, FLUID_SIZE, FLUID_SIZE);
        GlStateManager.popMatrix();
        try
        {
            File f = new File(ConfigHandler.getConfigDir(), "fluids/" + filename);
            if (f.exists()) return fluidName;
            /*
			 * We need to flip the image over here, because again, GL Y-zero is
			 * the bottom, so it's "Y-up". Minecraft's Y-zero is the top, so it's
			 * "Y-down". Since readPixels is Y-up, our Y-down render is flipped.
			 * It's easier to do this operation on the resulting image than to
			 * do it with GL transforms. Not faster, just easier.
			 */
            BufferedImage img = RenderHelper.createFlipped(RenderHelper.readPixels(FLUID_SIZE, FLUID_SIZE));
            Files.createParentDirs(f);
            f.createNewFile();
            ImageIO.write(img, "PNG", f);
            return fluidName;
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        RenderHelper.tearDownRenderState();
        return null;
    }

    private static void drawFluid(FluidStack fluidStack, int x, int y, int width, int height)
    {
        TextureMap textureMapBlocks = Minecraft.getMinecraft().getTextureMapBlocks();
        ResourceLocation fluidStill = fluidStack.getFluid().getStill(fluidStack);
        TextureAtlasSprite fluidStillSprite = null;
        if (fluidStill != null) {
            fluidStillSprite = textureMapBlocks.getTextureExtry(fluidStill.toString());
        }
        if (fluidStillSprite == null) {
            fluidStillSprite = textureMapBlocks.getMissingSprite();
        }

        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);

        int colour = fluidStack.getFluid().getColor(fluidStack);
        float red = (colour >> 16 & 0xFF) / 255.0F;
        float green = (colour >> 8 & 0xFF) / 255.0F;
        float blue = (colour & 0xFF) / 255.0F;
        GlStateManager.color(red, green, blue, 1.0F);

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(0, FLUID_SIZE, 0).tex(fluidStillSprite.getMinU(), fluidStillSprite.getMaxV()).endVertex();
        worldrenderer.pos(FLUID_SIZE, FLUID_SIZE, 0).tex(fluidStillSprite.getMaxU(), fluidStillSprite.getMaxV()).endVertex();
        worldrenderer.pos(FLUID_SIZE, 0, 0).tex(fluidStillSprite.getMaxU(), fluidStillSprite.getMinV()).endVertex();
        worldrenderer.pos(0, 0, 0).tex(fluidStillSprite.getMinU(), fluidStillSprite.getMinV()).endVertex();
        tessellator.draw();
    }
}
