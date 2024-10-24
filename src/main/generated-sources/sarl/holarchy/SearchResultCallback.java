package holarchy;

import io.sarl.lang.core.annotation.SarlElementType;
import io.sarl.lang.core.annotation.SarlSpecification;
import io.sarl.lang.core.annotation.SyntheticMember;
import java.io.File;
import java.util.concurrent.ConcurrentLinkedQueue;

@SarlSpecification("0.13")
@SarlElementType(10)
@SuppressWarnings("all")
public abstract class SearchResultCallback {
  public abstract void onSearchCompleted(final ConcurrentLinkedQueue<File> foundFiles);

  @SyntheticMember
  public SearchResultCallback() {
    super();
  }
}
