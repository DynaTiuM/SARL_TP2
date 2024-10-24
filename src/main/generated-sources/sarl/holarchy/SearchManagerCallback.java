package holarchy;

import io.sarl.lang.core.annotation.SarlElementType;
import io.sarl.lang.core.annotation.SarlSpecification;

@FunctionalInterface
@SarlSpecification("0.13")
@SarlElementType(11)
@SuppressWarnings("all")
public interface SearchManagerCallback {
  void onSearch(final String path, final String criteria);
}
