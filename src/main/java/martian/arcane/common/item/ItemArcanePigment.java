package martian.arcane.common.item;

import martian.arcane.ArcaneMod;
import martian.arcane.common.ArcaneContent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class ItemArcanePigment extends Item {
    public ItemArcanePigment() {
        super(new Properties().stacksTo(1).component(ArcaneContent.DC_COLOUR_PALETTE, ArcaneMod.id("magic")));
    }

    @Override
    @ParametersAreNonnullByDefault
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> text, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, context, text, flag);
        text.add(Component.translatable("item.arcane.arcane_pigment.tooltip"));
        ResourceLocation palette = stack.get(ArcaneContent.DC_COLOUR_PALETTE);
        if (palette != null)
            text.add(Component
                    .translatable("messages.arcane.colour")
                    .append(Component.translatable("pigment." + palette.getNamespace() + "." + palette.getPath()))
                    .withStyle(ChatFormatting.AQUA));
    }
}
