import io.sarl.lang.annotation.DefaultValue;
import io.sarl.lang.annotation.DefaultValueSource;
import io.sarl.lang.annotation.SarlElementType;
import io.sarl.lang.annotation.SarlSourceCode;
import io.sarl.lang.annotation.SarlSpecification;
import io.sarl.lang.annotation.SyntheticMember;
import io.sarl.lang.core.Event;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.UUID;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;

@SarlSpecification("0.11")
@SarlElementType(15)
@SuppressWarnings("all")
public class StartGame extends Event {
  public LinkedHashMap<UUID, String> tradersList;
  
  public String name;
  
  public LinkedHashMap<UUID, String> buyersList;
  
  public LinkedHashMap<UUID, ActorPersonality> actors;
  
  @DefaultValueSource
  public StartGame(final LinkedHashMap<UUID, String> tradersList, final String name, final LinkedHashMap<UUID, String> buyersList, @DefaultValue("StartGame#NEW_0") final LinkedHashMap<UUID, ActorPersonality> actors) {
    this.name = name;
    this.tradersList = tradersList;
    this.buyersList = buyersList;
    this.actors = actors;
  }
  
  /**
   * Default value for the parameter actors
   */
  @SyntheticMember
  @SarlSourceCode("newLinkedHashMap")
  private static final LinkedHashMap $DEFAULT_VALUE$NEW_0 = CollectionLiterals.<Object, Object>newLinkedHashMap();
  
  public StartGame(final LinkedHashMap<UUID, String> tradersList, final String name, final LinkedHashMap<UUID, String> buyersList) {
    this.name = name;
    this.tradersList = tradersList;
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
    StartGame other = (StartGame) obj;
    if (!Objects.equals(this.name, other.name))
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
    return result;
  }
  
  /**
   * Returns a String representation of the StartGame event's attributes only.
   */
  @SyntheticMember
  @Pure
  protected void toString(final ToStringBuilder builder) {
    super.toString(builder);
    builder.add("tradersList", this.tradersList);
    builder.add("name", this.name);
    builder.add("buyersList", this.buyersList);
    builder.add("actors", this.actors);
  }
  
  @SyntheticMember
  private static final long serialVersionUID = -2666807953L;
}
