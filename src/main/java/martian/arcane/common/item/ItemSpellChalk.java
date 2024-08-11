package martian.arcane.common.item;

import martian.arcane.client.particle.MagicParticle;
import martian.arcane.common.ArcaneContent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.UseOnContext;
import org.jetbrains.annotations.NotNull;

public class ItemSpellChalk extends BlockItem {
    public ItemSpellChalk() {
        super(ArcaneContent.BE_SPELL_CIRCLE.block().get(), new Properties().stacksTo(1));
    }

    @Override
    @NotNull
    public InteractionResult useOn(@NotNull UseOnContext context) {
        InteractionResult res = super.useOn(context);
        if ((res == InteractionResult.CONSUME || res == InteractionResult.CONSUME_PARTIAL) && !context.getLevel().isClientSide)
            MagicParticle.spawn(context.getLevel(), context.getClickedPos().getCenter(), 0.4f, ArcaneContent.PIGMENT_MAGIC.get());
        return res;
    }
}
