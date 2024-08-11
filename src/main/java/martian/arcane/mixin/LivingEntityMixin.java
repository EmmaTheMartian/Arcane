package martian.arcane.mixin;

import martian.arcane.api.IHasFakeElytra;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(LivingEntity.class)
public class LivingEntityMixin implements IHasFakeElytra {
    @Unique
    public boolean arcane$hasFakeElytra = false;

    @SuppressWarnings({"UnusedAssignment", "ReassignedVariable"})
    @Inject(
            method = "updateFallFlying",
            at = @At(
                    value = "INVOKE_ASSIGN",
                    target = "Lnet/minecraft/world/entity/LivingEntity;getItemBySlot(Lnet/minecraft/world/entity/EquipmentSlot;)Lnet/minecraft/world/item/ItemStack;"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD,
            cancellable = true
    )
    private void arcane$updateFallFlying(CallbackInfo ci, boolean flag) {
        if (arcane$hasFakeElytra) {
            if (((Entity) (Object) this).onGround()) {
                arcane$hasFakeElytra = false;
            }
            flag = arcane$hasFakeElytra;
            ci.cancel();
        }
    }

    @Override
    public void arcane$setHasFakeElytra(boolean value) {
        arcane$hasFakeElytra = value;
    }

    @Override
    public boolean arcane$getHasFakeElytra() {
        return arcane$hasFakeElytra;
    }
}
