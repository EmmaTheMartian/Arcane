package martian.arcane.datagen.client.lang;

import com.klikli_dev.modonomicon.api.datagen.AbstractModonomiconLanguageProvider;
import martian.arcane.ArcaneMod;
import martian.arcane.common.registry.ArcaneBlocks;
import martian.arcane.common.registry.ArcaneItems;
import net.minecraft.data.PackOutput;

public class ArcaneLanguageEnUsProvider extends AbstractModonomiconLanguageProvider {
    public ArcaneLanguageEnUsProvider(PackOutput output) {
        super(output, ArcaneMod.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        // Tabs
        {
            add("itemGroup.arcane.arcane_tab", "Arcane");
            add("itemGroup.arcane.arcane_spells_tab", "Arcane (Spells)");
        }

        // Items
        {
            // Wands
            addItem(ArcaneItems.WAND_ACACIA, "Acacia Wand");
            addItem(ArcaneItems.WAND_BAMBOO, "Bamboo Wand");
            addItem(ArcaneItems.WAND_BIRCH, "Birch Wand");
            addItem(ArcaneItems.WAND_CHERRY, "Cherry Wand");
            addItem(ArcaneItems.WAND_DARK_OAK, "Dark Oak Wand");
            addItem(ArcaneItems.WAND_JUNGLE, "Jungle Wand");
            addItem(ArcaneItems.WAND_MANGROVE, "Mangrove Wand");
            addItem(ArcaneItems.WAND_OAK, "Oak Wand");
            addItem(ArcaneItems.WAND_SPRUCE, "Spruce Wand");
            addItem(ArcaneItems.WAND_WARPED, "Warped Wand");
            addItem(ArcaneItems.WAND_CRIMSON, "Crimson Wand");
            addItem(ArcaneItems.WAND_COPPER, "Copper Wand");
            addItem(ArcaneItems.WAND_LARIMAR, "Larimar Wand");
            addItem(ArcaneItems.WAND_AURACHALCUM, "Aurachalcum Wand");
            addItem(ArcaneItems.WAND_ELDRITCH, "Eldritch Wand");

            // Tools
            addItem(ArcaneItems.AURAGLASS_BOTTLE, "Auraglass Bottle");
            addItem(ArcaneItems.MEDIUM_AURAGLASS_BOTTLE, "Medium Auraglass Bottle");
            addItem(ArcaneItems.LARGE_AURAGLASS_BOTTLE, "Large Auraglass Bottle");
            addItem(ArcaneItems.EXTREME_AURAGLASS_BOTTLE, "Extreme Auraglass Bottle");
            add("item.arcane.creative_auraglass_bottle", "Creative Auraglass Bottle");

            addItem(ArcaneItems.AURAOMETER, "Auraometer");
            addItem(ArcaneItems.AURA_WRENCH, "Aurawrench");
            addItem(ArcaneItems.AURA_CONFIGURATOR, "Configaurator");
            addItem(ArcaneItems.SPELL_TABLET, "Spell Tablet");
            addItem(ArcaneItems.SPELL_CHALK, "Spell Chalk");
            addItem(ArcaneItems.ARCANE_BLEACH, "Arcane Bleach");
            addItem(ArcaneItems.ENDERPACK, "Enderpack");
            addItem(ArcaneItems.AXOBOTTLE, "Axobottle");
            addItem(ArcaneItems.GEM_SAW, "Gem Saw");

            // Resources
            addItem(ArcaneItems.RAW_LARIMAR, "Raw Larimar");
            addItem(ArcaneItems.CUT_LARIMAR, "Cut Larimar");
            addItem(ArcaneItems.POLISHED_LARIMAR, "Polished Larimar");
            addItem(ArcaneItems.FADED_RAW_LARIMAR, "Faded Raw Larimar");
            addItem(ArcaneItems.FADED_CUT_LARIMAR, "Faded Cut Larimar");
            addItem(ArcaneItems.FADED_POLISHED_LARIMAR, "Faded Polished Larimar");
            addItem(ArcaneItems.RAW_IDOCRASE, "Raw Idocrase");
            addItem(ArcaneItems.CUT_IDOCRASE, "Cut Idocrase");
            addItem(ArcaneItems.POLISHED_IDOCRASE, "Polished Idocrase");
            addItem(ArcaneItems.RAW_AURACHALCUM, "Raw Aurachalcum");
            addItem(ArcaneItems.AURACHALCUM, "Aurachalcum");
            addItem(ArcaneItems.ELDRITCH_ALLOY, "Eldritch Alloy");

            addItem(ArcaneItems.COPPER_CORE, "Copper Focus");
            addItem(ArcaneItems.LARIMAR_CORE, "Larimar Focus");
            addItem(ArcaneItems.AURACHALCUM_CORE, "Aurachalcum Focus");
            addItem(ArcaneItems.ELDRITCH_CORE, "Eldritch Focus");
            addItem(ArcaneItems.SPELL_CIRCLE_CORE, "Spell Circle Focus");

            addItem(ArcaneItems.AURAGLASS_SHARD, "Auraglass Shard");
            addItem(ArcaneItems.AURAGLASS_DUST, "Auraglass Dust");

            addItem(ArcaneItems.CRUSHED_RAW_COPPER, "Crushed Raw Copper");
            addItem(ArcaneItems.CRUSHED_RAW_IRON, "Crushed Raw Iron");
            addItem(ArcaneItems.CRUSHED_RAW_GOLD, "Crushed Raw Gold");
            addItem(ArcaneItems.PURIFIED_RAW_COPPER, "Purified Raw Copper");
            addItem(ArcaneItems.PURIFIED_RAW_IRON, "Purified Raw Iron");
            addItem(ArcaneItems.PURIFIED_RAW_GOLD, "Purified Raw Gold");

            // Item Tooltips
            add("item.arcane.auraometer.tooltip", "Shows information about blocks that possess Aura when held.");
            add("item.arcane.aura_wrench.tooltip", "Use while sneaking to quickly break machinery.");
            add("item.arcane.aura_configurator.tooltip.1", "Used to configure and pair machinery.");
            add("item.arcane.aura_configurator.tooltip.2", "Pronounced config-aura-tor.");
            add("item.arcane.spell_tablet.tooltip", "Stores the spell inscribed upon it.");
        }

        // Blocks
        {
            addBlock(ArcaneBlocks.AURA_NODI, "Aura Nodi");

            addBlock(ArcaneBlocks.COPPER_AURA_EXTRACTOR, "Copper Aura Extractor");
            addBlock(ArcaneBlocks.LARIMAR_AURA_EXTRACTOR, "Larimar Aura Extractor");
            addBlock(ArcaneBlocks.AURACHALCUM_AURA_EXTRACTOR, "Aurachalcum Aura Extractor");

            addBlock(ArcaneBlocks.COPPER_AURA_INSERTER, "Copper Aura Inserter");
            addBlock(ArcaneBlocks.LARIMAR_AURA_INSERTER, "Larimar Aura Inserter");
            addBlock(ArcaneBlocks.AURACHALCUM_AURA_INSERTER, "Aurachalcum Aura Inserter");

            addBlock(ArcaneBlocks.COPPER_AURA_BASIN, "Copper Aura Basin");
            addBlock(ArcaneBlocks.LARIMAR_AURA_BASIN, "Larimar Aura Basin");
            addBlock(ArcaneBlocks.AURACHALCUM_AURA_BASIN, "Aurachalcum Aura Basin");

            addBlock(ArcaneBlocks.AURA_INFUSER, "Aura Infuser");
            addBlock(ArcaneBlocks.PEDESTAL, "Pedestal");

            addBlock(ArcaneBlocks.HEAT_COLLECTOR, "Ignis Collector");
            addBlock(ArcaneBlocks.AQUA_COLLECTOR, "Aqua Collector");

            addBlock(ArcaneBlocks.AURAGLASS, "Auraglass");
            addBlock(ArcaneBlocks.SOUL_MAGMA, "Soul Magma");

            addBlock(ArcaneBlocks.CONJURED_CRAFTING_TABLE, "Conjured Crafting Table");
            addBlock(ArcaneBlocks.CONJURED_BLOCK, "Conjured Block");

            addBlock(ArcaneBlocks.LARIMAR_ORE, "Larimar Ore");
            addBlock(ArcaneBlocks.FADING_LARIMAR_ORE, "Fading Larimar Ore");
            addBlock(ArcaneBlocks.FADED_LARIMAR_ORE, "Faded Larimar Ore");
            addBlock(ArcaneBlocks.DEEPSLATE_LARIMAR_ORE, "Deepslate Larimar Ore");
            addBlock(ArcaneBlocks.FADING_DEEPSLATE_LARIMAR_ORE, "Fading Deepslate Larimar Ore");
            addBlock(ArcaneBlocks.FADED_DEEPSLATE_LARIMAR_ORE, "Faded Deepslate Larimar Ore");

            addBlock(ArcaneBlocks.IDOCRASE_ORE, "Idocrase Ore");
            addBlock(ArcaneBlocks.DEEPSLATE_IDOCRASE_ORE, "Deepslate Idocrase Ore");
            addBlock(ArcaneBlocks.NETHER_IDOCRASE_ORE, "Idocrase Ore");
            addBlock(ArcaneBlocks.BLACKSTONE_IDOCRASE_ORE, "Idocrase-Guilded Blackstone");

            addBlock(ArcaneBlocks.FADED_LARIMAR_BLOCK, "Faded Larimar Block");
            addBlock(ArcaneBlocks.FADING_LARIMAR_BLOCK, "Fading Larimar Block");
            addBlock(ArcaneBlocks.LARIMAR_BLOCK, "Larimar Block");
            addBlock(ArcaneBlocks.AURACHALCUM_BLOCK, "Aurachalcum Block");
        }

        // Messages
        {
            add("messages.arcane.aura", "Aura: ");
            add("messages.arcane.en", "En: ");
            add("messages.arcane.elemental_energy", "Elemental Energy: ");
            add("messages.arcane.holding", "Holding: ");
            add("messages.arcane.mode", "Mode: ");
            add("messages.arcane.crafting", "Crafting: ");
            add("messages.arcane.linked_to", "Linked to: ");
            add("messages.arcane.is_linked", "Is Linked?: ");
            add("messages.arcane.infusing_progress", "Infusing Progress: ");
            add("messages.arcane.item_aura", "Item Aura: ");
            add("messages.arcane.generating", "Generating: ");
            add("messages.arcane.linking_from", "Linking From: ");
            add("messages.arcane.spell", "Spell: ");
            add("messages.arcane.no_spell", "No Spell!");
            add("messages.arcane.not_active", "No Spell!");
            add("messages.arcane.can_extract", "Aura Extractable?: ");
            add("messages.arcane.can_insert", "Aura Insertable?: ");
            add("messages.arcane.wand_level", "Wand Level: ");
            add("messages.arcane.spell_min_level", "Required Wand Level: ");
            add("messages.arcane.push_rate", "Push Rate: ");
            add("messages.arcane.cast_timer", "Cast Timer: ");
            add("messages.arcane.not_linked", "Not linked");
            add("messages.arcane.mode_crafting", "Crafting");
            add("messages.arcane.mode_filling", "Filling");
            add("messages.arcane.not_enough_aura", "Not enough aura");
            add("messages.arcane.cast_spell", "Casted spell");
            add("messages.arcane.linked", "Linked!");
            add("messages.arcane.selected", "Selected block");
            add("messages.arcane.pedestal_set_spell", "Spell set!");
            add("messages.arcane.invalid_wand_for_spell", "Invalid wand for spell.");
            add("messages.arcane.chance", "Chance: ");
            add("messages.arcane.distance_too_far", "Distance too far!");
            add("messages.arcane.unlinked", "Unlinked");
        }

        // GUI
        {
            add("gui.arcane.jei.aura_infusion", "Aura Infusion");
            add("gui.arcane.jei.pedestal", "Pedestal Interaction");
            add("gui.arcane.jei.hammering", "Hammering");
            add("gui.arcane.jei.cleansing", "Cleansing");
            add("gui.arcane.jei.purifying", "Purifying");

            add("config.jade.plugin_arcane.jade_auraometer_provider", "Auraometer");

            add("container.arcane.conjured_crafting_table", "Conjured Crafting Table");
            add("container.arcane.enderpack", "Enderpack");
        }

        // Tags
        {
            add("tag.item.arcane.wands", "Wands");
            add("tag.item.arcane.basic_wands", "Basic Wands");
            add("tag.item.arcane.advanced_wands", "Advanced Wands");
            add("tag.item.arcane.mystical_wands", "Mystical Wands");
            add("tag.item.arcane.crushed_dusts", "Crushed Dusts");
            add("tag.item.arcane.purified_dusts", "Purified Dusts");
        }

        // Keybindings
        {
            add("key.categories.arcane.arcane", "Arcane");
            add("key.arcane.open_enderpack", "Open Enderpack");
        }

        // Spells
        {
            add("spell.arcane.breaking.name", "Breaking");
            add("spell.arcane.hammering.name", "Hammering");
            add("spell.arcane.building.name", "Building");
            add("spell.arcane.dashing.name", "Dashing");
            add("spell.arcane.crafting.name", "Conjure Crafting Table");
            add("spell.arcane.cleansing.name", "Cleansing");
            add("spell.arcane.purifying.name", "Purification");
            add("spell.arcane.activator.name", "Activation");
            add("spell.arcane.preservation.name", "Preservation");

            add("spell.arcane.hammering.name.item", "Arcane Hammer");
            add("spell.arcane.breaking.name.item", "Arcane Pickaxe");
            add("spell.arcane.building.name.item", "Arcane Constructor");
            add("spell.arcane.dashing.name.item", "Arcane Dash");
            add("spell.arcane.crafting.name.item", "Arcane Crafting Table");
            add("spell.arcane.cleansing.name.item", "Arcane Cleansing");
            add("spell.arcane.purifying.name.item", "Arcane Purification");
            add("spell.arcane.activator.name.item", "Arcane Activation");
            add("spell.arcane.preservation.name.item", "Arcane Preservation");
        }
    }
}
