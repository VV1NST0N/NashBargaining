import io.sarl.lang.annotation.SarlElementType;
import io.sarl.lang.annotation.SarlSpecification;
import io.sarl.lang.annotation.SyntheticMember;
import io.sarl.lang.core.Event;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Objects;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;

@SarlSpecification("0.11")
@SarlElementType(15)
@SuppressWarnings("all")
public class Acception extends Event {
  public ArrayList<LinkedHashMap<String, Object>> offer;
  
  public String name;
  
  public boolean result;
  
  public Acception(final String name, final ArrayList<LinkedHashMap<String, Object>> offer) {
    this.name = name;
    this.offer = offer;
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
    Acception other = (Acception) obj;
    if (!Objects.equals(this.name, other.name))
      return false;
    if (other.result != this.result)
      return false;
    return super.equals(obj);
  }
  
  @Override
  @Pure
  @SyntheticMember
  public int hashCode() {
    int result = super.hashCode();
    final int prime = 31;
    result = prime * result + Objects.hashCode(this.name);
    result = prime * result + Boolean.hashCode(this.result);
    return result;
  }
  
  /**
   * Returns a String representation of the Acception event's attributes only.
   */
  @SyntheticMember
  @Pure
  protected void toString(final ToStringBuilder builder) {
    super.toString(builder);
    builder.add("offer", this.offer);
    builder.add("name", this.name);
    builder.add("result", this.result);
  }
  
  @SyntheticMember
  private static final long serialVersionUID = -1721274631L;
}
