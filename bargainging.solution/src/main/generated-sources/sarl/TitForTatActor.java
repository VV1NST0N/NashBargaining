import io.sarl.lang.annotation.SarlElementType;
import io.sarl.lang.annotation.SarlSpecification;
import io.sarl.lang.annotation.SyntheticMember;
import java.util.List;
import java.util.UUID;

@SarlSpecification("0.11")
@SarlElementType(10)
@SuppressWarnings("all")
public class TitForTatActor extends ActorPersonality {
  public boolean playStrat(final UUID tradingId) {
    if (((((this.playersLastTurns.get(tradingId)) == null ? false : (this.playersLastTurns.get(tradingId)).booleanValue()) == true) || (this.playersLastTurns.get(tradingId) == null))) {
      return true;
    } else {
      return false;
    }
  }
  
  @SyntheticMember
  public TitForTatActor(final UUID tradingId, final String name, final List<String> items, final Double money, final Double trustScore, final Double baseTolerance, final String role) {
    super(tradingId, name, items, money, trustScore, baseTolerance, role);
  }
}
