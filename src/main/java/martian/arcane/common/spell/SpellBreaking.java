package martian.arcane.common.spell;

import martian.arcane.ArcaneStaticConfig;
import martian.arcane.api.block.AOEHelpers;
import martian.arcane.api.spell.AbstractSpell;
import martian.arcane.api.spell.CastContext;
import martian.arcane.api.spell.CastResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

import java.util.concurrent.atomic.AtomicInteger;

public class SpellBreaking extends AbstractSpell {
    public SpellBreaking() {
        super(ArcaneStaticConfig.SpellMinLevels.BREAKING);
    }

    @Override
    public CastResult cast(CastContext c) {
        if (c.level.isClientSide)
            return CastResult.PASS;

        AtomicInteger cost = new AtomicInteger(0);
        BlockPos target = c.getTarget();

        if (target == null)
            return CastResult.FAILED;

        if (c instanceof CastContext.WandContext wc) {
            if (wc.raycast() instanceof BlockHitResult bHit) {
                if (c.source.getCastLevel() == 1 || wc.caster.isCrouching()) {
                    if (tryBreak(c.level, target, !wc.caster.isCreative()))
                        cost.getAndAdd(ArcaneStaticConfig.SpellCosts.BREAKING);
                } else {
                    AOEHelpers.streamAOE(target, bHit.getDirection(), getRadius(c.source.getCastLevel())).forEach(pos -> {
                        if (c.aura.getAura() - cost.get() <= 0)
                            return;

                        if (tryBreak(c.level, pos, !wc.caster.isCreative()))
                            cost.getAndAdd(ArcaneStaticConfig.SpellCosts.BREAKING);
                    });
                }
            } else {
                return CastResult.FAILED;
            }
        } else if (tryBreak(c.level, target, true)) {
            cost.getAndAdd(ArcaneStaticConfig.SpellCosts.BREAKING);
        }

        return new CastResult(cost.get(), false);
    }

    private static boolean tryBreak(Level level, BlockPos pos, boolean drop) {
        if (level.getBlockState(pos).getDestroySpeed(level, pos) >= 0) {
            level.destroyBlock(pos, drop);
            return true;
        }
        return false;
    }

    private static int getRadius(int level) {
        return switch (level) {
            case 2 -> 1;
            case 3 -> 2;
            default -> 0;
        };
    }
}
