package holarchy;

import io.sarl.lang.core.annotation.SarlElementType;
import io.sarl.lang.core.annotation.SarlSpecification;
import java.io.File;
import java.util.concurrent.ConcurrentLinkedQueue;

@FunctionalInterface
@SarlSpecification("0.13")
@SarlElementType(11)
@SuppressWarnings("all")
public interface SearchResultCallback {
  void onSearchCompleted(final ConcurrentLinkedQueue<File> foundFiles);
}
