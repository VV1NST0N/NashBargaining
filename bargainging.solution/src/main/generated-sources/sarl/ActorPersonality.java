import io.sarl.lang.annotation.SarlElementType;
import io.sarl.lang.annotation.SarlSpecification;
import io.sarl.lang.annotation.SyntheticMember;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Pure;

@SarlSpecification("0.11")
@SarlElementType(10)
@SuppressWarnings("all")
public abstract class ActorPersonality {
  private String name;
  
  private UUID tradingId;
  
  private Double trustScore;
  
  private List<String> items;
  
  private Double money;
  
  private LinkedHashMap<UUID, LinkedHashMap<UUID, LinkedHashMap<String, Object>>> myUtils;
  
  private Double baseTolerance;
  
  private Double toleranceChangeRate = Double.valueOf(0.1);
  
  private String role;
  
  protected LinkedHashMap<UUID, Boolean> playersLastTurns = CollectionLiterals.<UUID, Boolean>newLinkedHashMap();
  
  public ActorPersonality(final UUID tradingId, final String name, final List<String> items, final Double money, final Double trustScore, final Double baseTolerance, final String role) {
    this.name = name;
    this.tradingId = tradingId;
    this.trustScore = trustScore;
    this.items = items;
    this.baseTolerance = baseTolerance;
    this.role = role;
  }
  
  @Pure
  public String getRole() {
    return this.role;
  }
  
  public void setRole(final String role) {
    this.role = role;
  }
  
  @Pure
  public String getName() {
    return this.name;
  }
  
  public void setName(final String name) {
    this.name = name;
  }
  
  @Pure
  public UUID getTradingId() {
    return this.tradingId;
  }
  
  public void setTradingId(final UUID tradingId) {
    this.tradingId = tradingId;
  }
  
  @Pure
  public Double getTrustScore() {
    return this.trustScore;
  }
  
  public void setTrustScore(final Double trustScore) {
    this.trustScore = trustScore;
  }
  
  @Pure
  public List<String> getItems() {
    return this.items;
  }
  
  public void setItems(final List<String> items) {
    this.items = items;
  }
  
  @Pure
  public Double getMoney() {
    return this.money;
  }
  
  public void setMoney(final Double money) {
    this.money = money;
  }
  
  @Pure
  public LinkedHashMap<UUID, LinkedHashMap<UUID, LinkedHashMap<String, Object>>> getMyUtils() {
    return this.myUtils;
  }
  
  public void setMyUtils(final LinkedHashMap<UUID, LinkedHashMap<UUID, LinkedHashMap<String, Object>>> myUtils) {
    this.myUtils = myUtils;
  }
  
  @Pure
  public Double getBaseTolerance() {
    return this.baseTolerance;
  }
  
  public void setBaseTolerance(final Double baseTolerance) {
    this.baseTolerance = baseTolerance;
  }
  
  @Pure
  public LinkedHashMap<String, Object> getUtilByActorIdAndUtilId(final UUID buyer, final UUID utilId) {
    return this.myUtils.get(buyer).get(utilId);
  }
  
  @Pure
  public LinkedHashMap<UUID, LinkedHashMap<String, Object>> getUtilsByBuyer(final UUID buyer) {
    return this.myUtils.get(buyer);
  }
  
  @Pure
  public ActorPersonality getActor() {
    return this;
  }
  
  public Boolean setPlayerBeavhiour(final UUID tradingId, final boolean strat) {
    return this.playersLastTurns.put(tradingId, Boolean.valueOf(strat));
  }
  
  public void setToleranceChangeRate(final Double rate) {
    this.toleranceChangeRate = rate;
  }
  
  @Pure
  public Double getToleranceChangeRate() {
    return this.toleranceChangeRate;
  }
  
  public abstract boolean playStrat(final UUID tradingId);
  
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
    ActorPersonality other = (ActorPersonality) obj;
    if (!Objects.equals(this.name, other.name))
      return false;
    if (!Objects.equals(this.tradingId, other.tradingId))
      return false;
    if (other.trustScore == null) {
      if (this.trustScore != null)
        return false;
    } else if (this.trustScore == null)
      return false;
    if (other.trustScore != null && Double.doubleToLongBits(other.trustScore.doubleValue()) != Double.doubleToLongBits(this.trustScore.doubleValue()))
      return false;
    if (other.money == null) {
      if (this.money != null)
        return false;
    } else if (this.money == null)
      return false;
    if (other.money != null && Double.doubleToLongBits(other.money.doubleValue()) != Double.doubleToLongBits(this.money.doubleValue()))
      return false;
    if (other.baseTolerance == null) {
      if (this.baseTolerance != null)
        return false;
    } else if (this.baseTolerance == null)
      return false;
    if (other.baseTolerance != null && Double.doubleToLongBits(other.baseTolerance.doubleValue()) != Double.doubleToLongBits(this.baseTolerance.doubleValue()))
      return false;
    if (other.toleranceChangeRate == null) {
      if (this.toleranceChangeRate != null)
        return false;
    } else if (this.toleranceChangeRate == null)
      return false;
    if (other.toleranceChangeRate != null && Double.doubleToLongBits(other.toleranceChangeRate.doubleValue()) != Double.doubleToLongBits(this.toleranceChangeRate.doubleValue()))
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
    result = prime * result + Objects.hashCode(this.name);
    result = prime * result + Objects.hashCode(this.tradingId);
    result = prime * result + Objects.hashCode(this.trustScore);
    result = prime * result + Objects.hashCode(this.money);
    result = prime * result + Objects.hashCode(this.baseTolerance);
    result = prime * result + Objects.hashCode(this.toleranceChangeRate);
    result = prime * result + Objects.hashCode(this.role);
    return result;
  }
}
