package martian.arcane.api;

import net.minecraft.core.NonNullList;

public class ListHelpers {
    @SafeVarargs
    public static <T> NonNullList<T> nonNullListOf(T... elements) {
        //noinspection DataFlowIssue
        return NonNullList.of(null, elements);
    }
}
