package jeiexporter.config;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import org.lwjgl.input.Keyboard;

public class Settings
{
    public static KeyBinding exportAll = new KeyBinding("key.exportAll", KeyConflictContext.IN_GAME, KeyModifier.CONTROL, Keyboard.KEY_J, "key.categories.JEIE");
    public static KeyBinding craftTree = new KeyBinding("key.craftTree", KeyConflictContext.UNIVERSAL, Keyboard.KEY_J, "key.categories.JEIE");
}
