package martian.arcane.datagen.server.book;

import com.klikli_dev.modonomicon.api.datagen.BookProvider;
import com.klikli_dev.modonomicon.api.datagen.CategoryProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookCategoryModel;
import com.klikli_dev.modonomicon.api.datagen.book.BookEntryModel;
import com.klikli_dev.modonomicon.api.datagen.book.BookEntryParentModel;
import com.klikli_dev.modonomicon.api.datagen.book.condition.BookAndConditionModel;
import com.klikli_dev.modonomicon.api.datagen.book.condition.BookEntryReadConditionModel;
import com.klikli_dev.modonomicon.api.datagen.book.page.*;
import com.klikli_dev.modonomicon.book.BookCategoryBackgroundParallaxLayer;
import martian.arcane.ArcaneMod;
import martian.arcane.ArcaneTags;
import martian.arcane.common.registry.ArcaneBlocks;
import martian.arcane.common.registry.ArcaneItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import java.util.function.BiFunction;

public class ArcaneBookFeaturesProvider extends CategoryProvider {
    public ArcaneBookFeaturesProvider(BookProvider parent) {
        super(parent, "features");
    }

    @Override
    protected String[] generateEntryMap() {
        return new String[]{
            "__#__0_____2_________",
            "________1____________",
            "_____________________",
            "_____5__4__3_________",
            "_____________________",
            "_____________________",
            "_____________________",
        };
    }

    @Override
    public void generateEntries() {
        var welcome = add(makeWelcomeEntry('#'));
        var pedestal = add(withParent(makePedestalEntry('0'), welcome));
        var aboutAura = add(withParent(makeAboutAuraEntry('1'), pedestal));
        var copper = add(withParent(makeCopperEntry('2'), aboutAura));
        var basicCollectors = add(withParent(makeBasicCollectorsEntry('3'), copper));
        var basicLogistics = add(withParent(makeBasicLogisticsEntry('4'), basicCollectors));
        var basicWand = add(withParent(makeBasicWandEntry('5'), basicLogistics));
    }

    @Override
    protected BookCategoryModel generateCategory() {
        lang().add(context().categoryName(), "Arcane");

        BiFunction<ResourceLocation, Float, BookCategoryBackgroundParallaxLayer> makeLayer = (loc, speed) ->
                new BookCategoryBackgroundParallaxLayer(loc, speed, -1);

        return BookCategoryModel.create(modLoc(context().categoryId()), context().categoryName())
                .withIcon(ArcaneItems.RAW_LARIMAR.get())
                .withBackgroundParallaxLayers(
                        makeLayer.apply(new ResourceLocation("modonomicon", "textures/gui/parallax/flow/base.png"), 0.7f),
                        makeLayer.apply(new ResourceLocation("modonomicon", "textures/gui/parallax/flow/1.png"), 1f),
                        makeLayer.apply(new ResourceLocation("modonomicon", "textures/gui/parallax/flow/2.png"), 1.4f)
                );
    }

    // Entries
    private BookEntryModel makeWelcomeEntry(char loc) {
        startEntry("welcome", "Hello Reader");

        var welcome = simpleTextPage("welcome", "Hello", """
                Hello dearest reader, I shall be your personal guide to the Arcane Arts henceforth.\\
                \\
                Also, would you put me on a pedestal? It is quite uncomfortable here in your... wait where are you keeping me? Your pocket?!?""");

        return createEntry(Items.COPPER_INGOT, loc, welcome);
    }

    private BookEntryModel makePedestalEntry(char loc) {
        startEntry("pedestal", "Pedestal");

        var pedestal = simpleCraftingPage("pedestal", ArcaneMod.id("shaped/pedestal"), """
                Even with such a simple Pedestal you may craft numerous resources, materials, and trinkets that are certainly needed on your journey.""");

        var putMeOnAPedestal = simpleTextPage("put_me_on_a_pedestal", "", """
                Now that you have a Pedestal, please put me on it. It is so much more comfortable there than in your hand.""");

        return createEntry(ArcaneBlocks.COPPER_AURA_BASIN.get().asItem(), loc, pedestal, putMeOnAPedestal);
    }

    private BookEntryModel makeAboutAuraEntry(char loc) {
        startEntry("about_aura", "About Aura");

        var aboutAura = simpleTextPage("about", "Aura", """
                Aura is the most important part of the Arts. To properly utilize it, you should know a little bit about it.\\
                \\
                First off, Aura is a form of energy that simply exists, I will tell you how it exists when you are ready.""");

        var aboutAura2 = simpleTextPage("about2", "Aura (cont.)", """
                Secondly, Aura can be used to cast spells, power Auric machines, and basically anything else you can think of.\\
                \\
                Finally, the flow of Aura can only be impeded by a small handful of materials. Some materials are better at others than this. One of these materials is the abundant Copper.""");

        return createEntry(ArcaneItems.AURAOMETER.get(), loc, aboutAura, aboutAura2);
    }

    private BookEntryModel makeCopperEntry(char loc) {
        startEntry("copper", "Copper");

        var copper = simpleTextPage("about", "Copper", """
                Copper is versatile beyond comprehension, especially in the application of the Arts.\\
                \\
                Copper is capable of partially restraining the flow of Aura, allowing you to store Aura.""");

        var basin = simpleCraftingPage("basin", ArcaneMod.id("shaped/aura_basin_copper"), """
                By simply making a Cauldron out of Copper and giving it a bit of pizzazz, you can store Aura.""");

        var auraometer = simpleCraftingPage("auraometer", ArcaneMod.id("shaped/auraometer"), """
                The Auraometer can be used to view details about any block that can store Aura. It will be immensely useful.""");

        var jadeTip = simpleTextPage("jade_tip", "Tip", """
                If you have the Jade mod installed, just having the Auraometer in your inventory lets you see details in the Jade view.""");

        return createEntry(ArcaneBlocks.COPPER_AURA_BASIN.get().asItem(), loc, copper, basin, auraometer, jadeTip);
    }

    private BookEntryModel makeBasicCollectorsEntry(char loc) {
        startEntry("basic_collectors", "Aura Collectors");

        var about = simpleTextPage("about", "Collectors", """
                You can generate Aura by using Aura Collectors. Simple, right? *Right?*""");

        var collectors = simpleCraftingPage("collector", ArcaneMod.id("shaped/ignis_collector"), ArcaneMod.id("shaped/aqua_collector"), """
                The Ignis Collector produces Aura when hot blocks- lava, fire, magma, etc- are placed underneath it.\\
                \\
                The Aqua Collector produces Aura based on the amount of Water around it.""");

        return createEntry(ArcaneBlocks.AQUA_COLLECTOR.get().asItem(), loc, about, collectors);
    }

    private BookEntryModel makeBasicLogisticsEntry(char loc) {
        startEntry("basic_logistics", "Basic Logistics");

        var about = simpleTextPage("about", "Logistics", """
                You can now generate Aura, but you also need to move it places. This is where Extractors and Inserters become useful.""");

        var extractorAndInserter = simpleCraftingPage("extractor_and_inserter", ArcaneMod.id("shaped/aura_extractor_copper"), ArcaneMod.id("shaped/aura_inserter_copper"), """
                """);

        var aurawrench = simpleCraftingPage("aurawrench", ArcaneMod.id("shaped/aura_wrench"), """
                The Aurawrench can be used to bind an Extractor to an Inserter to send Aura. First, click the Extractor, then the Inserter.""");

        return createEntry(ArcaneBlocks.COPPER_AURA_EXTRACTOR.get().asItem(), loc, about, extractorAndInserter, aurawrench);
    }

    private BookEntryModel makeBasicWandEntry(char loc) {
        this.startEntry("wand", "Wand");

        var wand = simpleSpotlightPage("wand", Ingredient.of(ArcaneTags.BASIC_WANDS), """
                Wands are perhaps even more important than Pedestals. Actually I take that back, Pedestals are more comfortable to sit on.\\
                \\
                Wands are capable of channeling Aura and casting spells to perform many common tasks.""");

        return createEntry(ArcaneItems.WAND_OAK.get(), loc, wand);
    }

    // Entry Helpers
    private BookEntryModel createEntry(Item icon, char loc, BookPageModel<?>... pages) {
        return BookEntryModel.create(ArcaneMod.id(context().categoryId() + "/" + context().entryId()), context().entryName())
                .withIcon(icon)
                .withLocation(entryMap().get(loc))
                .withPages(pages);
    }

    private void startEntry(String id, String name) {
        context().entry(id);
        lang().add(context().entryName(), name);
    }

    // Page Builders
    private BookSpotlightPageModel simpleSpotlightPage(String id, Ingredient spotlight, String text) {
        context().page(id);
        var page = BookSpotlightPageModel.create()
                .withItem(spotlight)
                .withText(context().pageText());
        lang().add(context().pageText(), text);
        return page;
    }

    private BookCraftingRecipePageModel simpleCraftingPage(String id, ResourceLocation recipeId, String text) {
        context().page(id);
        var page = BookCraftingRecipePageModel.create()
                .withRecipeId1(recipeId)
                .withText(context().pageText());
        lang().add(context().pageText(), text);
        return page;
    }

    private BookCraftingRecipePageModel simpleCraftingPage(String id, ResourceLocation recipeId, ResourceLocation recipeId2, String text) {
        context().page(id);
        var page = BookCraftingRecipePageModel.create()
                .withRecipeId1(recipeId)
                .withRecipeId2(recipeId2)
                .withText(context().pageText());
        lang().add(context().pageText(), text);
        return page;
    }

    private BookTextPageModel simpleTextPage(String id, String title, String text) {
        context().page(id);
        var page = BookTextPageModel.create()
                .withTitle(context().pageTitle())
                .withText(context().pageText());
        lang().add(context().pageTitle(), title);
        lang().add(context().pageText(), text);
        return page;
    }

    // Other Helpers
    private BookEntryModel withParent(BookEntryModel child, BookEntryModel parent) {
        return child.withParent(BookEntryParentModel.create(parent.getId()))
                .withCondition(requiresReadEntry(parent))
                .hideWhileLocked(true);
    }

    private BookAndConditionModel requiresReadEntry(BookEntryModel entry) {
        return BookAndConditionModel.create().withChildren(BookEntryReadConditionModel.create().withEntry(entry.getId()));
    }
}
