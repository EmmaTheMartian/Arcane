package martian.arcane.api;

public interface IMachineTierable {
    boolean upgradeTo(MachineTier newTier);
    boolean isUpgradable();
}
