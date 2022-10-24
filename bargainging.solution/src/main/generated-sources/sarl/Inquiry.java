import io.sarl.lang.annotation.SarlElementType;
import io.sarl.lang.annotation.SarlSpecification;
import io.sarl.lang.annotation.SyntheticMember;
import io.sarl.lang.core.Event;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.UUID;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;

@SarlSpecification("0.11")
@SarlElementType(15)
@SuppressWarnings("all")
public class Inquiry extends Event {
  public UUID id;
  
  public LinkedHashMap<UUID, String> buyersList;
  
  public Inquiry(final UUID id, final LinkedHashMap<UUID, String> buyersList) {
    this.id = id;
    this.buyersList = buyersList;
  }
  
  @Override
  @Pure
  @SyntheticMember
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Inquiry other = (Inquiry) obj;
    if (!Objects.equals(this.id, other.id))
      return false;
    return super.equals(obj);
  }
  
  @Override
  @Pure
  @SyntheticMember
  public int hashCode() {
    int result = super.hashCode();
    final int prime = 31;
    result = prime * result + Objects.hashCode(this.id);
    return result;
  }
  
  /**
   * Returns a String representation of the Inquiry event's attributes only.
   */
  @SyntheticMember
  @Pure
  protected void toString(final ToStringBuilder builder) {
    super.toString(builder);
    builder.add("id", this.id);
    builder.add("buyersList", this.buyersList);
  }
  
  @SyntheticMember
  private static final long serialVersionUID = 242838162L;
}
