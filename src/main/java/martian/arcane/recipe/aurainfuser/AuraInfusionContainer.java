package martian.arcane.recipe.aurainfuser;

import martian.arcane.api.recipe.SimpleContainer;
import net.minecraftforge.items.IItemHandlerModifiable;

public class AuraInfusionContainer extends SimpleContainer {
    public final int aura;

    public AuraInfusionContainer(IItemHandlerModifiable items, int aura) {
        super(items);
        this.aura = aura;
    }
}
