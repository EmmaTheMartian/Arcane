package martian.arcane.block;

import martian.arcane.api.PropertyHelpers;
import martian.arcane.api.block.AbstractAuraMachine;
import martian.arcane.block.entity.BlockEntityAuraExtractor;

public class BlockAuraExtractor extends AbstractAuraMachine {
    public BlockAuraExtractor() {
        super(PropertyHelpers.basicAuraMachine(), BlockEntityAuraExtractor::new);
    }
}
