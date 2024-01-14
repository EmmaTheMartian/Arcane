package martian.arcane.block.entity;

import martian.arcane.api.NBTHelpers;
import martian.arcane.api.block.entity.AbstractAuraBlockEntity;
import martian.arcane.api.block.entity.IAuraometerOutput;
import martian.arcane.api.capability.IAuraStorage;
import martian.arcane.recipe.aurainfuser.AuraInfusionContainer;
import martian.arcane.recipe.aurainfuser.RecipeAuraInfusion;
import martian.arcane.registry.ArcaneBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class BlockEntityAuraInfuser extends AbstractAuraBlockEntity implements IAuraometerOutput {
    public ItemStackHandler inv;
    public int auraProgress = 0;

    public BlockEntityAuraInfuser(BlockPos pos, BlockState state) {
        super(32, false, true, ArcaneBlockEntities.AURA_INFUSER_BE.get(), pos, state);
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
    public List<Component> getText(List<Component> text) {
        if (!getItem().isEmpty())
            text.add(Component.literal("Holding: ").append(getItem().getDisplayName()));

        Optional<RecipeAuraInfusion> optionalRecipe = getRecipe();
        if (optionalRecipe.isPresent()) {
            RecipeAuraInfusion recipe = optionalRecipe.get();

            text.add(Component
                    .literal("Crafting: ")
                    .append(recipe.result.getDisplayName()));

            text.add(Component
                    .literal("Crafting Progress: ")
                    .append(Integer.toString(auraProgress))
                    .append("/")
                    .append(Integer.toString(recipe.aura)));
        }

        return super.getText(text);
    }

    public ItemStack getItem() {
        return inv.getStackInSlot(0);
    }

    public void setItem(ItemStack stack) {
        inv.setStackInSlot(0, stack);
    }

    public Optional<RecipeAuraInfusion> getRecipe() {
        if (getLevel() == null)
            return Optional.empty();
        return RecipeAuraInfusion.getRecipeFor(getLevel(), new AuraInfusionContainer(this.inv, this.auraProgress));
    }

    public Optional<RecipeAuraInfusion> getRecipeIgnoringAura() {
        if (getLevel() == null)
            return Optional.empty();
        return RecipeAuraInfusion.getRecipeFor(getLevel(), new AuraInfusionContainer(this.inv, this.auraProgress));
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos ignoredPos, BlockState ignoredState, T entity) {
        if (level.isClientSide())
            return;

        if (entity instanceof BlockEntityAuraInfuser infuser) {
            IAuraStorage storage = infuser.getAuraStorage().orElseThrow();

            Optional<RecipeAuraInfusion> optionalRecipe = infuser.getRecipeIgnoringAura();
            if (optionalRecipe.isEmpty()) return;
            RecipeAuraInfusion recipe = optionalRecipe.get();

            if (storage.getAura() > 0 && infuser.auraProgress < recipe.aura) {
                infuser.auraProgress++;
                storage.setAura(storage.getAura() - 1);
            }

            if (infuser.auraProgress >= recipe.aura) {
                recipe.assemble(infuser);
            }
        }
    }
}
