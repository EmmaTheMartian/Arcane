package martian.arcane.block.entity;

import martian.arcane.api.NBTHelpers;
import martian.arcane.api.block.entity.AbstractAuraBlockEntity;
import martian.arcane.api.block.entity.IAuraometerOutput;
import martian.arcane.api.capability.IAuraStorage;
import martian.arcane.recipe.aurainfuser.AuraInfusionContainer;
import martian.arcane.recipe.aurainfuser.RecipeAuraInfusion;
import martian.arcane.registry.ArcaneBlockEntities;
import net.minecraft.ChatFormatting;
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
        nbt.putInt(NBTHelpers.KEY_AURA, auraProgress);
        super.saveAdditional(nbt);
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        inv = new ItemStackHandler(1);
        inv.deserializeNBT(nbt.getCompound(NBTHelpers.KEY_STACK));
        auraProgress = nbt.getInt(NBTHelpers.KEY_AURA);
    }

    @Override
    @NotNull
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        nbt.put(NBTHelpers.KEY_STACK, inv.serializeNBT());
        nbt.putInt(NBTHelpers.KEY_AURA, auraProgress);
        return nbt;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        CompoundTag nbt = getUpdateTag();
        inv = new ItemStackHandler(1);
        inv.deserializeNBT(nbt.getCompound(NBTHelpers.KEY_STACK));
        auraProgress = nbt.getInt(NBTHelpers.KEY_AURA);
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public List<Component> getText(List<Component> text, boolean detailed) {
        if (!getItem().isEmpty())
            text.add(Component.literal("Holding: ").append(getItem().getDisplayName()));

        Optional<RecipeAuraInfusion> optionalRecipe = getRecipe(true);
        if (optionalRecipe.isPresent()) {
            RecipeAuraInfusion recipe = optionalRecipe.get();

            text.add(Component
                    .literal("Crafting: ")
                    .append(recipe.result.getDisplayName()));

            text.add(Component
                    .literal("Infusing Progress: ")
                    .append(Integer.toString(auraProgress))
                    .append("/")
                    .append(Integer.toString(recipe.aura))
                    .withStyle(ChatFormatting.LIGHT_PURPLE));
        }

        return super.getText(text, detailed);
    }

    public ItemStack getItem() {
        return inv.getStackInSlot(0);
    }

    public void setItem(ItemStack stack) {
        inv.setStackInSlot(0, stack);
    }

    public Optional<RecipeAuraInfusion> getRecipe(boolean ignoreAura) {
        if (getLevel() == null)
            return Optional.empty();
        return RecipeAuraInfusion.getRecipeFor(getLevel(), new AuraInfusionContainer(this.inv, this.auraProgress), ignoreAura);
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T entity) {
        if (level.isClientSide())
            return;

        if (entity instanceof BlockEntityAuraInfuser infuser) {
            IAuraStorage storage = infuser.getAuraStorage().orElseThrow();

            //FIXME: Cache current recipe
            Optional<RecipeAuraInfusion> optionalRecipe = infuser.getRecipe(true);
            if (optionalRecipe.isEmpty())
                return;
            RecipeAuraInfusion recipe = optionalRecipe.get();

            if (storage.getAura() > 0 && infuser.auraProgress < recipe.aura) {
                int auraToAdd = recipe.aura;
                if (auraToAdd > storage.getAura())
                    auraToAdd = storage.getAura();

                infuser.auraProgress += auraToAdd;
                storage.setAura(storage.getAura() - auraToAdd);
            }

            if (infuser.auraProgress >= recipe.aura) {
                recipe.assemble(infuser);
                level.sendBlockUpdated(pos, state, state, 2);
            }
        }
    }
}
