package martian.arcane.api.spell;

import com.electronwill.nightconfig.core.file.FileConfig;
import net.minecraft.resources.ResourceLocation;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

public class SpellConfig {
    protected final ResourceLocation spellId;
    protected final String filePath;
    protected final FileConfig config;

    public SpellConfig(ResourceLocation spellId) {
        this.spellId = spellId;
        this.filePath = "config/" + spellId.getNamespace() + "/spells/" + spellId.getPath() + ".toml";
        this.config = FileConfig.of(filePath);
    }

    public SpellConfig withConfig(Consumer<FileConfig> consumer) {
        consumer.accept(config);
        return this;
    }

    public <T> T get(String key) {
        return config.get(key);
    }

    public SpellConfig set(String key, Object value) {
        config.set(key, value);
        return this;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public SpellConfig build() {
        if (!config.getFile().exists()) {
            try {
                new File("config/" + spellId.getNamespace() + "/spells/").mkdirs();
                config.getFile().createNewFile();
            } catch (IOException e) {
                throw new RuntimeException("Arcane (" + spellId + "): Failed to create config file at " + filePath + "\nError:" + e);
            }

            config.save();
        }

        config.load();
        return this;
    }

    public void save() {
        config.save();
    }

    public void load() {
        config.load();
    }

    public void close() {
        config.close();
    }

    public static SpellConfig basicConfig(ResourceLocation id, int auraCost, int cooldown, int minLevel) {
        return new SpellConfig(id)
                .set("auraCost", auraCost)
                .set("cooldown", cooldown)
                .set("minLevel", minLevel);
    }
}
