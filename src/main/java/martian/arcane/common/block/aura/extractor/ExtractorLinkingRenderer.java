package martian.arcane.common.block.aura.extractor;

import martian.arcane.api.Raycasting;
import martian.arcane.client.ArcaneClient;
import martian.arcane.client.ParticleHelper;
import martian.arcane.client.gui.AuraometerOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ExtractorLinkingRenderer {
    private static final Random RANDOM = new Random();
    public static List<BlockPos> selectedExtractors = new ArrayList<>();

    public static void tick() {
        if (ArcaneClient.clientTicks % 8 != 0)
            return;

        Player player = Minecraft.getInstance().player;
        Level level = Minecraft.getInstance().level;

        if (player == null || level == null || Minecraft.getInstance().screen != null)
            return;

        if (!AuraometerOverlay.isHoldingAuraometer(player))
            return;

        selectedExtractors.forEach(selectedPos -> {
            if (level.getBlockEntity(selectedPos) instanceof BlockEntityAuraExtractor extractor && extractor.targetPos != null) {
                addParticles(extractor.getBlockPos(), extractor.targetPos, level, 0.5f);
            }
        });

        HitResult hit = Minecraft.getInstance().hitResult;
        if (hit == null || hit.getType() != HitResult.Type.BLOCK)
            return;

        BlockHitResult bHit = (BlockHitResult) hit;
        if (level.getBlockEntity(bHit.getBlockPos()) instanceof BlockEntityAuraExtractor extractor && extractor.targetPos != null) {
            addParticles(extractor.getBlockPos(), extractor.targetPos, level, 0.75f);
        }
    }

    private static void addParticles(BlockPos from, BlockPos to, Level level, float chance) {
        Raycasting.traverseBetweenPoints(from.getCenter(), to.getCenter(), 0.3D, 0.1D, pos -> {
            // This helps cut down on how many particles get spawned to prevent lag
            if (RANDOM.nextFloat() <= chance)
                ParticleHelper.addMagicParticle(level, pos);
            return true;
        });
    }
}
