import com.google.common.base.Objects;
import io.sarl.core.ExternalContextAccess;
import io.sarl.core.Logging;
import io.sarl.core.OpenEventSpace;
import io.sarl.lang.annotation.ImportedCapacityFeature;
import io.sarl.lang.annotation.PerceptGuardEvaluator;
import io.sarl.lang.annotation.SarlElementType;
import io.sarl.lang.annotation.SarlSpecification;
import io.sarl.lang.annotation.SyntheticMember;
import io.sarl.lang.core.Agent;
import io.sarl.lang.core.AtomicSkillReference;
import io.sarl.lang.core.BuiltinCapacitiesProvider;
import io.sarl.lang.core.DynamicSkillProvider;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;
import javax.inject.Inject;
import org.eclipse.xtext.xbase.lib.DoubleExtensions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Pure;

@SarlSpecification("0.11")
@SarlElementType(19)
@SuppressWarnings("all")
public class BaseActor extends Agent {
  protected OpenEventSpace space;
  
  protected LinkedHashMap<UUID, String> playersList;
  
  protected ActorPersonality me;
  
  protected UUID tradingId;
  
  protected Object lockObj = new Object();
  
  protected Double toleranceToReachMaxGenUtil;
  
  protected Double toleranceToReachMaxOwnUtil;
  
  private void $behaviorUnit$Punish$0(final Punish occurrence) {
    Double trust = this.me.getTrustScore();
    if ((trust.doubleValue() <= 0.0)) {
      this.me.setTrustScore(Double.valueOf(0.0));
    }
    this.me.setTrustScore(Double.valueOf((((trust) == null ? 0 : (trust).doubleValue()) - 10.0)));
    Logging _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER();
    Double _trustScore = this.me.getTrustScore();
    _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER.info(("New TrustScore: " + _trustScore));
  }
  
  @SyntheticMember
  @Pure
  private boolean $behaviorUnitGuard$Punish$0(final Punish it, final Punish occurrence) {
    boolean _equals = Objects.equal(occurrence.id, this.tradingId);
    return _equals;
  }
  
  protected Offer buildNewOffer(final Offer offer, final List<String> traderItems, final List<String> buyersItems, final Double amount) {
    offer.setTraderItems(traderItems);
    offer.setOfferItems(buyersItems);
    return offer;
  }
  
  @Pure
  protected Offer buildOffer(final UUID trader, final List<String> myItems, final UUID buyer, final List<String> buyersItems, final Double amount, final String traderName, final String buyerName) {
    UUID id = UUID.randomUUID();
    Offer newOffer = new Offer(id, trader, buyer, buyersItems, myItems, amount, traderName, buyerName);
    return newOffer;
  }
  
  /**
   * Lowering Tolerance after one Bargaining round was unsuccessful
   */
  protected void lowerTolerances(final Double value) {
    synchronized (this.lockObj) {
      Logging _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER();
      _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER.info(((("First 1: " + this.toleranceToReachMaxGenUtil) + " 2: ") + this.toleranceToReachMaxOwnUtil));
      this.toleranceToReachMaxGenUtil = Double.valueOf((((this.toleranceToReachMaxGenUtil) == null ? 0 : (this.toleranceToReachMaxGenUtil).doubleValue()) - (((value) == null ? 0 : (value).doubleValue()) * 1.3)));
      double _minus = DoubleExtensions.operator_minus(this.toleranceToReachMaxOwnUtil, value);
      this.toleranceToReachMaxOwnUtil = Double.valueOf(_minus);
      Logging _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER_1 = this.$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER();
      _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER_1.info(((("Last 1: " + this.toleranceToReachMaxGenUtil) + " 2: ") + this.toleranceToReachMaxOwnUtil));
    }
  }
  
  protected Double resetTolerances() {
    Double _xblockexpression = null;
    {
      this.toleranceToReachMaxGenUtil = this.me.getBaseTolerance();
      _xblockexpression = this.toleranceToReachMaxOwnUtil = this.me.getBaseTolerance();
    }
    return _xblockexpression;
  }
  
  @Extension
  @ImportedCapacityFeature(Logging.class)
  @SyntheticMember
  private transient AtomicSkillReference $CAPACITY_USE$IO_SARL_CORE_LOGGING;
  
  @SyntheticMember
  @Pure
  private Logging $CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER() {
    if (this.$CAPACITY_USE$IO_SARL_CORE_LOGGING == null || this.$CAPACITY_USE$IO_SARL_CORE_LOGGING.get() == null) {
      this.$CAPACITY_USE$IO_SARL_CORE_LOGGING = $getSkill(Logging.class);
    }
    return $castSkill(Logging.class, this.$CAPACITY_USE$IO_SARL_CORE_LOGGING);
  }
  
  @Extension
  @ImportedCapacityFeature(ExternalContextAccess.class)
  @SyntheticMember
  private transient AtomicSkillReference $CAPACITY_USE$IO_SARL_CORE_EXTERNALCONTEXTACCESS;
  
  @SyntheticMember
  @Pure
  private ExternalContextAccess $CAPACITY_USE$IO_SARL_CORE_EXTERNALCONTEXTACCESS$CALLER() {
    if (this.$CAPACITY_USE$IO_SARL_CORE_EXTERNALCONTEXTACCESS == null || this.$CAPACITY_USE$IO_SARL_CORE_EXTERNALCONTEXTACCESS.get() == null) {
      this.$CAPACITY_USE$IO_SARL_CORE_EXTERNALCONTEXTACCESS = $getSkill(ExternalContextAccess.class);
    }
    return $castSkill(ExternalContextAccess.class, this.$CAPACITY_USE$IO_SARL_CORE_EXTERNALCONTEXTACCESS);
  }
  
  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$Punish(final Punish occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    if ($behaviorUnitGuard$Punish$0(occurrence, occurrence)) {
      ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$Punish$0(occurrence));
    }
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
    BaseActor other = (BaseActor) obj;
    if (!java.util.Objects.equals(this.tradingId, other.tradingId))
      return false;
    if (other.toleranceToReachMaxGenUtil == null) {
      if (this.toleranceToReachMaxGenUtil != null)
        return false;
    } else if (this.toleranceToReachMaxGenUtil == null)
      return false;
    if (other.toleranceToReachMaxGenUtil != null && Double.doubleToLongBits(other.toleranceToReachMaxGenUtil.doubleValue()) != Double.doubleToLongBits(this.toleranceToReachMaxGenUtil.doubleValue()))
      return false;
    if (other.toleranceToReachMaxOwnUtil == null) {
      if (this.toleranceToReachMaxOwnUtil != null)
        return false;
    } else if (this.toleranceToReachMaxOwnUtil == null)
      return false;
    if (other.toleranceToReachMaxOwnUtil != null && Double.doubleToLongBits(other.toleranceToReachMaxOwnUtil.doubleValue()) != Double.doubleToLongBits(this.toleranceToReachMaxOwnUtil.doubleValue()))
      return false;
    return super.equals(obj);
  }
  
  @Override
  @Pure
  @SyntheticMember
  public int hashCode() {
    int result = super.hashCode();
    final int prime = 31;
    result = prime * result + java.util.Objects.hashCode(this.tradingId);
    result = prime * result + java.util.Objects.hashCode(this.toleranceToReachMaxGenUtil);
    result = prime * result + java.util.Objects.hashCode(this.toleranceToReachMaxOwnUtil);
    return result;
  }
  
  @SyntheticMember
  public BaseActor(final UUID parentID, final UUID agentID) {
    super(parentID, agentID);
  }
  
  @SyntheticMember
  @Inject
  @Deprecated
  public BaseActor(final BuiltinCapacitiesProvider provider, final UUID parentID, final UUID agentID) {
    super(provider, parentID, agentID);
  }
  
  @SyntheticMember
  @Inject
  public BaseActor(final UUID parentID, final UUID agentID, final DynamicSkillProvider skillProvider) {
    super(parentID, agentID, skillProvider);
  }
}
