import io.sarl.lang.annotation.SarlElementType;
import io.sarl.lang.annotation.SarlSpecification;
import io.sarl.lang.annotation.SyntheticMember;
import io.sarl.lang.core.Address;
import io.sarl.lang.core.Event;

@SarlSpecification("0.11")
@SarlElementType(15)
@SuppressWarnings("all")
public class EndGame extends Event {
  @SyntheticMember
  public EndGame() {
    super();
  }
  
  @SyntheticMember
  public EndGame(final Address source) {
    super(source);
  }
  
  @SyntheticMember
  private static final long serialVersionUID = 588368462L;
}
