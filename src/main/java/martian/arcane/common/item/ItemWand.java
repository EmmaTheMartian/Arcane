package martian.arcane.common.item;

import martian.arcane.api.ArcaneRegistries;
import martian.arcane.api.aura.AuraRecord;
import martian.arcane.api.item.AbstractAuraItem;
import martian.arcane.api.item.IAuraWand;
import martian.arcane.api.spell.AbstractSpell;
import martian.arcane.api.spell.CastContext;
import martian.arcane.api.wand.WandData;
import martian.arcane.client.particle.MagicParticle;
import martian.arcane.common.ArcaneContent;
import martian.arcane.common.networking.c2s.C2SUpdateWandTexture;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class ItemWand extends AbstractAuraItem implements IAuraWand {
    public ItemWand(Supplier<Integer> maxAura, Properties properties) {
        //noinspection DataFlowIssue
        super(() -> new AuraRecord(maxAura.get(), 0, false, true), properties
                .component(ArcaneContent.DC_SPELL, null)
                .component(ArcaneContent.DC_WAND_DATA, null));
    }

    @Override
    @NotNull
    public InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (player.isCrouching()) {
            ItemStack offhandItem = player.getOffhandItem();

            // Servers do not have textures, so we have to send a packet to update the texture
            if (offhandItem.getItem() instanceof BlockItem blockItem && level.isClientSide) {
                BlockModelShaper blockModelShaper = Minecraft.getInstance().getBlockRenderer().getBlockModelShaper();
                //noinspection deprecation
                try (var spriteContents = blockModelShaper.getParticleIcon(blockItem.getBlock().defaultBlockState()).contents()) {
                    PacketDistributor.sendToServer(new C2SUpdateWandTexture(player.getInventory().selected, spriteContents.name()));
                }
            } else if (offhandItem.is(ArcaneContent.ITEM_ARCANE_PIGMENT)) {
                mutateWandData(stack, data -> data.withColourPalette(offhandItem.get(ArcaneContent.DC_COLOUR_PALETTE)));
                offhandItem.shrink(1);
            }

            return InteractionResultHolder.success(stack);
        }

        if (player.getCooldowns().isOnCooldown(this) || !hasSpell(stack))
            return InteractionResultHolder.fail(stack);

        AbstractSpell spell = getSpell(stack);
        if (spell == null)
            return InteractionResultHolder.fail(stack);

        mutateAuraStorage(stack, aura -> {
            CastContext.WandContext ctx = new CastContext.WandContext(level, aura, player, hand, stack, this);

            if (!ctx.tryCast(spell).failed()) {
                MagicParticle.spawn(level, player.position(), player.getEyeHeight() - .2f, ArcaneRegistries.PIGMENTS.get(getWandData(stack).pigment()));
                player.getCooldowns().addCooldown(this, spell.getCooldownTicks(ctx));
            }

            return aura;
        });

        return InteractionResultHolder.success(stack);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> text, TooltipFlag flag) {
        super.appendHoverText(stack, context, text, flag);

        if (hasSpell(stack))
            text.add(Component
                    .translatable("messages.arcane.spell")
                    .append(getSpellOrThrow(stack).getSpellName())
                    .withStyle(ChatFormatting.AQUA));
        else
            text.add(Component.translatable("messages.arcane.no_spell"));

        text.add(Component
                .translatable("messages.arcane.wand_level")
                .append(Integer.toString(getWandData(stack).level())));
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean shouldCauseReequipAnimation(ItemStack old, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    @Override
    public int getCastLevel(CastContext context) {
        return getWandData(((CastContext.WandContext) context).castingStack).level();
    }

    // Static methods
    public static void mutateWandData(ItemStack stack, UnaryOperator<WandData> function) {
        stack.set(ArcaneContent.DC_WAND_DATA, function.apply(WandData.getOrCreate(stack, () -> WandData.DEFAULT)));
    }

    public static WandData getWandData(ItemStack stack) {
        return WandData.getOrCreate(stack, () -> WandData.DEFAULT);
    }

    public static void setSpell(ResourceLocation newSpell, ItemStack stack) {
        stack.set(ArcaneContent.DC_SPELL, newSpell);
        if (!stack.has(DataComponents.CUSTOM_NAME))
            stack.set(DataComponents.CUSTOM_NAME, getSpellOrThrow(stack).getItemName());
    }

    public static @Nullable AbstractSpell getSpell(ItemStack stack) {
        return ArcaneRegistries.SPELLS.get(getSpellId(stack));
    }

    public static AbstractSpell getSpellOrThrow(ItemStack stack) {
        return Objects.requireNonNull(ArcaneRegistries.SPELLS.get(getSpellId(stack)));
    }

    public static void removeSpell(ItemStack stack) {
        stack.remove(ArcaneContent.DC_SPELL);
    }

    public static boolean hasSpell(ItemStack stack) {
        return getSpellId(stack) != null;
    }

    public static @Nullable ResourceLocation getSpellId(ItemStack stack) {
        return stack.get(ArcaneContent.DC_SPELL);
    }

    public static ItemStack wandOfSpell(ResourceLocation spell) {
        ItemStack stack = new ItemStack(ArcaneContent.ITEM_WAND.get());
        stack.set(ArcaneContent.DC_SPELL, spell);
        return stack;
    }
}
