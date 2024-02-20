package martian.arcane.common.item;

import com.lowdragmc.photon.client.fx.BlockEffect;
import martian.arcane.client.ArcaneFx;
import martian.arcane.common.registry.ArcaneBlocks;
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
            new BlockEffect(ArcaneFx.SPELL_CIRCLE_PLACE, context.getLevel(), context.getClickedPos());
        return res;
    }
}
