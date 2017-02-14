package jeiexporter.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

public class Loading {
    public static void render(String title, String mainSubtitle, float mainProgress, String subSubtitle, float subProgress) {
        Minecraft mc = Minecraft.getMinecraft();
        mc.getFramebuffer().unbindFramebuffer();
        GlStateManager.pushMatrix();
        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
        /*
         * If you're not familiar, this call switches the rendering mode from
         * 3D perspective to 2D orthogonal.
         */
        mc.entityRenderer.setupOverlayRendering();
        // Draw the dirt background and status text...
        RenderAccess.drawBackground(res.getScaledWidth(), res.getScaledHeight());

        RenderAccess.drawCenteredString(mc.fontRenderer, title, res.getScaledWidth() / 2, res.getScaledHeight() / 2 - 24, -1);

        RenderAccess.drawRect(res.getScaledWidth() / 2 - 50, res.getScaledHeight() / 2 - 1, res.getScaledWidth() / 2 + 50, res.getScaledHeight() / 2 + 1, 0xFF001100);
        RenderAccess.drawRect(res.getScaledWidth() / 2 - 50, res.getScaledHeight() / 2 - 1, (res.getScaledWidth() / 2 - 50) + (int) (mainProgress * 100), res.getScaledHeight() / 2 + 1, 0xFF55FF55);

        RenderAccess.drawRect(res.getScaledWidth() / 2 - 50, res.getScaledHeight() / 2 + 20, res.getScaledWidth() / 2 + 50, res.getScaledHeight() / 2 + 22, 0xFF001100);
        RenderAccess.drawRect(res.getScaledWidth() / 2 - 50, res.getScaledHeight() / 2 + 20, (res.getScaledWidth() / 2 - 50) + (int) (subProgress * 100), res.getScaledHeight() / 2 + 22, 0xFF55FF55);

        GlStateManager.pushMatrix();
        GlStateManager.scale(0.5f, 0.5f, 1);
        RenderAccess.drawCenteredString(mc.fontRenderer, mainSubtitle, res.getScaledWidth(), res.getScaledHeight() - 20, -1);
        RenderAccess.drawCenteredString(mc.fontRenderer, subSubtitle, res.getScaledWidth(), res.getScaledHeight() + 18, -1);
        GlStateManager.popMatrix();
        GlStateManager.popMatrix();
        mc.updateDisplay();
        /*
		 * While OpenGL itself is double-buffered, Minecraft is actually *triple*-buffered.
		 * This is to allow shaders to work, as shaders are only available in "modern" GL.
		 * Minecraft uses "legacy" GL, so it renders using a separate GL context to this
		 * third buffer, which is then flipped to the back buffer with this call.
		 */
        mc.getFramebuffer().bindFramebuffer(false);
    }
}
