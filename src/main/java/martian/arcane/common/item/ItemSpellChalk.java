package martian.arcane.common.item;

import martian.arcane.common.registry.ArcaneBlocks;
import martian.arcane.integration.photon.ArcaneFx;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.UseOnContext;
import org.jetbrains.annotations.NotNull;

public class ItemSpellChalk extends BlockItem {
    public ItemSpellChalk() {
        super(ArcaneBlocks.SPELL_CIRCLE.get(), new Properties().stacksTo(1));
    }

    @Override
    @NotNull
    public InteractionResult useOn(@NotNull UseOnContext context) {
        InteractionResult res = super.useOn(context);
        if ((res == InteractionResult.CONSUME || res == InteractionResult.CONSUME_PARTIAL) && !context.getLevel().isClientSide)
            ArcaneFx.SPELL_CIRCLE_PLACE.goBlock(context.getLevel(), context.getClickedPos());
        return res;
    }
}
