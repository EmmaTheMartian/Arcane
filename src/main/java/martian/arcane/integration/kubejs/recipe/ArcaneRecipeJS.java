package martian.arcane.integration.kubejs.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import martian.arcane.api.recipe.RecipeOutput;
import net.minecraft.world.item.crafting.Ingredient;

public class ArcaneRecipeJS extends RecipeJS {
    @Override
    public InputItem readInputItem(Object from) {
        if (from instanceof JsonObject o) {
            Ingredient ingredient;
            int count = 1;

            if (o.has("value"))
                ingredient = Ingredient.fromJson(o.get("value"));
            else
                ingredient = Ingredient.fromJson(o);

            if (o.has("count"))
                count = o.get("count").getAsInt();
            else if (o.has("amount"))
                count = o.get("amount").getAsInt();

            return InputItem.of(ingredient, count);
        }

        return super.readInputItem(from);
    }

    @Override
    public JsonElement writeInputItem(InputItem item) {
        if (item.count > 1) {
            JsonObject object = new JsonObject();
            object.add("value", item.ingredient.toJson());
            object.addProperty("count", item.count);
            return object;
        }

        return item.ingredient.toJson();
    }

    @Override
    public JsonElement writeOutputItem(OutputItem item) {
        return new RecipeOutput(item.item, item.hasChance() ? (float)item.chance : 1).toJson();
    }

    @Override
    public OutputItem readOutputItem(Object from) {
        if (from instanceof JsonObject o) {
            RecipeOutput ro = RecipeOutput.fromJson(o);
            return OutputItem.of(ro.stack(), ro.chance());
        }

        return super.readOutputItem(from);
    }
}
