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
        add("itemGroup.arcane.arcane_spells_tab", "Arcane (Spells & Pigments)");

        // Items
        addItem(ITEM_AURAGLASS_BOTTLE, "Auraglass Bottle");
        addItem(ITEM_MEDIUM_AURAGLASS_BOTTLE, "Medium Auraglass Bottle");
        addItem(ITEM_LARGE_AURAGLASS_BOTTLE, "Large Auraglass Bottle");
        addItem(ITEM_EXTREME_AURAGLASS_BOTTLE, "Extreme Auraglass Bottle");
        add("item.arcane.creative_auraglass_bottle", "Creative Auraglass Bottle");

        addItem(ITEM_WAND, "Wand");
        addItem(ITEM_WANDBOOK, "Wandbook");
        addItem(ITEM_CHAINWAND, "Chainwand");

        addItem(ITEM_AURAOMETER, "Auraometer");
        addItem(ITEM_AURA_WRENCH, "Aurawrench");
        addItem(ITEM_AURA_CONFIGURATOR, "Configaurator");
        addItem(ITEM_AURA_MULTITOOL, "Aura Multitool");
        addItem(ITEM_SPELL_TABLET, "Spell Tablet");
        addItem(ITEM_SPELL_CHALK, "Spell Chalk");
        addItem(ITEM_ARCANE_BLEACH, "Arcane Bleach");
        addItem(ITEM_ENDERPACK, "Enderpack");
        addItem(ITEM_AXOBOTTLE, "Axobottle");
        addItem(ITEM_GEM_SAW, "Gem Saw");
        addItem(ITEM_UPGRADE_KIT_COPPER, "Copper Machine Parts");
        addItem(ITEM_UPGRADE_KIT_LARIMAR, "Larimar Machine Parts");
        addItem(ITEM_UPGRADE_KIT_AURACHALCUM, "Aurachalcum Machine Parts");
        addItem(ITEM_ARCANE_PIGMENT, "Arcane Pigment");

        addItem(ITEMS_LARIMAR.rough(), "Rough Larimar");
        addItem(ITEMS_LARIMAR.smooth(), "Smooth Larimar");
        addItem(ITEMS_LARIMAR.sandyPolished(), "Sandy Polished Larimar");
        addItem(ITEMS_LARIMAR.polished(), "Polished Larimar");
        addItem(ITEMS_LARIMAR.exquisite(), "Exquisite Larimar");
        addItem(ITEMS_FADED_LARIMAR.rough(), "Rough Faded Larimar");
        addItem(ITEMS_FADED_LARIMAR.smooth(), "Smooth Faded Larimar");
        addItem(ITEMS_FADED_LARIMAR.sandyPolished(), "Sandy Polished Faded Larimar");
        addItem(ITEMS_FADED_LARIMAR.polished(), "Polished Faded Larimar");
        addItem(ITEMS_FADED_LARIMAR.exquisite(), "Exquisite Faded Larimar");
        addItem(ITEMS_IDOCRASE.rough(), "Rough Idocrase");
        addItem(ITEMS_IDOCRASE.smooth(), "Smooth Idocrase");
        addItem(ITEMS_IDOCRASE.sandyPolished(), "Sandy Polished Idocrase");
        addItem(ITEMS_IDOCRASE.polished(), "Polished Idocrase");
        addItem(ITEMS_IDOCRASE.exquisite(), "Exquisite Idocrase");
        addItem(ITEM_RAW_AURACHALCUM, "Raw Aurachalcum");
        addItem(ITEM_AURACHALCUM, "Aurachalcum");
        addItem(ITEM_ELDRITCH_ALLOY, "Eldritch Alloy");
        addItem(ITEM_COPPER_CORE, "Copper Focus");
        addItem(ITEM_LARIMAR_CORE, "Larimar Focus");
        addItem(ITEM_AURACHALCUM_CORE, "Aurachalcum Focus");
        addItem(ITEM_ELDRITCH_CORE, "Eldritch Focus");
        addItem(ITEM_SPELL_CIRCLE_CORE, "Spell Circle Focus");
        addItem(ITEM_AURAGLASS_SHARD, "Auraglass Shard");
        addItem(ITEM_AURAGLASS_DUST, "Auraglass Dust");

        addItem(ITEM_CRUSHED_RAW_COPPER, "Crushed Raw Copper");
        addItem(ITEM_CRUSHED_RAW_IRON, "Crushed Raw Iron");
        addItem(ITEM_CRUSHED_RAW_GOLD, "Crushed Raw Gold");
        addItem(ITEM_PURIFIED_RAW_COPPER, "Purified Raw Copper");
        addItem(ITEM_PURIFIED_RAW_IRON, "Purified Raw Iron");
        addItem(ITEM_PURIFIED_RAW_GOLD, "Purified Raw Gold");

        add("item.arcane.auraometer.tooltip", "Shows information about blocks that possess Aura when held.");
        add("item.arcane.aura_wrench.tooltip.1", "Used to pair machinery.");
        add("item.arcane.aura_wrench.tooltip.2", "Use while sneaking to quickly break machinery.");
        add("item.arcane.aura_configurator.tooltip.1", "Used to configure machinery.");
        add("item.arcane.aura_configurator.tooltip.2", "Pronounced config-aura-tor.");
        add("item.arcane.aura_multitool.tooltip.1", "Functions universally as an Auraometer, Aura Wrench, and Configaurator.");
        add("item.arcane.aura_multitool.tooltip.2", "Use while crouching to change modes.");
        add("item.arcane.spell_tablet.tooltip", "Stores the spell inscribed upon it.");
        add("item.arcane.arcane_pigment.tooltip", "Stores the color palette inscribed upon it.");

        // Blocks
        addBlock(BE_AURA_CONNECTOR.block(), "Aura Connector");
        addBlock(BE_AURA_BASIN.block(), "Aura Basin");
        addBlock(BE_AURA_INFUSER.block(), "Aura Infuser");
        addBlock(BE_PEDESTAL.block(), "Pedestal");
        addBlock(BE_HEAT_COLLECTOR.block(), "Ignis Collector");
        addBlock(BE_AQUA_COLLECTOR.block(), "Aqua Collector");

        addBlock(BLOCK_AURAGLASS, "Auraglass");
        addBlock(BLOCK_SOUL_MAGMA, "Soul Magma");

        addBlock(BLOCK_CONJURED_CRAFTING_TABLE, "Conjured Crafting Table");
        addBlock(BLOCK_CONJURED_BLOCK, "Conjured Block");

        addBlock(BLOCK_LARIMAR_ORE, "Larimar Ore");
        addBlock(BLOCK_FADING_LARIMAR_ORE, "Fading Larimar Ore");
        addBlock(BLOCK_FADED_LARIMAR_ORE, "Faded Larimar Ore");
        addBlock(BLOCK_DEEPSLATE_LARIMAR_ORE, "Deepslate Larimar Ore");
        addBlock(BLOCK_FADING_DEEPSLATE_LARIMAR_ORE, "Fading Deepslate Larimar Ore");
        addBlock(BLOCK_FADED_DEEPSLATE_LARIMAR_ORE, "Faded Deepslate Larimar Ore");

        addBlock(BLOCK_IDOCRASE_ORE, "Idocrase Ore");
        addBlock(BLOCK_DEEPSLATE_IDOCRASE_ORE, "Deepslate Idocrase Ore");
        addBlock(BLOCK_NETHER_IDOCRASE_ORE, "Idocrase Ore");
        addBlock(BLOCK_BLACKSTONE_IDOCRASE_ORE, "Idocrase-Guilded Blackstone");

        addBlock(BLOCK_FADED_LARIMAR, "Faded Larimar Block");
        addBlock(BLOCK_FADING_LARIMAR, "Fading Larimar Block");
        addBlock(BLOCK_LARIMAR, "Larimar Block");
        addBlock(BLOCK_AURACHALCUM, "Aurachalcum Block");

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
        add("messages.arcane.holding_n_of_n_wands", "Holding %d/%d wands");
        add("messages.arcane.spells", "Spells: ");
        add("messages.arcane.spell_disabled", "Spell disabled!");
        add("messages.arcane.colour", "Color: ");

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
        add("spell.arcane.conjure_water.name", "Conjure Water");
        add("spell.arcane.freezing.name", "Freezing");
        add("spell.arcane.smelting.name", "Smelting");
        add("spell.arcane.mixing.name", "Mixing");
        add("spell.arcane.enlarging.name", "Enlarging");
        add("spell.arcane.shrinking.name", "Shrinking");
        add("spell.arcane.deploy_elytra.name", "Deploy Elytra");

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
        add("spell.arcane.conjure_water.name.item", "Arcane Bucket");
        add("spell.arcane.freezing.name.item", "Arcane Freezing");
        add("spell.arcane.smelting.name.item", "Arcane Smelting");
        add("spell.arcane.mixing.name.item", "Arcane Mixing");
        add("spell.arcane.enlarging.name.item", "Arcane Enlarging");
        add("spell.arcane.shrinking.name.item", "Arcane Shrinking");
        add("spell.arcane.deploy_elytra.name.item", "Arcane Elytra");

        // Machine tiers
        add("arcane.machine_tier.arcane.copper.name", "Copper");
        add("arcane.machine_tier.arcane.larimar.name", "Larimar");
        add("arcane.machine_tier.arcane.aurachalcum.name", "Aurachalcum");

        // Pigments
        add("pigment.arcane.magic", "Magic");
        add("pigment.arcane.pride", "Pride");
        add("pigment.arcane.trans", "Trans");
    }
}
