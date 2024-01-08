package martian.arcane.block;

import martian.arcane.api.PropertyHelpers;
import martian.arcane.api.block.AbstractAuraMachine;
import martian.arcane.block.entity.BlockEntityAuraBasin;

public class BlockAuraBasin extends AbstractAuraMachine {
    public BlockAuraBasin() {
        super(PropertyHelpers.basicAuraMachine(), BlockEntityAuraBasin::new);
    }
}
