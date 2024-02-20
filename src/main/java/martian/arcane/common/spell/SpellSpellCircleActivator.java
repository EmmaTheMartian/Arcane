package martian.arcane.common.spell;

import com.lowdragmc.photon.client.fx.BlockEffect;
import martian.arcane.api.BlockHelpers;
import martian.arcane.api.spell.AbstractSpell;
import martian.arcane.api.spell.CastContext;
import martian.arcane.api.spell.ICastingSource;
import martian.arcane.client.ArcaneFx;
import martian.arcane.common.block.entity.machines.BlockEntitySpellCircle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class SpellSpellCircleActivator extends AbstractSpell {
    public SpellSpellCircleActivator() {
        super(2);
    }

    @Override
    public int getAuraCost(int level) {
        return 16;
    }

    @Override
    public void cast(CastContext c) {
        if (c.level.isClientSide)
            return;

        if (c.source == ICastingSource.Type.WAND) {
            CastContext.WandContext wandContext = (CastContext.WandContext) c;
            HitResult hit = wandContext.raycast();

            if (hit.getType() != HitResult.Type.BLOCK)
                return;

            BlockPos pos = ((BlockHitResult) hit).getBlockPos();
            if (c.level.getBlockEntity(pos) instanceof BlockEntitySpellCircle spellCircle && !spellCircle.getActive()) {
                new BlockEffect(ArcaneFx.ON_CAST_GRAVITY, c.level, pos).start();
                new BlockEffect(ArcaneFx.SPELL_CIRCLE_INIT, c.level, pos).start();
                spellCircle.setActive(true);
                BlockHelpers.sync(spellCircle);
            }
        }
    }
}
