package martian.arcane.block.entity;

import com.mojang.datafixers.util.Function4;
import martian.arcane.ArcaneMod;
import martian.arcane.api.NBTHelpers;
import martian.arcane.api.block.entity.AbstractAuraBlockEntity;
import martian.arcane.api.block.entity.IAuraometerOutput;
import martian.arcane.api.capability.IAuraStorage;
import martian.arcane.recipe.spellcrafting.RecipeSpellcrafting;
import martian.arcane.recipe.spellcrafting.SpellcraftingContainer;
import martian.arcane.registry.ArcaneBlockEntities;
import martian.arcane.registry.ArcaneBlocks;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class BlockEntitySpellcraftingCore extends AbstractAuraBlockEntity implements IAuraometerOutput {
    public static final List<BlockPos> POSITION_OFFSETS = List.of(
            new BlockPos(-2, 0, -1), new BlockPos(-2, 0, 1),
            new BlockPos(0, 0, -2), new BlockPos(0, 0, 2),
            new BlockPos(2, 0, -1), new BlockPos(2, 0, 1)
    );

    public ItemStackHandler inv;

    public BlockEntitySpellcraftingCore(BlockPos pos, BlockState state) {
        super(256, false, true, ArcaneBlockEntities.SPELLCRAFTING_CORE_BE.get(), pos, state);
        inv = new ItemStackHandler(1);
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag nbt) {
        nbt.put(NBTHelpers.KEY_STACK, inv.serializeNBT());
        super.saveAdditional(nbt);
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        inv = new ItemStackHandler(1);
        inv.deserializeNBT(nbt.getCompound(NBTHelpers.KEY_STACK));
    }

    @Override
    @NotNull
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        nbt.put(NBTHelpers.KEY_STACK, inv.serializeNBT());
        return nbt;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        CompoundTag nbt = getUpdateTag();
        inv = new ItemStackHandler(1);
        inv.deserializeNBT(nbt.getCompound(NBTHelpers.KEY_STACK));
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public List<Component> getText(List<Component> text, boolean detailed) {
        if (!getItem().isEmpty())
            text.add(Component.literal("Holding: ").append(getItem().getDisplayName()));

        if (isMultiblockInvalid()) {
            Optional<RecipeSpellcrafting> optionalRecipe = getRecipe();
            if (optionalRecipe.isPresent()) {
                RecipeSpellcrafting recipe = optionalRecipe.get();

                text.add(Component
                        .literal("Crafting: ")
                        .append(recipe.result.getDisplayName()));
            }
        } else {
            text.add(Component
                    .literal("Invalid multiblock.")
                    .withStyle(ChatFormatting.RED));
        }

        if (detailed) {
            var inventory = getInventory();
            text.add(Component.literal("Pylon Items:"));
            for (int i = 0; i < inventory.getSlots(); i++)
                text.add(Component.literal("  - ").append(inventory.getStackInSlot(i).getDisplayName()));
        }

        return super.getText(text, detailed);
    }

    public ItemStack getItem() {
        return inv.getStackInSlot(0);
    }

    public void setItem(ItemStack stack) {
        inv.setStackInSlot(0, stack);
    }

    public Optional<RecipeSpellcrafting> getRecipe() {
        if (level == null)
            return Optional.empty();
        return RecipeSpellcrafting.getRecipeFor(level, new SpellcraftingContainer(this));
    }

    public boolean forEachPylon(Function4<Level, BlockState, BlockPos, BlockEntityItemPylon, ?> sup) {
        assert level != null;
        boolean valid = true;
        BlockPos pos;
        BlockState state;
        for (BlockPos offset : POSITION_OFFSETS) {
            pos = this.getBlockPos().offset(offset.getX(), offset.getY(), offset.getZ());
            state = level.getBlockState(pos);

            if (state.is(ArcaneBlocks.ITEM_PYLON.get()) && level.getBlockEntity(pos) instanceof BlockEntityItemPylon pylon)
                sup.apply(level, state, pos, pylon);
            else
                valid = false;
        }
        return valid;
    }

    public boolean isMultiblockInvalid() {
        return forEachPylon((a, b, c, d) -> null);
    }

    public void clearPylonItems() {
        forEachPylon((level, state, pos, entity) -> {
            entity.setItem(ItemStack.EMPTY);
            level.sendBlockUpdated(pos, state, state, 2);
            return 0;
        });
    }

    public IItemHandlerModifiable getInventory() {
        IItemHandlerModifiable inventory = new ItemStackHandler(7);
        inventory.setStackInSlot(0, getItem());
        AtomicInteger index = new AtomicInteger(1);
        forEachPylon((level, state, pos, pylon) -> {
            inventory.setStackInSlot(index.get(), pylon.getItem());
            return index.getAndIncrement();
        });
        return inventory;
    }

    public static SpellcraftingCraftStatus craft(Level level, BlockPos pos, BlockState state, BlockEntitySpellcraftingCore core) {
        IAuraStorage storage = core.getAuraStorage().orElseThrow();

        if (storage.getAura() < RecipeSpellcrafting.spellcraftingAuraCost)
            return SpellcraftingCraftStatus.INSUFFICIENT_AURA;

        if (core.isMultiblockInvalid())
            return SpellcraftingCraftStatus.INVALID_MULTIBLOCK;

        Optional<RecipeSpellcrafting> optionalRecipe = core.getRecipe();
        if (optionalRecipe.isEmpty())
            return SpellcraftingCraftStatus.NOT_A_RECIPE;

        optionalRecipe.get().assemble(core);
        level.sendBlockUpdated(pos, state, state, 2);
        return SpellcraftingCraftStatus.SUCCESS;
    }

    public enum SpellcraftingCraftStatus {
        SUCCESS,
        INVALID_MULTIBLOCK,
        INSUFFICIENT_AURA,
        NOT_A_RECIPE,
    }
}
