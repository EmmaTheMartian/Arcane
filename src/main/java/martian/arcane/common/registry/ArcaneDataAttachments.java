package martian.arcane.common.registry;

import martian.arcane.ArcaneMod;
import martian.arcane.api.ArcaneRegistry;
import martian.arcane.api.aura.AuraStorage;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ArcaneDataAttachments extends ArcaneRegistry {
    public ArcaneDataAttachments() { super(REGISTER); }

    private static final DeferredRegister<AttachmentType<?>> REGISTER = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, ArcaneMod.MODID);

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<AuraStorage>> AURA = REGISTER.register("aura_storage", () ->
            AttachmentType.builder(() -> new AuraStorage(-1, false, false)).serialize(AuraStorage.CODEC).build());
}
