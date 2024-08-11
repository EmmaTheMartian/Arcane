package martian.arcane.api.machine;

public interface IMachineTierable {
    boolean upgradeTo(MachineTier newTier);
    boolean isUpgradable();
}
