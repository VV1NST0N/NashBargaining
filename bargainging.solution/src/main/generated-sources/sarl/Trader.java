import com.google.common.base.Objects;
import io.sarl.core.AgentTask;
import io.sarl.core.Behaviors;
import io.sarl.core.DefaultContextInteractions;
import io.sarl.core.ExternalContextAccess;
import io.sarl.core.Initialize;
import io.sarl.core.Lifecycle;
import io.sarl.core.Logging;
import io.sarl.core.OpenEventSpace;
import io.sarl.core.Schedules;
import io.sarl.lang.annotation.ImportedCapacityFeature;
import io.sarl.lang.annotation.PerceptGuardEvaluator;
import io.sarl.lang.annotation.SarlElementType;
import io.sarl.lang.annotation.SarlSpecification;
import io.sarl.lang.annotation.SyntheticMember;
import io.sarl.lang.core.Agent;
import io.sarl.lang.core.AtomicSkillReference;
import io.sarl.lang.core.BuiltinCapacitiesProvider;
import io.sarl.lang.core.DynamicSkillProvider;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import javax.inject.Inject;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.DoubleExtensions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Pure;

@SarlSpecification("0.11")
@SarlElementType(19)
@SuppressWarnings("all")
public class Trader extends BaseActor {
  private int numBargainRounds = 0;
  
  private LinkedHashMap<UUID, TreeMap<UUID, Double>> sortedUtilsByBuyer = CollectionLiterals.<UUID, TreeMap<UUID, Double>>newLinkedHashMap();
  
  private void $behaviorUnit$Initialize$0(final Initialize occurrence) {
    synchronized (this) {
      DefaultContextInteractions _$CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER();
      Object _get = occurrence.parameters[0];
      this.space = _$CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER.getDefaultContext().<OpenEventSpace>getSpace(((UUID) _get));
      Behaviors _$CAPACITY_USE$IO_SARL_CORE_BEHAVIORS$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_BEHAVIORS$CALLER();
      this.space.register(_$CAPACITY_USE$IO_SARL_CORE_BEHAVIORS$CALLER.asEventListener());
      Object _get_1 = occurrence.parameters[1];
      this.tradingId = ((UUID) _get_1);
      Object _get_2 = occurrence.parameters[2];
      this.me = ((ActorPersonality) _get_2);
      this.toleranceToReachMaxOwnUtil = this.me.getBaseTolerance();
      this.toleranceToReachMaxGenUtil = this.me.getBaseTolerance();
      Logging _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER();
      _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER.setLoggingName(this.me.getName());
      Logging _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER_1 = this.$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER();
      String _name = this.me.getName();
      _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER_1.info((("The Actor " + _name) + " was started."));
    }
  }
  
  private void $behaviorUnit$Inquiry$1(final Inquiry occurrence) {
    Logging _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER();
    String _name = this.me.getName();
    _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER.info((((("Trader: " + this.tradingId) + "  ") + _name) + " init Round"));
    this.playersList = occurrence.buyersList;
    Set<UUID> _keySet = this.playersList.keySet();
    for (final UUID buyer : _keySet) {
      {
        this.sortedUtilsByBuyer.put(buyer, Utils.getSortedUtilsForOpponent(this.me.getUtilsByBuyer(buyer)));
        LinkedHashMap<String, Object> highestPersonalUtil = this.me.getUtilByActorIdAndUtilId(buyer, 
          this.sortedUtilsByBuyer.get(buyer).lastKey());
        Object _get = highestPersonalUtil.get("tradeItems");
        List<String> tradeItems = ((ArrayList) _get);
        Object _get_1 = highestPersonalUtil.get("offerItems");
        List<String> offerItems = ((ArrayList) _get_1);
        Offer myOffer = this.buildOffer(this.tradingId, tradeItems, buyer, offerItems, Double.valueOf(0.0), this.me.getName(), null);
        myOffer.setTraderTrustScore(this.me.getTrustScore());
        ExternalContextAccess _$CAPACITY_USE$IO_SARL_CORE_EXTERNALCONTEXTACCESS$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_EXTERNALCONTEXTACCESS$CALLER();
        MakeOffer _makeOffer = new MakeOffer(myOffer);
        _$CAPACITY_USE$IO_SARL_CORE_EXTERNALCONTEXTACCESS$CALLER.emit(this.space, _makeOffer);
      }
    }
  }
  
  @SyntheticMember
  @Pure
  private boolean $behaviorUnitGuard$Inquiry$1(final Inquiry it, final Inquiry occurrence) {
    boolean _equals = Objects.equal(occurrence.id, this.tradingId);
    return _equals;
  }
  
  private void $behaviorUnit$MakeOffer$2(final MakeOffer occurrence) {
    Offer offer = occurrence.offer;
    UUID _traderId = offer.getTraderId();
    boolean _equals = Objects.equal(_traderId, this.tradingId);
    if (_equals) {
      this.checkOffer(offer);
    }
  }
  
  @SyntheticMember
  @Pure
  private boolean $behaviorUnitGuard$MakeOffer$2(final MakeOffer it, final MakeOffer occurrence) {
    boolean _isFromMe = (it != null && this.getID().equals(it.getSource().getUUID()));
    return (!_isFromMe);
  }
  
  private void $behaviorUnit$InitPlay$3(final InitPlay occurrence) {
    ExternalContextAccess _$CAPACITY_USE$IO_SARL_CORE_EXTERNALCONTEXTACCESS$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_EXTERNALCONTEXTACCESS$CALLER();
    boolean _playStrat = this.me.playStrat(occurrence.offer.getClientId());
    String _role = this.me.getRole();
    Play _play = new Play(occurrence.offer, this.me, _playStrat, _role);
    _$CAPACITY_USE$IO_SARL_CORE_EXTERNALCONTEXTACCESS$CALLER.emit(this.space, _play);
  }
  
  @SyntheticMember
  @Pure
  private boolean $behaviorUnitGuard$InitPlay$3(final InitPlay it, final InitPlay occurrence) {
    UUID _traderId = occurrence.offer.getTraderId();
    boolean _equals = Objects.equal(_traderId, this.tradingId);
    return _equals;
  }
  
  private void $behaviorUnit$Notify$4(final Notify occurrence) {
    Object _get = occurrence.resultMap.get("Buyer").get("id");
    UUID id = ((UUID) _get);
    Object _get_1 = occurrence.resultMap.get("Buyer").get("result");
    Boolean result = ((Boolean) _get_1);
    this.me.setPlayerBeavhiour(id, ((result) == null ? false : (result).booleanValue()));
  }
  
  @SyntheticMember
  @Pure
  private boolean $behaviorUnitGuard$Notify$4(final Notify it, final Notify occurrence) {
    Object _get = occurrence.resultMap.get("Trader").get("id");
    boolean _equals = Objects.equal(_get, this.tradingId);
    return _equals;
  }
  
  private void $behaviorUnit$TransportGoods$5(final TransportGoods occurrence) {
    Object _get = occurrence.resultMap.get("items");
    List items = ((List) _get);
    Object _get_1 = occurrence.resultMap.get("utils");
    LinkedHashMap utils = ((LinkedHashMap) _get_1);
    this.me.setItems(items);
    this.me.setMyUtils(utils);
  }
  
  @SyntheticMember
  @Pure
  private boolean $behaviorUnitGuard$TransportGoods$5(final TransportGoods it, final TransportGoods occurrence) {
    Object _get = occurrence.resultMap.get("id");
    boolean _equals = Objects.equal(_get, this.tradingId);
    return _equals;
  }
  
  private void $behaviorUnit$Clean$6(final Clean occurrence) {
    this.sortedUtilsByBuyer = CollectionLiterals.<UUID, TreeMap<UUID, Double>>newLinkedHashMap();
    this.numBargainRounds = 0;
    this.resetTolerances();
  }
  
  private void $behaviorUnit$CleanBatch$7(final CleanBatch occurrence) {
  }
  
  private void $behaviorUnit$Die$8(final Die occurrence) {
    Logging _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER();
    _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER.info("Stopping");
    Lifecycle _$CAPACITY_USE$IO_SARL_CORE_LIFECYCLE$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_LIFECYCLE$CALLER();
    _$CAPACITY_USE$IO_SARL_CORE_LIFECYCLE$CALLER.killMe();
  }
  
  protected AgentTask checkOffer(final Offer offer) {
    AgentTask _xifexpression = null;
    if ((offer != null)) {
      AgentTask _xblockexpression = null;
      {
        LinkedHashMap<String, Double> util = Utils.calcUtilForOffer(offer);
        double minAccept = Math.sqrt(((util.get("genUtil")) == null ? 0 : (util.get("genUtil")).doubleValue()));
        minAccept = (minAccept * ((this.toleranceToReachMaxOwnUtil) == null ? 0 : (this.toleranceToReachMaxOwnUtil).doubleValue()));
        AgentTask _xifexpression_1 = null;
        if (((util.get("utilTrader").doubleValue() >= minAccept) && 
          (util.get("genUtil").doubleValue() >= DoubleExtensions.operator_multiply(Utils.getMaxUtil(offer), this.toleranceToReachMaxGenUtil)))) {
          Logging _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER();
          _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER.info(("Accept Offer: " + offer));
          ExternalContextAccess _$CAPACITY_USE$IO_SARL_CORE_EXTERNALCONTEXTACCESS$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_EXTERNALCONTEXTACCESS$CALLER();
          AcceptOffer _acceptOffer = new AcceptOffer(offer);
          _$CAPACITY_USE$IO_SARL_CORE_EXTERNALCONTEXTACCESS$CALLER.emit(this.space, _acceptOffer);
        } else {
          _xifexpression_1 = this.evaluateCounterOffer(offer);
        }
        _xblockexpression = _xifexpression_1;
      }
      _xifexpression = _xblockexpression;
    } else {
      _xifexpression = this.evaluateCounterOffer(offer);
    }
    return _xifexpression;
  }
  
  protected AgentTask evaluateCounterOffer(final Offer oldOffer) {
    AgentTask _xsynchronizedexpression = null;
    synchronized (this.lockObj) {
      AgentTask _xblockexpression = null;
      {
        UUID buyer = oldOffer.getClientId();
        double amount = 0.0;
        List<String> items = null;
        AgentTask _xifexpression = null;
        if (((this.numBargainRounds >= 7) || (oldOffer.getBuyerTrustScore().doubleValue() < 35.0))) {
          Logging _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER();
          _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER.info(oldOffer.getNumBargainRounds());
          Logging _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER_1 = this.$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER();
          _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER_1.info("Reject!");
          ExternalContextAccess _$CAPACITY_USE$IO_SARL_CORE_EXTERNALCONTEXTACCESS$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_EXTERNALCONTEXTACCESS$CALLER();
          Reject _reject = new Reject(oldOffer);
          _$CAPACITY_USE$IO_SARL_CORE_EXTERNALCONTEXTACCESS$CALLER.emit(this.space, _reject);
        } else {
          AgentTask _xifexpression_1 = null;
          TreeMap<UUID, Double> _get = this.sortedUtilsByBuyer.get(buyer);
          if ((_get == null)) {
            this.sortedUtilsByBuyer.put(buyer, Utils.getSortedUtilsForOpponent(this.me.getUtilsByBuyer(buyer)));
            int _size = this.sortedUtilsByBuyer.size();
            if ((_size <= 0)) {
              ExternalContextAccess _$CAPACITY_USE$IO_SARL_CORE_EXTERNALCONTEXTACCESS$CALLER_1 = this.$CAPACITY_USE$IO_SARL_CORE_EXTERNALCONTEXTACCESS$CALLER();
              Reject _reject_1 = new Reject(oldOffer);
              _$CAPACITY_USE$IO_SARL_CORE_EXTERNALCONTEXTACCESS$CALLER_1.emit(this.space, _reject_1);
            }
            LinkedHashMap<String, Object> highestPersonalUtil = this.me.getUtilByActorIdAndUtilId(buyer, 
              this.sortedUtilsByBuyer.get(buyer).lastKey());
            Object _get_1 = highestPersonalUtil.get("tradeItems");
            List<String> tradeItems = ((ArrayList) _get_1);
            Object _get_2 = highestPersonalUtil.get("offerItems");
            List<String> offerItems = ((ArrayList) _get_2);
            Offer myOffer = this.buildNewOffer(oldOffer, tradeItems, offerItems, Double.valueOf(0.0));
            ExternalContextAccess _$CAPACITY_USE$IO_SARL_CORE_EXTERNALCONTEXTACCESS$CALLER_2 = this.$CAPACITY_USE$IO_SARL_CORE_EXTERNALCONTEXTACCESS$CALLER();
            MakeOffer _makeOffer = new MakeOffer(myOffer);
            _$CAPACITY_USE$IO_SARL_CORE_EXTERNALCONTEXTACCESS$CALLER_2.emit(this.space, _makeOffer);
          } else {
            AgentTask _xblockexpression_1 = null;
            {
              int _size_1 = this.sortedUtilsByBuyer.get(buyer).keySet().size();
              if ((_size_1 > 0)) {
                this.sortedUtilsByBuyer.get(buyer).remove(this.sortedUtilsByBuyer.get(buyer).lastKey());
              }
              AgentTask _xifexpression_2 = null;
              int _size_2 = this.sortedUtilsByBuyer.get(buyer).size();
              if ((_size_2 == 0)) {
                AgentTask _xblockexpression_2 = null;
                {
                  Double _toleranceChangeRate = this.me.getToleranceChangeRate();
                  Double _traderTrustScore = oldOffer.getTraderTrustScore();
                  this.lowerTolerances(Double.valueOf((((_toleranceChangeRate) == null ? 0 : (_toleranceChangeRate).doubleValue()) * (((_traderTrustScore) == null ? 0 : (_traderTrustScore).doubleValue()) / 100))));
                  this.sortedUtilsByBuyer.remove(buyer);
                  this.numBargainRounds++;
                  Logging _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER_2 = this.$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER();
                  _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER_2.info(Integer.valueOf(this.numBargainRounds));
                  Schedules _$CAPACITY_USE$IO_SARL_CORE_SCHEDULES$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_SCHEDULES$CALLER();
                  final Procedure1<Agent> _function = (Agent it) -> {
                    this.evaluateCounterOffer(oldOffer);
                  };
                  _xblockexpression_2 = _$CAPACITY_USE$IO_SARL_CORE_SCHEDULES$CALLER.in(200, _function);
                }
                _xifexpression_2 = _xblockexpression_2;
              } else {
                LinkedHashMap<String, Object> highestPersonalUtil_1 = this.me.getUtilsByBuyer(buyer).get(
                  this.sortedUtilsByBuyer.get(buyer).lastKey());
                Object _get_3 = highestPersonalUtil_1.get("tradeItems");
                List<String> tradeItems_1 = ((ArrayList) _get_3);
                Object _get_4 = highestPersonalUtil_1.get("offerItems");
                List<String> offerItems_1 = ((ArrayList) _get_4);
                Offer myOffer_1 = this.buildNewOffer(oldOffer, tradeItems_1, offerItems_1, Double.valueOf(0.0));
                ExternalContextAccess _$CAPACITY_USE$IO_SARL_CORE_EXTERNALCONTEXTACCESS$CALLER_3 = this.$CAPACITY_USE$IO_SARL_CORE_EXTERNALCONTEXTACCESS$CALLER();
                MakeOffer _makeOffer_1 = new MakeOffer(myOffer_1);
                _$CAPACITY_USE$IO_SARL_CORE_EXTERNALCONTEXTACCESS$CALLER_3.emit(this.space, _makeOffer_1);
              }
              _xblockexpression_1 = _xifexpression_2;
            }
            _xifexpression_1 = _xblockexpression_1;
          }
          _xifexpression = _xifexpression_1;
        }
        _xblockexpression = _xifexpression;
      }
      _xsynchronizedexpression = _xblockexpression;
    }
    return _xsynchronizedexpression;
  }
  
  @Extension
  @ImportedCapacityFeature(Lifecycle.class)
  @SyntheticMember
  private transient AtomicSkillReference $CAPACITY_USE$IO_SARL_CORE_LIFECYCLE;
  
  @SyntheticMember
  @Pure
  private Lifecycle $CAPACITY_USE$IO_SARL_CORE_LIFECYCLE$CALLER() {
    if (this.$CAPACITY_USE$IO_SARL_CORE_LIFECYCLE == null || this.$CAPACITY_USE$IO_SARL_CORE_LIFECYCLE.get() == null) {
      this.$CAPACITY_USE$IO_SARL_CORE_LIFECYCLE = $getSkill(Lifecycle.class);
    }
    return $castSkill(Lifecycle.class, this.$CAPACITY_USE$IO_SARL_CORE_LIFECYCLE);
  }
  
  @Extension
  @ImportedCapacityFeature(Schedules.class)
  @SyntheticMember
  private transient AtomicSkillReference $CAPACITY_USE$IO_SARL_CORE_SCHEDULES;
  
  @SyntheticMember
  @Pure
  private Schedules $CAPACITY_USE$IO_SARL_CORE_SCHEDULES$CALLER() {
    if (this.$CAPACITY_USE$IO_SARL_CORE_SCHEDULES == null || this.$CAPACITY_USE$IO_SARL_CORE_SCHEDULES.get() == null) {
      this.$CAPACITY_USE$IO_SARL_CORE_SCHEDULES = $getSkill(Schedules.class);
    }
    return $castSkill(Schedules.class, this.$CAPACITY_USE$IO_SARL_CORE_SCHEDULES);
  }
  
  @Extension
  @ImportedCapacityFeature(DefaultContextInteractions.class)
  @SyntheticMember
  private transient AtomicSkillReference $CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS;
  
  @SyntheticMember
  @Pure
  private DefaultContextInteractions $CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER() {
    if (this.$CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS == null || this.$CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS.get() == null) {
      this.$CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS = $getSkill(DefaultContextInteractions.class);
    }
    return $castSkill(DefaultContextInteractions.class, this.$CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS);
  }
  
  @Extension
  @ImportedCapacityFeature(Behaviors.class)
  @SyntheticMember
  private transient AtomicSkillReference $CAPACITY_USE$IO_SARL_CORE_BEHAVIORS;
  
  @SyntheticMember
  @Pure
  private Behaviors $CAPACITY_USE$IO_SARL_CORE_BEHAVIORS$CALLER() {
    if (this.$CAPACITY_USE$IO_SARL_CORE_BEHAVIORS == null || this.$CAPACITY_USE$IO_SARL_CORE_BEHAVIORS.get() == null) {
      this.$CAPACITY_USE$IO_SARL_CORE_BEHAVIORS = $getSkill(Behaviors.class);
    }
    return $castSkill(Behaviors.class, this.$CAPACITY_USE$IO_SARL_CORE_BEHAVIORS);
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
  private void $guardEvaluator$Initialize(final Initialize occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$Initialize$0(occurrence));
  }
  
  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$Die(final Die occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$Die$8(occurrence));
  }
  
  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$TransportGoods(final TransportGoods occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    if ($behaviorUnitGuard$TransportGoods$5(occurrence, occurrence)) {
      ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$TransportGoods$5(occurrence));
    }
  }
  
  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$Notify(final Notify occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    if ($behaviorUnitGuard$Notify$4(occurrence, occurrence)) {
      ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$Notify$4(occurrence));
    }
  }
  
  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$MakeOffer(final MakeOffer occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    if ($behaviorUnitGuard$MakeOffer$2(occurrence, occurrence)) {
      ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$MakeOffer$2(occurrence));
    }
  }
  
  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$Clean(final Clean occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$Clean$6(occurrence));
  }
  
  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$Inquiry(final Inquiry occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    if ($behaviorUnitGuard$Inquiry$1(occurrence, occurrence)) {
      ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$Inquiry$1(occurrence));
    }
  }
  
  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$InitPlay(final InitPlay occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    if ($behaviorUnitGuard$InitPlay$3(occurrence, occurrence)) {
      ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$InitPlay$3(occurrence));
    }
  }
  
  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$CleanBatch(final CleanBatch occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$CleanBatch$7(occurrence));
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
    Trader other = (Trader) obj;
    if (other.numBargainRounds != this.numBargainRounds)
      return false;
    return super.equals(obj);
  }
  
  @Override
  @Pure
  @SyntheticMember
  public int hashCode() {
    int result = super.hashCode();
    final int prime = 31;
    result = prime * result + Integer.hashCode(this.numBargainRounds);
    return result;
  }
  
  @SyntheticMember
  public Trader(final UUID parentID, final UUID agentID) {
    super(parentID, agentID);
  }
  
  @SyntheticMember
  @Inject
  @Deprecated
  public Trader(final BuiltinCapacitiesProvider provider, final UUID parentID, final UUID agentID) {
    super(provider, parentID, agentID);
  }
  
  @SyntheticMember
  @Inject
  public Trader(final UUID parentID, final UUID agentID, final DynamicSkillProvider skillProvider) {
    super(parentID, agentID, skillProvider);
  }
}
