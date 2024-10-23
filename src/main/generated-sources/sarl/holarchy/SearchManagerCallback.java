package holarchy;

import io.sarl.lang.core.annotation.SarlElementType;
import io.sarl.lang.core.annotation.SarlSpecification;
import io.sarl.lang.core.annotation.SyntheticMember;

@SarlSpecification("0.13")
@SarlElementType(10)
@SuppressWarnings("all")
public abstract class SearchManagerCallback {
  public abstract void onSearch(final String path, final String criteria);

  @SyntheticMember
  public SearchManagerCallback() {
    super();
  }
}
