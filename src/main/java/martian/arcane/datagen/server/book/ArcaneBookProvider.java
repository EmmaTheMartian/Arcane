package martian.arcane.datagen.server.book;

import com.klikli_dev.modonomicon.api.datagen.BookProvider;
import com.klikli_dev.modonomicon.api.datagen.ModonomiconLanguageProvider;
import com.klikli_dev.modonomicon.api.datagen.book.BookModel;
import martian.arcane.ArcaneMod;
import net.minecraft.data.PackOutput;

public class ArcaneBookProvider extends BookProvider {
    public ArcaneBookProvider(PackOutput packOutput, ModonomiconLanguageProvider lang) {
        super("arcane_guidebook", packOutput, ArcaneMod.MODID, lang);
    }

    @Override
    protected void registerDefaultMacros() {

    }

    @Override
    protected BookModel generateBook() {
        this.lang().add(this.context.bookName(), "Dux ad Arcane Artium");
        this.lang().add(this.context.bookTooltip(), "\"A Guide to the Arcane Arts\"");

        var features = new ArcaneBookFeaturesProvider(this).generate();

        return BookModel
                .create(modLoc(this.context.bookId()), this.context.bookName())
                .withTooltip(this.context.bookTooltip())
                .withGenerateBookItem(false)
                .withCustomBookItem(ArcaneMod.id("guidebook"))
                .withCreativeTab(ArcaneMod.id("arcane_tab"))
                .withCategories(features);
    }
}
