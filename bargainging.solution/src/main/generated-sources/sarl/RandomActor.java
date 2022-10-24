import io.sarl.lang.annotation.SarlElementType;
import io.sarl.lang.annotation.SarlSpecification;
import io.sarl.lang.annotation.SyntheticMember;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import org.eclipse.xtext.xbase.lib.Pure;

@SarlSpecification("0.11")
@SarlElementType(10)
@SuppressWarnings("all")
public class RandomActor extends ActorPersonality {
  private final Random random = new Random();
  
  public boolean playStrat(final UUID tradingId) {
    return this.random.nextBoolean();
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
  
  @SyntheticMember
  public RandomActor(final UUID tradingId, final String name, final List<String> items, final Double money, final Double trustScore, final Double baseTolerance, final String role) {
    super(tradingId, name, items, money, trustScore, baseTolerance, role);
  }
}
