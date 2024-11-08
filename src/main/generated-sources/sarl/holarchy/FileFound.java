package holarchy;

import io.sarl.lang.core.Event;
import io.sarl.lang.core.annotation.SarlElementType;
import io.sarl.lang.core.annotation.SarlSpecification;
import io.sarl.lang.core.annotation.SyntheticMember;
import java.io.File;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;

/**
 * Event emitted when an agent founds a file
 */
@SarlSpecification("0.13")
@SarlElementType(15)
@SuppressWarnings("all")
public class FileFound extends Event {
  public final File path;

  public FileFound(final File path) {
    this.path = path;
  }

  @Override
  @Pure
  @SyntheticMember
  public boolean equals(final Object obj) {
    return super.equals(obj);
  }

  @Override
  @Pure
  @SyntheticMember
  public int hashCode() {
    int result = super.hashCode();
    return result;
  }

  /**
   * Returns a String representation of the FileFound event's attributes only.
   */
  @SyntheticMember
  @Pure
  protected void toString(final ToStringBuilder builder) {
    super.toString(builder);
    builder.add("path", this.path);
  }

  @SyntheticMember
  private static final long serialVersionUID = 4329631178L;
}
