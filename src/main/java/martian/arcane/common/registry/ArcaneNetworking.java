package martian.arcane.common.registry;

import martian.arcane.ArcaneMod;
import martian.arcane.api.ArcaneRegistry;
import martian.arcane.common.networking.c2s.C2SPacketOpenEnderpack;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ArcaneNetworking extends ArcaneRegistry {
    private static final String PROTOCOL_VERSION = "1";
    private static int id = 0;

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(ArcaneMod.id("main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals,PROTOCOL_VERSION::equals);

    public static void init() {
        CHANNEL.messageBuilder(C2SPacketOpenEnderpack.class, id++)
                .encoder(C2SPacketOpenEnderpack::encoder)
                .decoder(C2SPacketOpenEnderpack::decoder)
                .consumerMainThread(C2SPacketOpenEnderpack::messageConsumer)
                .add();
    }
}
