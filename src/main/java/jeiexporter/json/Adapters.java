package jeiexporter.json;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import jeiexporter.render.RenderFluid;
import jeiexporter.render.RenderIDrawable;
import jeiexporter.render.RenderItem;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiIngredient;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.gui.ingredients.GuiIngredient;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Field;

public class Adapters {
    public static final TypeAdapter<IRecipeCategory> drawable = new TypeAdapter<IRecipeCategory>() {
        @Override
        public void write(JsonWriter out, IRecipeCategory value) throws IOException {
            out.beginObject();
            IDrawable drawable = value.getBackground();
            out.name("w").value(drawable.getWidth());
            out.name("h").value(drawable.getHeight());
            out.name("tex").value(RenderIDrawable.render(drawable, value.getUid()));
            out.endObject();
        }

        @Override
        public IRecipeCategory read(JsonReader in) throws IOException {
            return null;
        }
    };

    public static final TypeAdapter<IGuiIngredient<ItemStack>> itemIngredient = new TypeAdapter<IGuiIngredient<ItemStack>>() {
        @Override
        public void write(JsonWriter out, IGuiIngredient<ItemStack> value) throws IOException {
            out.beginObject();
            Rectangle rect = getRect(value);
            out.name("x").value(rect.getX());
            out.name("y").value(rect.getY());
            out.name("w").value(rect.getWidth());
            out.name("h").value(rect.getHeight());
            out.name("p").value((getInt(xp, value) + (getInt(yp, value))) / 2);
            out.name("in").value(value.isInput());
            out.name("amount").value(value.getAllIngredients().size() > 0 ? value.getAllIngredients().get(0).getCount() : 0);
            out.name("stacks").beginArray();
            for (ItemStack itemStack : value.getAllIngredients())
                out.value(RenderItem.render(itemStack));
            out.endArray();
            out.endObject();
        }

        @Override
        public IGuiIngredient<ItemStack> read(JsonReader in) throws IOException {
            return null;
        }
    };

    public static final TypeAdapter<IGuiIngredient<FluidStack>> fluidIngredient = new TypeAdapter<IGuiIngredient<FluidStack>>() {
        @Override
        public void write(JsonWriter out, IGuiIngredient<FluidStack> value) throws IOException {
            out.beginObject();
            Rectangle rect = getRect(value);
            out.name("x").value(rect.getX());
            out.name("y").value(rect.getY());
            out.name("w").value(rect.getWidth());
            out.name("h").value(rect.getHeight());
            out.name("p").value((getInt(xp, value) + (getInt(yp, value))) / 2);
            out.name("in").value(value.isInput());
            out.name("amount").value(value.getAllIngredients().size() > 0 ? value.getAllIngredients().get(0).amount : 0);
            out.name("fluids").beginArray();
            for (FluidStack fluidStack : value.getAllIngredients())
                out.value(RenderFluid.render(fluidStack));
            out.endArray();
            out.endObject();
        }

        @Override
        public IGuiIngredient<FluidStack> read(JsonReader in) throws IOException {
            return null;
        }
    };

    private static Rectangle getRect(Object object) {
        try {
            return (Rectangle) rect.get(object);
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    private static int getInt(Field field, Object object) {
        try {
            return field.getInt(object);
        } catch (IllegalAccessException e) {
            return 0;
        }
    }

    private static Field rect;
    private static Field xp;
    private static Field yp;

    static {
        try {
            rect = GuiIngredient.class.getDeclaredField("rect");
            rect.setAccessible(true);
            xp = GuiIngredient.class.getDeclaredField("xPadding");
            xp.setAccessible(true);
            yp = GuiIngredient.class.getDeclaredField("yPadding");
            yp.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }


}
