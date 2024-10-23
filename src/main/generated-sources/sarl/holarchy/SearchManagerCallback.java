package holarchy;

import io.sarl.lang.core.AgentTrait;
import io.sarl.lang.core.Capacity;
import io.sarl.lang.core.annotation.SarlElementType;
import io.sarl.lang.core.annotation.SarlSpecification;

@FunctionalInterface
@SarlSpecification("0.13")
@SarlElementType(20)
@SuppressWarnings("all")
public interface SearchManagerCallback extends Capacity {
  void onSearch(final String inputText, final String selectedOption);

  /**
   * @ExcludeFromApidoc
   */
  class ContextAwareCapacityWrapper<C extends SearchManagerCallback> extends Capacity.ContextAwareCapacityWrapper<C> implements SearchManagerCallback {
    public ContextAwareCapacityWrapper(final C capacity, final AgentTrait caller) {
      super(capacity, caller);
    }

    public void onSearch(final String inputText, final String selectedOption) {
      try {
        ensureCallerInLocalThread();
        this.capacity.onSearch(inputText, selectedOption);
      } finally {
        resetCallerInLocalThread();
      }
    }
  }
}
