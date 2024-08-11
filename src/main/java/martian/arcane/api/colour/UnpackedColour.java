package martian.arcane.api.colour;

import martian.arcane.client.ArcaneClient;
import net.minecraft.util.FastColor;

public record UnpackedColour(int r, int g, int b, int a) {
    public UnpackedColour(int r, int g, int b) {
        this(r, g, b, 255);
    }

    public UnpackedColour(int packedColour) {
        this(
                FastColor.ARGB32.red(packedColour),
                FastColor.ARGB32.green(packedColour),
                FastColor.ARGB32.blue(packedColour),
                FastColor.ARGB32.alpha(packedColour)
        );
    }

    public int pack() {
        return FastColor.ARGB32.color(a, r, g, b);
    }

    public UnpackedColour modulateRGB(int vary) {
        return new UnpackedColour(
                r + Math.clamp(ArcaneClient.RANDOM.nextInt(-vary, vary), 0, 255),
                g + Math.clamp(ArcaneClient.RANDOM.nextInt(-vary, vary), 0, 255),
                b + Math.clamp(ArcaneClient.RANDOM.nextInt(-vary, vary), 0, 255),
                a
        );
    }

    public UnpackedColour modulateAlpha(int vary) {
        return new UnpackedColour(
                r,
                g,
                b,
                a + Math.clamp(ArcaneClient.RANDOM.nextInt(-vary, vary), 0, 255)
        );
    }
}
