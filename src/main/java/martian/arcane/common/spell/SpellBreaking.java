package martian.arcane.common.spell;

import martian.arcane.ArcaneMod;
import martian.arcane.api.block.AOEHelpers;
import martian.arcane.api.spell.AbstractSpell;
import martian.arcane.api.spell.CastContext;
import martian.arcane.api.spell.CastResult;
import martian.arcane.api.spell.SpellConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

import java.util.concurrent.atomic.AtomicInteger;

public class SpellBreaking extends AbstractSpell {
    private static final SpellConfig config = new SpellConfig(ArcaneMod.id("breaking"))
            .set("cooldown", 20)
            .set("minLevel", 1)
            .set("auraCostPerBlock", 2)
            .set("radiusAtLevel1", 0)
            .set("radiusAtLevel2", 1)
            .set("radiusAtLevel3", 2)
            .build();

    @Override
    protected SpellConfig getConfig() {
        return config;
    }

    @Override
    public int getAuraCost(CastContext c) {
        AtomicInteger cost = new AtomicInteger(0);
        BlockPos target = c.getTarget();

        if (c instanceof CastContext.WandContext wc) {
            if (wc.raycast() instanceof BlockHitResult bHit) {
                if (c.source.getCastLevel(wc) == 1 || wc.caster.isCrouching()) {
                    if (canBreak(c.level, target))
                        cost.getAndAdd(config.get("auraCostPerBlock"));
                } else {
                    AOEHelpers.streamAOE(target, bHit.getDirection(), getRadius(c.source.getCastLevel(wc))).forEach(pos -> {
                        if (c.aura.getAura() - cost.get() <= 0)
                            return;

                        if (canBreak(c.level, pos))
                            cost.getAndAdd(config.get("auraCostPerBlock"));
                    });
                }
            }
        } else if (canBreak(c.level, target)) {
            cost.getAndAdd(config.get("auraCostPerBlock"));
        }

        return cost.get();
    }

    @Override
    public CastResult cast(CastContext c) {
        if (c.level.isClientSide)
            return CastResult.SUCCESS;

        AtomicInteger cost = new AtomicInteger(0);
        BlockPos target = c.getTarget();

        if (target == null)
            return CastResult.FAILED;

        if (c instanceof CastContext.WandContext wc) {
            if (wc.raycast() instanceof BlockHitResult bHit) {
                if (c.source.getCastLevel(wc) == 1 || wc.caster.isCrouching()) {
                    if (tryBreak(c.level, target, !wc.caster.isCreative()))
                        cost.getAndAdd(config.get("auraCostPerBlock"));
                } else {
                    AOEHelpers.streamAOE(target, bHit.getDirection(), getRadius(c.source.getCastLevel(wc))).forEach(pos -> {
                        if (c.aura.getAura() - cost.get() <= 0)
                            return;

                        if (tryBreak(c.level, pos, !wc.caster.isCreative()))
                            cost.getAndAdd(config.get("auraCostPerBlock"));
                    });
                }
            } else {
                return CastResult.FAILED;
            }
        } else {
            tryBreak(c.level, target, true);
        }

        return CastResult.SUCCESS;
    }

    private static boolean canBreak(Level level, BlockPos pos) {
        return level.getBlockState(pos).getDestroySpeed(level, pos) >= 0;
    }

    private static boolean tryBreak(Level level, BlockPos pos, boolean drop) {
        if (canBreak(level, pos)) {
            level.destroyBlock(pos, drop);
            return true;
        }
        return false;
    }

    private static int getRadius(int level) {
        return switch (level) {
            case 1 -> config.get("radiusAtLevel1");
            case 2 -> config.get("radiusAtLevel2");
            default -> config.get("radiusAtLevel3");
        };
    }
}
