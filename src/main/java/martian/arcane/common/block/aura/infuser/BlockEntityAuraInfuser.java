package martian.arcane.common.block.aura.infuser;

import martian.arcane.ArcaneStaticConfig;
import martian.arcane.api.NBTHelpers;
import martian.arcane.api.block.entity.AbstractAuraBlockEntity;
import martian.arcane.api.block.entity.AbstractAuraBlockEntityWithSingleItem;
import martian.arcane.api.block.entity.IAuraometerOutput;
import martian.arcane.api.aura.AuraRecord;
import martian.arcane.common.recipe.RecipeAuraInfusion;
import martian.arcane.common.registry.ArcaneBlockEntities;
import martian.arcane.common.registry.ArcaneDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class BlockEntityAuraInfuser extends AbstractAuraBlockEntityWithSingleItem implements IAuraometerOutput {
//    public enum InfusionMode {
//        CRAFTING,
//        INSERT_AURA
//    }

//    public InfusionMode mode = InfusionMode.INSERT_AURA;
    public int auraProgress = 0;
    public boolean isActive = false;

    public BlockEntityAuraInfuser(int maxAura, int auraLoss, BlockPos pos, BlockState state) {
        super(maxAura, auraLoss, false, true, ArcaneBlockEntities.AURA_INFUSER.get(), pos, state);
    }

    public BlockEntityAuraInfuser(BlockPos pos, BlockState state) {
        super(ArcaneStaticConfig.AuraMaximums.AURA_INFUSER, ArcaneStaticConfig.AuraLoss.COPPER_TIER, false, true, ArcaneBlockEntities.AURA_INFUSER.get(), pos, state);
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag nbt, HolderLookup.@NotNull Provider provider) {
        nbt.putInt(NBTHelpers.KEY_AURA_PROGRESS, auraProgress);
//        nbt.putString(NBTHelpers.KEY_MODE, mode.toString());
        nbt.putBoolean(NBTHelpers.KEY_ACTIVE, isActive);
        super.saveAdditional(nbt, provider);
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag nbt, HolderLookup.@NotNull Provider provider) {
        super.loadAdditional(nbt, provider);
        auraProgress = nbt.getInt(NBTHelpers.KEY_AURA_PROGRESS);
//        mode = InfusionMode.valueOf(nbt.getString(NBTHelpers.KEY_MODE));
        isActive = nbt.getBoolean(NBTHelpers.KEY_ACTIVE);
    }

    @Override
    @NotNull
    public CompoundTag getUpdateTag(HolderLookup.@NotNull Provider provider) {
        CompoundTag nbt = super.getUpdateTag(provider);
        nbt.putInt(NBTHelpers.KEY_AURA_PROGRESS, auraProgress);
//        nbt.putString(NBTHelpers.KEY_MODE, mode.toString());
        nbt.putBoolean(NBTHelpers.KEY_ACTIVE, isActive);
        return nbt;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        assert level != null;
        CompoundTag nbt = getUpdateTag(level.registryAccess());
        auraProgress = nbt.getInt(NBTHelpers.KEY_AURA_PROGRESS);
//        mode = InfusionMode.valueOf(nbt.getString(NBTHelpers.KEY_MODE));
        isActive = nbt.getBoolean(NBTHelpers.KEY_ACTIVE);
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public List<Component> getText(List<Component> text, boolean detailed) {
        if (!getItem().isEmpty())
            text.add(Component.translatable("messages.arcane.holding").append(getItem().getDisplayName()));

//        text.add(Component.translatable("messages.arcane.mode")
//                .append(switch (mode) {
//                    case CRAFTING -> Component.translatable("messages.arcane.mode_crafting");
//                    case INSERT_AURA -> Component.translatable("messages.arcane.mode_filling");
//                }));

//        if (mode == InfusionMode.CRAFTING) {
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
//        } else if (mode == InfusionMode.INSERT_AURA && !getItem().isEmpty()) {
//            if (getItem().has(ArcaneDataComponents.AURA)) {
//                AuraRecord aura = getItem().get(ArcaneDataComponents.AURA);
//                assert aura != null;
//                text.add(Component
//                        .translatable("messages.arcane.item_aura")
//                        .append(Integer.toString(aura.getAura()))
//                        .append("/")
//                        .append(Integer.toString(aura.getMaxAura()))
//                        .withStyle(ChatFormatting.LIGHT_PURPLE));
//            }
//        }

        return super.getText(text, detailed);
    }

//    public void nextMode() {
//        mode = switch (mode) {
//            case CRAFTING -> InfusionMode.INSERT_AURA;
//            case INSERT_AURA -> InfusionMode.CRAFTING;
//        };
//    }

    public Optional<RecipeHolder<RecipeAuraInfusion>> getRecipe(boolean ignoreAura) {
        if (getLevel() == null)
            return Optional.empty();
        return RecipeAuraInfusion.getRecipeFor(getLevel(), new RecipeAuraInfusion.Container(this.stack, this.auraProgress), ignoreAura);
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T entity) {
        AbstractAuraBlockEntity.tick(level, pos, state, entity);

        if (level.isClientSide)
            return;

        if (entity instanceof BlockEntityAuraInfuser infuser) {
            // If there is a redstone signal coming into this block then we stop now
            if (level.hasNeighborSignal(pos))
                return;

//            if (infuser.mode == InfusionMode.CRAFTING) {
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
//            } else if (infuser.mode == InfusionMode.INSERT_AURA) {
//                ItemStack item = infuser.getItem();
//                if (item.has(ArcaneDataComponents.AURA)) {
//                    AuraRecord aura = item.get(ArcaneDataComponents.AURA);
//                    if (aura != null && aura.getAura() < aura.getMaxAura()) {
//                        var mutable = aura.unfreeze();
//                        infuser.sendAuraTo(mutable, 1);
//                        item.set(ArcaneDataComponents.AURA, mutable.freeze());
//                        level.sendBlockUpdated(pos, state, state, 2);
//                    }
//                }
//            }
        }
    }
}
