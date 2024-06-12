package martian.arcane.common.block.infuser;

import martian.arcane.ArcaneStaticConfig;
import martian.arcane.api.NBTHelpers;
import martian.arcane.api.block.entity.AbstractAuraBlockEntity;
import martian.arcane.api.block.entity.AbstractAuraBlockEntityWithSingleItem;
import martian.arcane.api.block.entity.IAuraometerOutput;
import martian.arcane.common.recipe.RecipeAuraInfusion;
import martian.arcane.common.registry.ArcaneBlockEntities;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class BlockEntityAuraInfuser extends AbstractAuraBlockEntityWithSingleItem implements IAuraometerOutput {
    public int auraProgress = 0;
    public boolean isActive = false;

    public BlockEntityAuraInfuser(BlockPos pos, BlockState state) {
        super(ArcaneStaticConfig.AuraMaximums.AURA_INFUSER, false, true, ArcaneBlockEntities.AURA_INFUSER.get(), pos, state);
    }

    @Override
    public boolean isUpgradable() {
        return true;
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag nbt, HolderLookup.@NotNull Provider provider) {
        nbt.putInt(NBTHelpers.KEY_AURA_PROGRESS, auraProgress);
        nbt.putBoolean(NBTHelpers.KEY_ACTIVE, isActive);
        super.saveAdditional(nbt, provider);
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag nbt, HolderLookup.@NotNull Provider provider) {
        super.loadAdditional(nbt, provider);
        auraProgress = nbt.getInt(NBTHelpers.KEY_AURA_PROGRESS);
        isActive = nbt.getBoolean(NBTHelpers.KEY_ACTIVE);
    }

    @Override
    @NotNull
    public CompoundTag getUpdateTag(HolderLookup.@NotNull Provider provider) {
        CompoundTag nbt = super.getUpdateTag(provider);
        nbt.putInt(NBTHelpers.KEY_AURA_PROGRESS, auraProgress);
        nbt.putBoolean(NBTHelpers.KEY_ACTIVE, isActive);
        return nbt;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        assert level != null;
        CompoundTag nbt = getUpdateTag(level.registryAccess());
        auraProgress = nbt.getInt(NBTHelpers.KEY_AURA_PROGRESS);
        isActive = nbt.getBoolean(NBTHelpers.KEY_ACTIVE);
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public List<Component> getText(List<Component> text, boolean detailed) {
        if (!getItem().isEmpty())
            text.add(Component.translatable("messages.arcane.holding").append(getItem().getDisplayName()));

        Optional<RecipeHolder<RecipeAuraInfusion>> optionalRecipe = getRecipe(true);
        if (optionalRecipe.isPresent()) {
            RecipeAuraInfusion recipe = optionalRecipe.get().value();

            text.add(Component
                    .translatable("messages.arcane.infusing_progress")
                    .append(Integer.toString(auraProgress))
                    .append("/")
                    .append(Integer.toString(recipe.aura))
                    .withStyle(ChatFormatting.LIGHT_PURPLE));
        }

        return super.getText(text, detailed);
    }

    public Optional<RecipeHolder<RecipeAuraInfusion>> getRecipe(boolean ignoreAura) {
        if (getLevel() == null)
            return Optional.empty();
        return RecipeAuraInfusion.getRecipeFor(getLevel(), new RecipeAuraInfusion.Container(this.getItem(), this.auraProgress), ignoreAura);
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T entity) {
        AbstractAuraBlockEntity.tick(level, pos, state, entity);

        if (level.isClientSide)
            return;

        if (entity instanceof BlockEntityAuraInfuser infuser) {
            if (level.hasNeighborSignal(pos))
                return;

            //FIXME: Cache current recipe?
            Optional<RecipeHolder<RecipeAuraInfusion>> optionalRecipe = infuser.getRecipe(true);
            if (optionalRecipe.isEmpty())
                return;

            if (!infuser.isActive)
                infuser.isActive = true;

            RecipeAuraInfusion recipe = optionalRecipe.get().value();

            if (infuser.getAura() > 0 && infuser.auraProgress < recipe.aura) {
                int auraToAdd = recipe.aura;
                if (auraToAdd > infuser.getAura())
                    auraToAdd = infuser.getAura();

                infuser.auraProgress += auraToAdd;
                infuser.removeAura(auraToAdd);
                level.sendBlockUpdated(pos, state, state, 2);
            }

            if (infuser.auraProgress >= recipe.aura) {
                recipe.assemble(infuser);
                infuser.isActive = false;
                level.sendBlockUpdated(pos, state, state, 2);
            }
        }
    }
}
