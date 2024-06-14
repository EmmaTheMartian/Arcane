package martian.arcane.common.spell;

import martian.arcane.ArcaneMod;
import martian.arcane.api.block.AOEHelpers;
import martian.arcane.api.spell.AbstractSpell;
import martian.arcane.api.spell.CastContext;
import martian.arcane.api.spell.CastResult;
import martian.arcane.api.spell.SpellConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class SpellBuilding extends AbstractSpell {
    private static final SpellConfig config = new SpellConfig(ArcaneMod.id("building"))
            .set("cooldown", 20)
            .set("minLevel", 1)
            .set("auraCostPerBlock", 2)
            .set("radiusAtLevel1", 0)
            .set("radiusAtLevel2", 1)
            .set("radiusAtLevel3", 2)
            .set("defaultBlock", "arcane:conjured_block")
            .build();

    @Override
    protected SpellConfig getConfig() {
        return config;
    }

    @Override
    public int getAuraCost(CastContext c) {
        AtomicInteger cost = new AtomicInteger(0);
        BlockPos target = c.getTarget();
        if (target == null)
            return 0;

        if (c instanceof CastContext.WandContext wc) {
            if (wc.raycast() instanceof BlockHitResult bHit) {
                target = bHit.getBlockPos().relative(bHit.getDirection());
                if (c.source.getCastLevel(wc) == 1 || wc.caster.isCrouching()) {
                    if (canPlace(c.level, target))
                        cost.getAndAdd(config.get("auraCostPerBlock"));
                } else {
                    AOEHelpers.streamAOE(target, bHit.getDirection(), getRadius(c.source.getCastLevel(wc))).forEach(pos -> {
                        if (c.aura.getAura() - cost.get() <= 0)
                            return;

                        if (canPlace(c.level, pos))
                            cost.getAndAdd(config.get("auraCostPerBlock"));
                    });
                }
            } else {
                return 0;
            }
        } else if (canPlace(c.level, target)) {
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

        BlockState toPlace = BuiltInRegistries.BLOCK.get(ResourceLocation.of(config.get("defaultBlock"), ':')).defaultBlockState();

        if (c instanceof CastContext.WandContext wc) {
            ItemStack offStack = wc.caster.getMainHandItem() == wc.castingStack
                    ? wc.caster.getOffhandItem()
                    : wc.caster.getMainHandItem();
            AtomicBoolean usingOffStack = new AtomicBoolean(false);

            if (!offStack.isEmpty() && offStack.getItem() instanceof BlockItem bi) {
                toPlace = bi.getBlock().defaultBlockState();
                usingOffStack.set(true);
            }

            if (wc.raycast() instanceof BlockHitResult bHit) {
                target = bHit.getBlockPos().relative(bHit.getDirection());
                if (c.source.getCastLevel(wc) == 1 || wc.caster.isCrouching()) {
                    if (tryPlace(c.level, target, toPlace))
                        cost.getAndAdd(config.get("auraCostPerBlock"));
                } else {
                    BlockState finalToPlace = toPlace;
                    AOEHelpers.streamAOE(target, bHit.getDirection(), getRadius(c.source.getCastLevel(wc))).forEach(pos -> {
                        if (c.aura.getAura() - cost.get() <= 0 || (usingOffStack.get() && offStack.isEmpty()))
                            return;

                        if (tryPlace(c.level, pos, finalToPlace)) {
                            cost.getAndAdd(config.get("auraCostPerBlock"));
                            if (usingOffStack.get() && !wc.caster.isCreative())
                                offStack.shrink(1);
                        }
                    });
                }
            } else {
                return CastResult.FAILED;
            }
        } else {
            tryPlace(c.level, target, toPlace);
        }

        return CastResult.SUCCESS;
    }

    private static boolean canPlace(Level level, BlockPos pos) {
        return level.getBlockState(pos).canBeReplaced();
    }

    private static boolean tryPlace(Level level, BlockPos pos, BlockState toPlace) {
        if (canPlace(level, pos)) {
            level.setBlockAndUpdate(pos, toPlace);
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
