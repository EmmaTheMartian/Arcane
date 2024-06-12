package martian.arcane.datagen.client;

import com.klikli_dev.modonomicon.api.datagen.AbstractModonomiconLanguageProvider;
import martian.arcane.ArcaneMod;
import net.minecraft.data.PackOutput;

import static martian.arcane.common.ArcaneContent.*;

public class ArcaneLanguageEnUsProvider extends AbstractModonomiconLanguageProvider {
    public ArcaneLanguageEnUsProvider(PackOutput output) {
        super(output, ArcaneMod.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        // Tabs
        add("itemGroup.arcane.arcane_tab", "Arcane");
        add("itemGroup.arcane.arcane_spells_tab", "Arcane (Spells)");

        // Items
        addItem(AURAGLASS_BOTTLE, "Auraglass Bottle");
        addItem(MEDIUM_AURAGLASS_BOTTLE, "Medium Auraglass Bottle");
        addItem(LARGE_AURAGLASS_BOTTLE, "Large Auraglass Bottle");
        addItem(EXTREME_AURAGLASS_BOTTLE, "Extreme Auraglass Bottle");
        add("item.arcane.creative_auraglass_bottle", "Creative Auraglass Bottle");

        addItem(WAND_ACACIA, "Acacia Wand");
        addItem(WAND_BAMBOO, "Bamboo Wand");
        addItem(WAND_BIRCH, "Birch Wand");
        addItem(WAND_CHERRY, "Cherry Wand");
        addItem(WAND_DARK_OAK, "Dark Oak Wand");
        addItem(WAND_JUNGLE, "Jungle Wand");
        addItem(WAND_MANGROVE, "Mangrove Wand");
        addItem(WAND_OAK, "Oak Wand");
        addItem(WAND_SPRUCE, "Spruce Wand");
        addItem(WAND_WARPED, "Warped Wand");
        addItem(WAND_CRIMSON, "Crimson Wand");
        addItem(WAND_COPPER, "Copper Wand");
        addItem(WAND_LARIMAR, "Larimar Wand");
        addItem(WAND_AURACHALCUM, "Aurachalcum Wand");
        addItem(WAND_ELDRITCH, "Eldritch Wand");
        addItem(WANDBOOK, "Wandbook");

        addItem(AURAOMETER, "Auraometer");
        addItem(AURA_WRENCH, "Aurawrench");
        addItem(AURA_CONFIGURATOR, "Configaurator");
        addItem(AURA_MULTITOOL, "Aura Multitool");
        addItem(SPELL_TABLET, "Spell Tablet");
        addItem(SPELL_CHALK, "Spell Chalk");
        addItem(ARCANE_BLEACH, "Arcane Bleach");
        addItem(ENDERPACK, "Enderpack");
        addItem(AXOBOTTLE, "Axobottle");
        addItem(GEM_SAW, "Gem Saw");
        addItem(UPGRADE_KIT_COPPER, "Copper Machine Parts");
        addItem(UPGRADE_KIT_LARIMAR, "Larimar Machine Parts");
        addItem(UPGRADE_KIT_AURACHALCUM, "Aurachalcum Machine Parts");

        addItem(RAW_LARIMAR, "Raw Larimar");
        addItem(CUT_LARIMAR, "Cut Larimar");
        addItem(POLISHED_LARIMAR, "Polished Larimar");
        addItem(FADED_RAW_LARIMAR, "Faded Raw Larimar");
        addItem(FADED_CUT_LARIMAR, "Faded Cut Larimar");
        addItem(FADED_POLISHED_LARIMAR, "Faded Polished Larimar");
        addItem(RAW_IDOCRASE, "Raw Idocrase");
        addItem(CUT_IDOCRASE, "Cut Idocrase");
        addItem(POLISHED_IDOCRASE, "Polished Idocrase");
        addItem(RAW_AURACHALCUM, "Raw Aurachalcum");
        addItem(AURACHALCUM, "Aurachalcum");
        addItem(ELDRITCH_ALLOY, "Eldritch Alloy");
        addItem(COPPER_CORE, "Copper Focus");
        addItem(LARIMAR_CORE, "Larimar Focus");
        addItem(AURACHALCUM_CORE, "Aurachalcum Focus");
        addItem(ELDRITCH_CORE, "Eldritch Focus");
        addItem(SPELL_CIRCLE_CORE, "Spell Circle Focus");
        addItem(AURAGLASS_SHARD, "Auraglass Shard");
        addItem(AURAGLASS_DUST, "Auraglass Dust");

        addItem(CRUSHED_RAW_COPPER, "Crushed Raw Copper");
        addItem(CRUSHED_RAW_IRON, "Crushed Raw Iron");
        addItem(CRUSHED_RAW_GOLD, "Crushed Raw Gold");
        addItem(PURIFIED_RAW_COPPER, "Purified Raw Copper");
        addItem(PURIFIED_RAW_IRON, "Purified Raw Iron");
        addItem(PURIFIED_RAW_GOLD, "Purified Raw Gold");

        add("item.arcane.auraometer.tooltip", "Shows information about blocks that possess Aura when held.");
        add("item.arcane.aura_wrench.tooltip.1", "Used to pair machinery.");
        add("item.arcane.aura_wrench.tooltip.2", "Use while sneaking to quickly break machinery.");
        add("item.arcane.aura_configurator.tooltip.1", "Used to configure machinery.");
        add("item.arcane.aura_configurator.tooltip.2", "Pronounced config-aura-tor.");
        add("item.arcane.aura_multitool.tooltip.1", "Functions universally as an Auraometer, Aura Wrench, and Configaurator.");
        add("item.arcane.aura_multitool.tooltip.2", "Use while crouching to change modes.");
        add("item.arcane.spell_tablet.tooltip", "Stores the spell inscribed upon it.");

        // Blocks
        addBlock(AURA_CONNECTOR.block(), "Aura Connector");
        addBlock(AURA_BASIN.block(), "Aura Basin");
        addBlock(AURA_INFUSER.block(), "Aura Infuser");
        addBlock(PEDESTAL.block(), "Pedestal");
        addBlock(HEAT_COLLECTOR.block(), "Ignis Collector");
        addBlock(AQUA_COLLECTOR.block(), "Aqua Collector");

        addBlock(AURAGLASS, "Auraglass");
        addBlock(SOUL_MAGMA, "Soul Magma");

        addBlock(CONJURED_CRAFTING_TABLE, "Conjured Crafting Table");
        addBlock(CONJURED_BLOCK, "Conjured Block");

        addBlock(LARIMAR_ORE, "Larimar Ore");
        addBlock(FADING_LARIMAR_ORE, "Fading Larimar Ore");
        addBlock(FADED_LARIMAR_ORE, "Faded Larimar Ore");
        addBlock(DEEPSLATE_LARIMAR_ORE, "Deepslate Larimar Ore");
        addBlock(FADING_DEEPSLATE_LARIMAR_ORE, "Fading Deepslate Larimar Ore");
        addBlock(FADED_DEEPSLATE_LARIMAR_ORE, "Faded Deepslate Larimar Ore");

        addBlock(IDOCRASE_ORE, "Idocrase Ore");
        addBlock(DEEPSLATE_IDOCRASE_ORE, "Deepslate Idocrase Ore");
        addBlock(NETHER_IDOCRASE_ORE, "Idocrase Ore");
        addBlock(BLACKSTONE_IDOCRASE_ORE, "Idocrase-Guilded Blackstone");

        addBlock(FADED_LARIMAR_BLOCK, "Faded Larimar Block");
        addBlock(FADING_LARIMAR_BLOCK, "Fading Larimar Block");
        addBlock(LARIMAR_BLOCK, "Larimar Block");
        addBlock(AURACHALCUM_BLOCK, "Aurachalcum Block");

        // Messages
        add("messages.arcane.aura", "Aura: ");
        add("messages.arcane.holding", "Holding: ");
        add("messages.arcane.mode", "Mode: ");
        add("messages.arcane.set_mode_to", "Set mode to ");
        add("messages.arcane.crafting", "Crafting: ");
        add("messages.arcane.linked_to", "Linked to: ");
        add("messages.arcane.is_linked", "Is Linked?: ");
        add("messages.arcane.infusing_progress", "Infusing Progress: ");
        add("messages.arcane.item_aura", "Item Aura: ");
        add("messages.arcane.generating", "Generating: ");
        add("messages.arcane.linking_from", "Linking From: ");
        add("messages.arcane.spell", "Spell: ");
        add("messages.arcane.no_spell", "No Spell!");
        add("messages.arcane.not_active", "Not Active!");
        add("messages.arcane.can_extract", "Aura Extractable?: ");
        add("messages.arcane.can_insert", "Aura Insertable?: ");
        add("messages.arcane.wand_level", "Wand Level: ");
        add("messages.arcane.spell_min_level", "Required Wand Level: ");
        add("messages.arcane.push_rate", "Push Rate: ");
        add("messages.arcane.cast_timer", "Cast Timer: ");
        add("messages.arcane.selection", "Selection: ");
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
        add("messages.arcane.tier", "Tier: ");

        // GUI
        add("gui.arcane.jei.aura_infusion", "Aura Infusion");
        add("gui.arcane.jei.pedestal", "Pedestal Interaction");
        add("gui.arcane.jei.hammering", "Hammering");
        add("gui.arcane.jei.cleansing", "Cleansing");
        add("gui.arcane.jei.purifying", "Purifying");

        add("emi.category.arcane.aura_infusion", "Aura Infusion");
        add("emi.category.arcane.pedestal", "Pedestal Interaction");
        add("emi.category.arcane.spell_hammering", "Hammering");
        add("emi.category.arcane.spell_cleansing", "Cleansing");
        add("emi.category.arcane.spell_purifying", "Purifying");

        add("config.jade.plugin_arcane.jade_auraometer_provider", "Auraometer");

        add("container.arcane.conjured_crafting_table", "Conjured Crafting Table");
        add("container.arcane.enderpack", "Enderpack");

        // Tags
        add("tag.item.arcane.wands", "Wands");
        add("tag.item.arcane.basic_wands", "Basic Wands");
        add("tag.item.arcane.advanced_wands", "Advanced Wands");
        add("tag.item.arcane.mystical_wands", "Mystical Wands");
        add("tag.item.arcane.crushed_dusts", "Crushed Dusts");
        add("tag.item.arcane.purified_dusts", "Purified Dusts");

        // Keybindings
        add("key.categories.arcane.arcane", "Arcane");
        add("key.arcane.open_enderpack", "Open Enderpack");
        add("key.arcane.wandbook_next_wand", "Next Wandbook Wand");
        add("key.arcane.wandbook_prev_wand", "Previous Wandbook Wand");

        // Spells
        add("spell.arcane.breaking.name", "Breaking");
        add("spell.arcane.hammering.name", "Hammering");
        add("spell.arcane.building.name", "Building");
        add("spell.arcane.dashing.name", "Dashing");
        add("spell.arcane.crafting.name", "Conjure Crafting Table");
        add("spell.arcane.cleansing.name", "Cleansing");
        add("spell.arcane.purifying.name", "Purification");
        add("spell.arcane.activator.name", "Activation");
        add("spell.arcane.preservation.name", "Preservation");
        add("spell.arcane.lighting.name", "Lighting");

        add("spell.arcane.hammering.name.item", "Arcane Hammer");
        add("spell.arcane.breaking.name.item", "Arcane Pickaxe");
        add("spell.arcane.building.name.item", "Arcane Constructor");
        add("spell.arcane.dashing.name.item", "Arcane Dash");
        add("spell.arcane.crafting.name.item", "Arcane Crafting Table");
        add("spell.arcane.cleansing.name.item", "Arcane Cleansing");
        add("spell.arcane.purifying.name.item", "Arcane Purification");
        add("spell.arcane.activator.name.item", "Arcane Activation");
        add("spell.arcane.preservation.name.item", "Arcane Preservation");
        add("spell.arcane.lighting.name.item", "Arcane Torch");

        // Machine tiers
        add("arcane.machine_tier.arcane.copper.name", "Copper");
        add("arcane.machine_tier.arcane.larimar.name", "Larimar");
        add("arcane.machine_tier.arcane.aurachalcum.name", "Aurachalcum");
    }
}
