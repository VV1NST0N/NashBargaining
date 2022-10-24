import io.sarl.lang.annotation.SarlElementType;
import io.sarl.lang.annotation.SarlSpecification;
import io.sarl.lang.annotation.SyntheticMember;
import io.sarl.lang.core.Address;
import io.sarl.lang.core.Event;

@SarlSpecification("0.11")
@SarlElementType(15)
@SuppressWarnings("all")
public class CalcResults extends Event {
  @SyntheticMember
  public CalcResults() {
    super();
  }
  
  @SyntheticMember
  public CalcResults(final Address source) {
    super(source);
  }
  
  @SyntheticMember
  private static final long serialVersionUID = 588368462L;
}
