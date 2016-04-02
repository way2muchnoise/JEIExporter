package jeiexporter.json;

import com.google.gson.stream.JsonWriter;
import jeiexporter.config.ConfigHandler;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.gui.ingredients.IGuiIngredient;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

public class JEIJsonWriter
{
    private FileWriter fileWriter;
    private JsonWriter jsonWriter;
    private String dir;

    private String getDir()
    {
        if (this.dir != null) return this.dir;
        this.dir = ConfigHandler.getConfigDir().getAbsolutePath() + "/exports/";
        new File(dir).mkdir();
        return this.dir;
    }

    public JEIJsonWriter(String filename) throws IOException
    {
        this.fileWriter = new FileWriter(getDir() + filename + ".json");
        this.jsonWriter = new JsonWriter(fileWriter);
        this.jsonWriter.setIndent("  ");
    }

    public void writeTitle(IRecipeCategory category) throws IOException
    {
        this.jsonWriter.beginObject();
        this.jsonWriter.name("category").value(category.getTitle());
        this.jsonWriter.name("uid").value(category.getUid());
        //this.jsonWriter.name("bg").value(category.getBackground()); // TODO add IDrawable adapter
        this.jsonWriter.name("recipes");
        this.jsonWriter.beginArray();
    }

    public void writeLayout(IRecipeLayout layout) throws IOException
    {
        this.jsonWriter.beginObject();
        writeItems(layout.getItemStacks().getGuiIngredients().values());
        writeFluids(layout.getFluidStacks().getGuiIngredients().values());
        this.jsonWriter.endObject();
    }

    public void writeItems(Collection<? extends IGuiIngredient<ItemStack>> ingredients) throws IOException
    {
        this.jsonWriter.name("ingredientItems");
        this.jsonWriter.beginArray();
        for (IGuiIngredient<ItemStack> ingredient : ingredients)
            writeItem(ingredient);
        this.jsonWriter.endArray();
    }

    public void writeItem(IGuiIngredient<ItemStack> ingredient) throws IOException
    {
        Adapters.itemIngredient.write(this.jsonWriter, ingredient);
    }

    public void writeFluids(Collection<? extends IGuiIngredient<FluidStack>> ingredients) throws IOException
    {
        this.jsonWriter.name("ingredientFluids");
        this.jsonWriter.beginArray();
        for (IGuiIngredient<FluidStack> ingredient : ingredients)
            writeFluid(ingredient);
        this.jsonWriter.endArray();
    }

    public void writeFluid(IGuiIngredient<FluidStack> ingredient) throws IOException
    {
        Adapters.fluidIngredient.write(this.jsonWriter, ingredient);
    }

    public void close() throws IOException
    {
        this.jsonWriter.endArray();
        this.jsonWriter.endObject();
        this.jsonWriter.flush();
        this.jsonWriter.close();
    }
}
