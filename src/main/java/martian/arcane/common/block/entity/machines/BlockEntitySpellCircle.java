package martian.arcane.common.block.entity.machines;

import com.lowdragmc.photon.client.fx.BlockEffect;
import martian.arcane.ArcaneStaticConfig;
import martian.arcane.api.BlockHelpers;
import martian.arcane.api.NBTHelpers;
import martian.arcane.api.block.entity.AbstractAuraBlockEntity;
import martian.arcane.api.spell.AbstractSpell;
import martian.arcane.api.spell.CastContext;
import martian.arcane.client.ArcaneFx;
import martian.arcane.common.registry.ArcaneBlockEntities;
import martian.arcane.common.registry.ArcaneSpells;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BlockEntitySpellCircle extends AbstractAuraBlockEntity {
    private int castRateTicks;
    private int ticksToNextCast;
    private int castingLevel;
    private boolean isActive = false;
    private @Nullable ResourceLocation spellId = null;

    public BlockEntitySpellCircle(int maxAura, int castRateTicks, int castingLevel, BlockPos pos, BlockState state) {
        super(maxAura, false, true, ArcaneBlockEntities.SPELL_CIRCLE.get(), pos, state);
        this.castRateTicks = castRateTicks;
        this.ticksToNextCast = this.castRateTicks;
        this.castingLevel = castingLevel;
    }

    public BlockEntitySpellCircle(BlockPos pos, BlockState state) {
        super(ArcaneStaticConfig.Maximums.SPELL_CIRCLE_BASIC, false, true, ArcaneBlockEntities.SPELL_CIRCLE.get(), pos, state);
        this.castRateTicks = ArcaneStaticConfig.Speed.SPELL_CIRCLE_BASIC;
        this.ticksToNextCast = this.castRateTicks;
        this.castingLevel = 1;
    }

    @Override
    public List<Component> getText(List<Component> text, boolean detailed) {
        if (isActive && hasSpell()) {
            text.add(Component
                    .translatable("messages.arcane.spell")
                    .append(ArcaneSpells.getSpellById(spellId).getSpellName())
                    .withStyle(ChatFormatting.LIGHT_PURPLE));

            text.add(Component
                    .translatable("messages.arcane.cast_timer")
                    .append(Integer.toString(ticksToNextCast))
                    .append("/")
                    .append(Integer.toString(castRateTicks))
                    .withStyle(ChatFormatting.AQUA));
        } else {
            if (!isActive)
                text.add(Component.literal("Not active!").withStyle(ChatFormatting.RED));
            if (!hasSpell())
                text.add(Component.literal("No spell!").withStyle(ChatFormatting.RED));
        }

        return super.getText(text, detailed);
    }

    protected void tick() {
        if (level != null && hasSpell() && isActive && --ticksToNextCast <= 0) {
            mapAuraStorage(storage -> {
                AbstractSpell spell = ArcaneSpells.getSpellById(spellId);
                int cost = spell.getAuraCost(castingLevel);
                if (storage.getAura() >= cost) {
                    CastContext.SpellCircleContext context = new CastContext.SpellCircleContext(level, getBlockPos().below());
                    spell.cast(context);
                    ticksToNextCast = castRateTicks;
                    storage.removeAura(cost);
                    new BlockEffect(ArcaneFx.ON_CAST_GRAVITY, level, getBlockPos()).start();
                }
                return null;
            });
        }
    }

    public boolean hasSpell() {
        return spellId != null;
    }

    // Setters
    public void setSpell(ResourceLocation id) {
        spellId = id;
        BlockHelpers.sync(this);
    }

    public void setActive(boolean value) {
        isActive = value;
        BlockHelpers.sync(this);
    }

    public void setCastRate(int value) {
        castRateTicks = value;
        BlockHelpers.sync(this);
    }

    public void setCastingLevel(int value) {
        castingLevel = value;
        BlockHelpers.sync(this);
    }

    // Getters
    public @Nullable ResourceLocation getSpellId() { return spellId; }
    public boolean getActive() { return isActive; }
    public int getCastRate() { return castRateTicks; }
    public int getTicksToNextCast() { return ticksToNextCast; }
    public int getCastingLevel() { return castingLevel; }

    @Override
    public void saveAdditional(@NotNull CompoundTag nbt) {
        nbt.putString(NBTHelpers.KEY_SPELL, spellId == null ? "null" : spellId.toString());
        nbt.putInt(NBTHelpers.KEY_LEVEL, castingLevel);
        nbt.putInt(NBTHelpers.KEY_TICKS_TO_NEXT, ticksToNextCast);
        nbt.putInt(NBTHelpers.KEY_CAST_RATE, castRateTicks);
        nbt.putBoolean(NBTHelpers.KEY_ACTIVE, isActive);
        super.saveAdditional(nbt);
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        String id = nbt.getString(NBTHelpers.KEY_SPELL);
        spellId = id.equals("null") ? null : ResourceLocation.of(id, ':');
        castingLevel = nbt.getInt(NBTHelpers.KEY_LEVEL);
        ticksToNextCast = nbt.getInt(NBTHelpers.KEY_TICKS_TO_NEXT);
        castRateTicks = nbt.getInt(NBTHelpers.KEY_CAST_RATE);
        isActive = nbt.getBoolean(NBTHelpers.KEY_ACTIVE);
    }

    @Override
    @NotNull
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        nbt.putString(NBTHelpers.KEY_SPELL, spellId == null ? "null" : spellId.toString());
        nbt.putInt(NBTHelpers.KEY_LEVEL, castingLevel);
        nbt.putInt(NBTHelpers.KEY_TICKS_TO_NEXT, ticksToNextCast);
        nbt.putInt(NBTHelpers.KEY_CAST_RATE, castRateTicks);
        nbt.putBoolean(NBTHelpers.KEY_ACTIVE, isActive);
        return nbt;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        CompoundTag nbt = getUpdateTag();
        String id = nbt.getString(NBTHelpers.KEY_SPELL);
        spellId = id.equals("null") ? null : ResourceLocation.of(id, ':');
        castingLevel = nbt.getInt(NBTHelpers.KEY_LEVEL);
        ticksToNextCast = nbt.getInt(NBTHelpers.KEY_TICKS_TO_NEXT);
        castRateTicks = nbt.getInt(NBTHelpers.KEY_CAST_RATE);
        isActive = nbt.getBoolean(NBTHelpers.KEY_ACTIVE);
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T be) {
        if (level.isClientSide)
            return;

        if (be instanceof BlockEntitySpellCircle spellCircle)
            spellCircle.tick();
    }
}
