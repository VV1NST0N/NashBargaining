import io.sarl.lang.annotation.SarlElementType;
import io.sarl.lang.annotation.SarlSpecification;
import io.sarl.lang.annotation.SyntheticMember;
import java.util.List;
import java.util.UUID;

@SarlSpecification("0.11")
@SarlElementType(10)
@SuppressWarnings("all")
public class DeflectingActor extends ActorPersonality {
  public boolean playStrat(final UUID tradingId) {
    return false;
  }
  
  @SyntheticMember
  public DeflectingActor(final UUID tradingId, final String name, final List<String> items, final Double money, final Double trustScore, final Double baseTolerance, final String role) {
    super(tradingId, name, items, money, trustScore, baseTolerance, role);
  }
}
