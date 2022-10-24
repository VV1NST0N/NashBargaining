import io.sarl.lang.annotation.SarlElementType;
import io.sarl.lang.annotation.SarlSpecification;
import io.sarl.lang.annotation.SyntheticMember;
import io.sarl.lang.core.Event;
import java.util.Objects;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;

@SarlSpecification("0.11")
@SarlElementType(15)
@SuppressWarnings("all")
public class Play extends Event {
  public boolean strat;
  
  public ActorPersonality actor;
  
  public Offer offer;
  
  public String role;
  
  public Play(final Offer offer, final ActorPersonality actor, final boolean strat, final String role) {
    this.strat = strat;
    this.actor = actor;
    this.offer = offer;
    this.role = role;
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
    Play other = (Play) obj;
    if (other.strat != this.strat)
      return false;
    if (!Objects.equals(this.role, other.role))
      return false;
    return super.equals(obj);
  }
  
  @Override
  @Pure
  @SyntheticMember
  public int hashCode() {
    int result = super.hashCode();
    final int prime = 31;
    result = prime * result + Boolean.hashCode(this.strat);
    result = prime * result + Objects.hashCode(this.role);
    return result;
  }
  
  /**
   * Returns a String representation of the Play event's attributes only.
   */
  @SyntheticMember
  @Pure
  protected void toString(final ToStringBuilder builder) {
    super.toString(builder);
    builder.add("strat", this.strat);
    builder.add("actor", this.actor);
    builder.add("offer", this.offer);
    builder.add("role", this.role);
  }
  
  @SyntheticMember
  private static final long serialVersionUID = -1329211082L;
}
