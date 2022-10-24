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
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import javax.inject.Inject;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.DoubleExtensions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Pair;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Pure;

@SarlSpecification("0.11")
@SarlElementType(19)
@SuppressWarnings("all")
public class Controller extends Agent {
  private LinkedHashMap<UUID, String> tradersList;
  
  private LinkedHashMap<UUID, String> buyersList;
  
  private final AtomicInteger count = new AtomicInteger();
  
  private String name;
  
  protected OpenEventSpace space;
  
  private LinkedHashMap<Integer, LinkedHashMap<String, Object>> gameResults = CollectionLiterals.<Integer, LinkedHashMap<String, Object>>newLinkedHashMap();
  
  private LinkedHashMap<String, Object> traderAcceptResult = null;
  
  private LinkedHashMap<String, Object> buyerAcceptResult = null;
  
  private boolean accepted = false;
  
  private Offer rejectOffer;
  
  private LinkedHashMap<UUID, ActorPersonality> actors;
  
  private void $behaviorUnit$Initialize$0(final Initialize occurrence) {
    Schedules _$CAPACITY_USE$IO_SARL_CORE_SCHEDULES$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_SCHEDULES$CALLER();
    final Procedure1<Agent> _function = (Agent it) -> {
      synchronized (this) {
        Logging _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER();
        Object _get = occurrence.parameters[1];
        _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER.setLoggingName((_get == null ? null : _get.toString()));
        DefaultContextInteractions _$CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER();
        Object _get_1 = occurrence.parameters[0];
        this.space = _$CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER.getDefaultContext().<OpenEventSpace>getSpace(((UUID) _get_1));
        Behaviors _$CAPACITY_USE$IO_SARL_CORE_BEHAVIORS$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_BEHAVIORS$CALLER();
        this.space.register(_$CAPACITY_USE$IO_SARL_CORE_BEHAVIORS$CALLER.asEventListener());
        Logging _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER_1 = this.$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER();
        Object _get_2 = occurrence.parameters[1];
        _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER_1.info((("The Actor " + _get_2) + " was started."));
        Object _get_3 = occurrence.parameters[1];
        this.name = (_get_3 == null ? null : _get_3.toString());
      }
    };
    _$CAPACITY_USE$IO_SARL_CORE_SCHEDULES$CALLER.in(1000, _function);
  }
  
  private void $behaviorUnit$StartGame$1(final StartGame occurrence) {
    this.tradersList = occurrence.tradersList;
    this.buyersList = occurrence.buyersList;
    if ((this.actors == null)) {
      this.actors = occurrence.actors;
    }
    Logging _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER();
    _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER.info("start new Game");
    ExternalContextAccess _$CAPACITY_USE$IO_SARL_CORE_EXTERNALCONTEXTACCESS$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_EXTERNALCONTEXTACCESS$CALLER();
    Count _count = new Count();
    _$CAPACITY_USE$IO_SARL_CORE_EXTERNALCONTEXTACCESS$CALLER.emit(this.space, _count);
  }
  
  @SyntheticMember
  @Pure
  private boolean $behaviorUnitGuard$StartGame$1(final StartGame it, final StartGame occurrence) {
    boolean _equals = Objects.equal(occurrence.name, this.name);
    return _equals;
  }
  
  private void $behaviorUnit$Play$2(final Play occurrence) {
    if (this.accepted) {
      if ((this.traderAcceptResult == null)) {
        Object _get = this.buyerAcceptResult.get("offer");
        Offer offer = ((Offer) _get);
        UUID _clientId = occurrence.offer.getClientId();
        UUID _clientId_1 = offer.getClientId();
        if ((_clientId != _clientId_1)) {
          Logging _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER();
          String _clientName = offer.getClientName();
          String _clientName_1 = occurrence.offer.getClientName();
          _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER.error(((("Results: " + _clientName) + " and ") + _clientName_1));
        }
      } else {
        if ((this.buyerAcceptResult == null)) {
          Object _get_1 = this.traderAcceptResult.get("offer");
          Offer offer_1 = ((Offer) _get_1);
          UUID _traderId = occurrence.offer.getTraderId();
          UUID _traderId_1 = offer_1.getTraderId();
          if ((_traderId != _traderId_1)) {
            Logging _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER_1 = this.$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER();
            UUID _traderId_2 = offer_1.getTraderId();
            String _clientName_2 = occurrence.offer.getClientName();
            _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER_1.error(((("Results: " + _traderId_2) + " and ") + _clientName_2));
          }
        }
      }
    }
    boolean _equals = occurrence.role.equals("Trader");
    if (_equals) {
      Pair<String, Object> _mappedTo = Pair.<String, Object>of("offer", occurrence.offer);
      Pair<String, Object> _mappedTo_1 = Pair.<String, Object>of("actor", occurrence.actor);
      Pair<String, Object> _mappedTo_2 = Pair.<String, Object>of("strat", Boolean.valueOf(occurrence.strat));
      this.traderAcceptResult = CollectionLiterals.<String, Object>newLinkedHashMap(_mappedTo, _mappedTo_1, _mappedTo_2);
    } else {
      boolean _equals_1 = occurrence.role.equals("Buyer");
      if (_equals_1) {
        Pair<String, Object> _mappedTo_3 = Pair.<String, Object>of("offer", occurrence.offer);
        Pair<String, Object> _mappedTo_4 = Pair.<String, Object>of("actor", occurrence.actor);
        Pair<String, Object> _mappedTo_5 = Pair.<String, Object>of("strat", Boolean.valueOf(occurrence.strat));
        this.buyerAcceptResult = CollectionLiterals.<String, Object>newLinkedHashMap(_mappedTo_3, _mappedTo_4, _mappedTo_5);
      }
    }
    if (((this.traderAcceptResult != null) && (this.buyerAcceptResult != null))) {
      this.calcResults();
    }
  }
  
  private void $behaviorUnit$Die$3(final Die occurrence) {
    Logging _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER();
    _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER.info("Dying");
    Lifecycle _$CAPACITY_USE$IO_SARL_CORE_LIFECYCLE$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_LIFECYCLE$CALLER();
    _$CAPACITY_USE$IO_SARL_CORE_LIFECYCLE$CALLER.killMe();
  }
  
  private void $behaviorUnit$Count$4(final Count occurrence) {
    synchronized (this) {
      Set<UUID> _keySet = this.tradersList.keySet();
      for (final UUID trader : _keySet) {
        {
          Logging _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER();
          _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER.info(("Trader: " + trader));
          ExternalContextAccess _$CAPACITY_USE$IO_SARL_CORE_EXTERNALCONTEXTACCESS$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_EXTERNALCONTEXTACCESS$CALLER();
          Inquiry _inquiry = new Inquiry(trader, this.buyersList);
          _$CAPACITY_USE$IO_SARL_CORE_EXTERNALCONTEXTACCESS$CALLER.emit(this.space, _inquiry);
        }
      }
    }
  }
  
  @SyntheticMember
  @Pure
  private boolean $behaviorUnitGuard$Count$4(final Count it, final Count occurrence) {
    boolean _isFromMe = (it != null && this.getID().equals(it.getSource().getUUID()));
    return _isFromMe;
  }
  
  private void $behaviorUnit$Clean$5(final Clean occurrence) {
    this.traderAcceptResult = null;
    this.buyerAcceptResult = null;
    this.accepted = false;
    this.rejectOffer = null;
    this.gameResults = CollectionLiterals.<Integer, LinkedHashMap<String, Object>>newLinkedHashMap();
  }
  
  private void $behaviorUnit$AcceptOffer$6(final AcceptOffer occurrence) {
    Offer offer = occurrence.offer;
    ExternalContextAccess _$CAPACITY_USE$IO_SARL_CORE_EXTERNALCONTEXTACCESS$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_EXTERNALCONTEXTACCESS$CALLER();
    InitPlay _initPlay = new InitPlay(offer);
    _$CAPACITY_USE$IO_SARL_CORE_EXTERNALCONTEXTACCESS$CALLER.emit(this.space, _initPlay);
  }
  
  private void $behaviorUnit$Reject$7(final Reject occurrence) {
    synchronized (this) {
      this.rejectOffer = occurrence.offer;
      UUID _traderId = this.rejectOffer.getTraderId();
      Pair<String, Object> _mappedTo = Pair.<String, Object>of("traderId", _traderId);
      String _traderName = this.rejectOffer.getTraderName();
      Pair<String, Object> _mappedTo_1 = Pair.<String, Object>of("traderName", _traderName);
      Pair<String, Object> _mappedTo_2 = Pair.<String, Object>of("utilTrader", Double.valueOf(0.0));
      UUID _clientId = this.rejectOffer.getClientId();
      Pair<String, Object> _mappedTo_3 = Pair.<String, Object>of("buyerId", _clientId);
      String _clientName = this.rejectOffer.getClientName();
      Pair<String, Object> _mappedTo_4 = Pair.<String, Object>of("buyerName", _clientName);
      Pair<String, Object> _mappedTo_5 = Pair.<String, Object>of("buyerUtil", Double.valueOf(0.0));
      Pair<String, Object> _mappedTo_6 = Pair.<String, Object>of("offer", this.rejectOffer);
      ArrayList<Object> _newArrayList = CollectionLiterals.<Object>newArrayList();
      Pair<String, Object> _mappedTo_7 = Pair.<String, Object>of("itemsTrader", _newArrayList);
      ArrayList<Object> _newArrayList_1 = CollectionLiterals.<Object>newArrayList();
      Pair<String, Object> _mappedTo_8 = Pair.<String, Object>of("itemsBuyer", _newArrayList_1);
      ActorPersonality _get = this.actors.get(this.rejectOffer.getTraderId());
      Pair<String, Object> _mappedTo_9 = Pair.<String, Object>of("TraderActor", _get);
      ActorPersonality _get_1 = this.actors.get(this.rejectOffer.getClientId());
      Pair<String, Object> _mappedTo_10 = Pair.<String, Object>of("BuyerActor", _get_1);
      this.gameResults.put(Integer.valueOf(1), 
        CollectionLiterals.<String, Object>newLinkedHashMap(_mappedTo, _mappedTo_1, _mappedTo_2, _mappedTo_3, _mappedTo_4, _mappedTo_5, _mappedTo_6, _mappedTo_7, _mappedTo_8, _mappedTo_9, _mappedTo_10));
      Schedules _$CAPACITY_USE$IO_SARL_CORE_SCHEDULES$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_SCHEDULES$CALLER();
      final Procedure1<Agent> _function = (Agent it) -> {
        DefaultContextInteractions _$CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER();
        TurnEnd _turnEnd = new TurnEnd(this.gameResults);
        _$CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER.emit(_turnEnd);
      };
      _$CAPACITY_USE$IO_SARL_CORE_SCHEDULES$CALLER.in(300, _function);
      Schedules _$CAPACITY_USE$IO_SARL_CORE_SCHEDULES$CALLER_1 = this.$CAPACITY_USE$IO_SARL_CORE_SCHEDULES$CALLER();
      final Procedure1<Agent> _function_1 = (Agent it) -> {
        ExternalContextAccess _$CAPACITY_USE$IO_SARL_CORE_EXTERNALCONTEXTACCESS$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_EXTERNALCONTEXTACCESS$CALLER();
        Clean _clean = new Clean();
        _$CAPACITY_USE$IO_SARL_CORE_EXTERNALCONTEXTACCESS$CALLER.emit(this.space, _clean);
      };
      _$CAPACITY_USE$IO_SARL_CORE_SCHEDULES$CALLER_1.in(300, _function_1);
    }
  }
  
  /**
   * Calculate Results from IDP
   */
  protected AgentTask calcResults() {
    AgentTask _xsynchronizedexpression = null;
    synchronized (this) {
      Schedules _$CAPACITY_USE$IO_SARL_CORE_SCHEDULES$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_SCHEDULES$CALLER();
      final Procedure1<Agent> _function = (Agent it) -> {
        try {
          Object _get = this.traderAcceptResult.get("actor");
          ActorPersonality trader = ((ActorPersonality) _get);
          Object _get_1 = this.buyerAcceptResult.get("actor");
          ActorPersonality buyer = ((ActorPersonality) _get_1);
          ExternalContextAccess _$CAPACITY_USE$IO_SARL_CORE_EXTERNALCONTEXTACCESS$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_EXTERNALCONTEXTACCESS$CALLER();
          UUID _tradingId = trader.getTradingId();
          Pair<String, UUID> _mappedTo = Pair.<String, UUID>of("id", _tradingId);
          Object _get_2 = this.traderAcceptResult.get("strat");
          Pair<String, Object> _mappedTo_1 = Pair.<String, Object>of("result", _get_2);
          LinkedHashMap<String, Object> _newLinkedHashMap = CollectionLiterals.<String, Object>newLinkedHashMap(_mappedTo, _mappedTo_1);
          Pair<String, LinkedHashMap<String, Object>> _mappedTo_2 = Pair.<String, LinkedHashMap<String, Object>>of("Trader", _newLinkedHashMap);
          UUID _tradingId_1 = buyer.getTradingId();
          Pair<String, UUID> _mappedTo_3 = Pair.<String, UUID>of("id", _tradingId_1);
          Object _get_3 = this.buyerAcceptResult.get("strat");
          Pair<String, Object> _mappedTo_4 = Pair.<String, Object>of("result", _get_3);
          LinkedHashMap<String, Object> _newLinkedHashMap_1 = CollectionLiterals.<String, Object>newLinkedHashMap(_mappedTo_3, _mappedTo_4);
          Pair<String, LinkedHashMap<String, Object>> _mappedTo_5 = Pair.<String, LinkedHashMap<String, Object>>of("Buyer", _newLinkedHashMap_1);
          LinkedHashMap<String, LinkedHashMap<String, Object>> _newLinkedHashMap_2 = CollectionLiterals.<String, LinkedHashMap<String, Object>>newLinkedHashMap(_mappedTo_2, _mappedTo_5);
          Notify _notify = new Notify(_newLinkedHashMap_2);
          _$CAPACITY_USE$IO_SARL_CORE_EXTERNALCONTEXTACCESS$CALLER.emit(this.space, _notify);
          if ((Objects.equal(this.traderAcceptResult.get("strat"), Boolean.valueOf(true)) && Objects.equal(this.buyerAcceptResult.get("strat"), Boolean.valueOf(true)))) {
            Object _get_4 = this.traderAcceptResult.get("offer");
            Offer offer = ((Offer) _get_4);
            LinkedHashMap<String, Double> utils = Utils.calcUtilForOffer(offer);
            UUID _traderId = offer.getTraderId();
            Pair<String, Object> _mappedTo_6 = Pair.<String, Object>of("traderId", _traderId);
            String _traderName = offer.getTraderName();
            Pair<String, Object> _mappedTo_7 = Pair.<String, Object>of("traderName", _traderName);
            Double _get_5 = utils.get("utilTrader");
            Pair<String, Object> _mappedTo_8 = Pair.<String, Object>of("utilTrader", _get_5);
            UUID _clientId = offer.getClientId();
            Pair<String, Object> _mappedTo_9 = Pair.<String, Object>of("buyerId", _clientId);
            String _clientName = offer.getClientName();
            Pair<String, Object> _mappedTo_10 = Pair.<String, Object>of("buyerName", _clientName);
            Double _get_6 = utils.get("utilBuyer");
            Pair<String, Object> _mappedTo_11 = Pair.<String, Object>of("buyerUtil", _get_6);
            Pair<String, Object> _mappedTo_12 = Pair.<String, Object>of("offer", offer);
            List<String> _offerItems = offer.getOfferItems();
            Pair<String, Object> _mappedTo_13 = Pair.<String, Object>of("itemsTrader", _offerItems);
            List<String> _traderItems = offer.getTraderItems();
            Pair<String, Object> _mappedTo_14 = Pair.<String, Object>of("itemsBuyer", _traderItems);
            Pair<String, Object> _mappedTo_15 = Pair.<String, Object>of("TraderActor", trader);
            Pair<String, Object> _mappedTo_16 = Pair.<String, Object>of("BuyerActor", buyer);
            this.gameResults.put(Integer.valueOf(1), 
              CollectionLiterals.<String, Object>newLinkedHashMap(_mappedTo_6, _mappedTo_7, _mappedTo_8, _mappedTo_9, _mappedTo_10, _mappedTo_11, _mappedTo_12, _mappedTo_13, _mappedTo_14, _mappedTo_15, _mappedTo_16));
          } else {
            if ((Objects.equal(this.traderAcceptResult.get("strat"), Boolean.valueOf(true)) && Objects.equal(this.buyerAcceptResult.get("strat"), Boolean.valueOf(false)))) {
              Object _get_7 = this.traderAcceptResult.get("offer");
              Offer offer_1 = ((Offer) _get_7);
              LinkedHashMap<String, Double> utils_1 = Utils.calcUtilForOffer(offer_1);
              ExternalContextAccess _$CAPACITY_USE$IO_SARL_CORE_EXTERNALCONTEXTACCESS$CALLER_1 = this.$CAPACITY_USE$IO_SARL_CORE_EXTERNALCONTEXTACCESS$CALLER();
              UUID _tradingId_2 = buyer.getTradingId();
              Punish _punish = new Punish(_tradingId_2);
              _$CAPACITY_USE$IO_SARL_CORE_EXTERNALCONTEXTACCESS$CALLER_1.emit(this.space, _punish);
              UUID _traderId_1 = offer_1.getTraderId();
              Pair<String, Object> _mappedTo_17 = Pair.<String, Object>of("traderId", _traderId_1);
              String _traderName_1 = offer_1.getTraderName();
              Pair<String, Object> _mappedTo_18 = Pair.<String, Object>of("traderName", _traderName_1);
              Pair<String, Object> _mappedTo_19 = Pair.<String, Object>of("utilTrader", Double.valueOf(0.0));
              UUID _clientId_1 = offer_1.getClientId();
              Pair<String, Object> _mappedTo_20 = Pair.<String, Object>of("buyerId", _clientId_1);
              String _clientName_1 = offer_1.getClientName();
              Pair<String, Object> _mappedTo_21 = Pair.<String, Object>of("buyerName", _clientName_1);
              Double _get_8 = utils_1.get("utilTrader");
              Double _get_9 = utils_1.get("utilBuyer");
              double _plus = DoubleExtensions.operator_plus(_get_8, _get_9);
              Pair<String, Object> _mappedTo_22 = Pair.<String, Object>of("buyerUtil", Double.valueOf(_plus));
              Pair<String, Object> _mappedTo_23 = Pair.<String, Object>of("offer", offer_1);
              ArrayList<Object> _newArrayList = CollectionLiterals.<Object>newArrayList();
              Pair<String, Object> _mappedTo_24 = Pair.<String, Object>of("itemsTrader", _newArrayList);
              List<String> _traderItems_1 = offer_1.getTraderItems();
              Pair<String, Object> _mappedTo_25 = Pair.<String, Object>of("itemsBuyer", _traderItems_1);
              Pair<String, Object> _mappedTo_26 = Pair.<String, Object>of("TraderActor", trader);
              Pair<String, Object> _mappedTo_27 = Pair.<String, Object>of("BuyerActor", buyer);
              this.gameResults.put(Integer.valueOf(1), 
                CollectionLiterals.<String, Object>newLinkedHashMap(_mappedTo_17, _mappedTo_18, _mappedTo_19, _mappedTo_20, _mappedTo_21, _mappedTo_22, _mappedTo_23, _mappedTo_24, _mappedTo_25, _mappedTo_26, _mappedTo_27));
            } else {
              if ((Objects.equal(this.traderAcceptResult.get("strat"), Boolean.valueOf(false)) && Objects.equal(this.buyerAcceptResult.get("strat"), Boolean.valueOf(true)))) {
                Object _get_10 = this.traderAcceptResult.get("offer");
                Offer offer_2 = ((Offer) _get_10);
                LinkedHashMap<String, Double> utils_2 = Utils.calcUtilForOffer(offer_2);
                ExternalContextAccess _$CAPACITY_USE$IO_SARL_CORE_EXTERNALCONTEXTACCESS$CALLER_2 = this.$CAPACITY_USE$IO_SARL_CORE_EXTERNALCONTEXTACCESS$CALLER();
                UUID _tradingId_3 = trader.getTradingId();
                Punish _punish_1 = new Punish(_tradingId_3);
                _$CAPACITY_USE$IO_SARL_CORE_EXTERNALCONTEXTACCESS$CALLER_2.emit(this.space, _punish_1);
                UUID _traderId_2 = offer_2.getTraderId();
                Pair<String, Object> _mappedTo_28 = Pair.<String, Object>of("traderId", _traderId_2);
                String _traderName_2 = offer_2.getTraderName();
                Pair<String, Object> _mappedTo_29 = Pair.<String, Object>of("traderName", _traderName_2);
                Double _get_11 = utils_2.get("utilTrader");
                Double _get_12 = utils_2.get("utilBuyer");
                double _plus_1 = DoubleExtensions.operator_plus(_get_11, _get_12);
                Pair<String, Object> _mappedTo_30 = Pair.<String, Object>of("utilTrader", Double.valueOf(_plus_1));
                UUID _clientId_2 = offer_2.getClientId();
                Pair<String, Object> _mappedTo_31 = Pair.<String, Object>of("buyerId", _clientId_2);
                String _clientName_2 = offer_2.getClientName();
                Pair<String, Object> _mappedTo_32 = Pair.<String, Object>of("buyerName", _clientName_2);
                Pair<String, Object> _mappedTo_33 = Pair.<String, Object>of("buyerUtil", Double.valueOf(0.0));
                Pair<String, Object> _mappedTo_34 = Pair.<String, Object>of("offer", offer_2);
                List<String> _offerItems_1 = offer_2.getOfferItems();
                Pair<String, Object> _mappedTo_35 = Pair.<String, Object>of("itemsTrader", _offerItems_1);
                ArrayList<Object> _newArrayList_1 = CollectionLiterals.<Object>newArrayList();
                Pair<String, Object> _mappedTo_36 = Pair.<String, Object>of("itemsBuyer", _newArrayList_1);
                Pair<String, Object> _mappedTo_37 = Pair.<String, Object>of("TraderActor", trader);
                Pair<String, Object> _mappedTo_38 = Pair.<String, Object>of("BuyerActor", buyer);
                this.gameResults.put(Integer.valueOf(1), 
                  CollectionLiterals.<String, Object>newLinkedHashMap(_mappedTo_28, _mappedTo_29, _mappedTo_30, _mappedTo_31, _mappedTo_32, _mappedTo_33, _mappedTo_34, _mappedTo_35, _mappedTo_36, _mappedTo_37, _mappedTo_38));
              } else {
                if ((Objects.equal(this.traderAcceptResult.get("strat"), Boolean.valueOf(false)) && Objects.equal(this.buyerAcceptResult.get("strat"), Boolean.valueOf(false)))) {
                  Object _get_13 = this.traderAcceptResult.get("offer");
                  Offer offer_3 = ((Offer) _get_13);
                  LinkedHashMap<String, Double> utils_3 = Utils.calcUtilForOffer(offer_3);
                  UUID _traderId_3 = offer_3.getTraderId();
                  Pair<String, Object> _mappedTo_39 = Pair.<String, Object>of("traderId", _traderId_3);
                  String _traderName_3 = offer_3.getTraderName();
                  Pair<String, Object> _mappedTo_40 = Pair.<String, Object>of("traderName", _traderName_3);
                  Pair<String, Object> _mappedTo_41 = Pair.<String, Object>of("utilTrader", Double.valueOf(0.0));
                  UUID _clientId_3 = offer_3.getClientId();
                  Pair<String, Object> _mappedTo_42 = Pair.<String, Object>of("buyerId", _clientId_3);
                  String _clientName_3 = offer_3.getClientName();
                  Pair<String, Object> _mappedTo_43 = Pair.<String, Object>of("buyerName", _clientName_3);
                  Pair<String, Object> _mappedTo_44 = Pair.<String, Object>of("buyerUtil", Double.valueOf(0.0));
                  Pair<String, Object> _mappedTo_45 = Pair.<String, Object>of("offer", offer_3);
                  ArrayList<Object> _newArrayList_2 = CollectionLiterals.<Object>newArrayList();
                  Pair<String, Object> _mappedTo_46 = Pair.<String, Object>of("itemsTrader", _newArrayList_2);
                  ArrayList<Object> _newArrayList_3 = CollectionLiterals.<Object>newArrayList();
                  Pair<String, Object> _mappedTo_47 = Pair.<String, Object>of("itemsBuyer", _newArrayList_3);
                  Pair<String, Object> _mappedTo_48 = Pair.<String, Object>of("TraderActor", trader);
                  Pair<String, Object> _mappedTo_49 = Pair.<String, Object>of("BuyerActor", buyer);
                  this.gameResults.put(Integer.valueOf(1), 
                    CollectionLiterals.<String, Object>newLinkedHashMap(_mappedTo_39, _mappedTo_40, _mappedTo_41, _mappedTo_42, _mappedTo_43, _mappedTo_44, _mappedTo_45, _mappedTo_46, _mappedTo_47, _mappedTo_48, _mappedTo_49));
                } else {
                  Object _get_14 = this.traderAcceptResult.get("offer");
                  Offer offer_4 = ((Offer) _get_14);
                  Pair<String, Object> _mappedTo_50 = Pair.<String, Object>of("Error", offer_4);
                  this.gameResults.put((this.count == null ? null : Integer.valueOf(this.count.intValue())), CollectionLiterals.<String, Object>newLinkedHashMap(_mappedTo_50));
                  Logging _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER();
                  _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER.error(((("Results: " + this.traderAcceptResult) + " and ") + this.buyerAcceptResult));
                }
              }
            }
          }
        } finally {
          Schedules _$CAPACITY_USE$IO_SARL_CORE_SCHEDULES$CALLER_1 = this.$CAPACITY_USE$IO_SARL_CORE_SCHEDULES$CALLER();
          final Procedure1<Agent> _function_1 = (Agent it_1) -> {
            DefaultContextInteractions _$CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER();
            TurnEnd _turnEnd = new TurnEnd(this.gameResults);
            _$CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER.emit(_turnEnd);
          };
          _$CAPACITY_USE$IO_SARL_CORE_SCHEDULES$CALLER_1.in(100, _function_1);
          Schedules _$CAPACITY_USE$IO_SARL_CORE_SCHEDULES$CALLER_2 = this.$CAPACITY_USE$IO_SARL_CORE_SCHEDULES$CALLER();
          final Procedure1<Agent> _function_2 = (Agent it_1) -> {
            ExternalContextAccess _$CAPACITY_USE$IO_SARL_CORE_EXTERNALCONTEXTACCESS$CALLER_3 = this.$CAPACITY_USE$IO_SARL_CORE_EXTERNALCONTEXTACCESS$CALLER();
            Clean _clean = new Clean();
            _$CAPACITY_USE$IO_SARL_CORE_EXTERNALCONTEXTACCESS$CALLER_3.emit(this.space, _clean);
          };
          _$CAPACITY_USE$IO_SARL_CORE_SCHEDULES$CALLER_2.in(300, _function_2);
        }
      };
      _xsynchronizedexpression = _$CAPACITY_USE$IO_SARL_CORE_SCHEDULES$CALLER.in(500, _function);
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
  private void $guardEvaluator$Play(final Play occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$Play$2(occurrence));
  }
  
  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$Die(final Die occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$Die$3(occurrence));
  }
  
  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$Reject(final Reject occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$Reject$7(occurrence));
  }
  
  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$StartGame(final StartGame occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    if ($behaviorUnitGuard$StartGame$1(occurrence, occurrence)) {
      ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$StartGame$1(occurrence));
    }
  }
  
  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$Count(final Count occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    if ($behaviorUnitGuard$Count$4(occurrence, occurrence)) {
      ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$Count$4(occurrence));
    }
  }
  
  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$Clean(final Clean occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$Clean$5(occurrence));
  }
  
  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$AcceptOffer(final AcceptOffer occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$AcceptOffer$6(occurrence));
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
    Controller other = (Controller) obj;
    if (!java.util.Objects.equals(this.name, other.name))
      return false;
    if (other.accepted != this.accepted)
      return false;
    return super.equals(obj);
  }
  
  @Override
  @Pure
  @SyntheticMember
  public int hashCode() {
    int result = super.hashCode();
    final int prime = 31;
    result = prime * result + java.util.Objects.hashCode(this.name);
    result = prime * result + Boolean.hashCode(this.accepted);
    return result;
  }
  
  @SyntheticMember
  public Controller(final UUID parentID, final UUID agentID) {
    super(parentID, agentID);
  }
  
  @SyntheticMember
  @Inject
  @Deprecated
  public Controller(final BuiltinCapacitiesProvider provider, final UUID parentID, final UUID agentID) {
    super(provider, parentID, agentID);
  }
  
  @SyntheticMember
  @Inject
  public Controller(final UUID parentID, final UUID agentID, final DynamicSkillProvider skillProvider) {
    super(parentID, agentID, skillProvider);
  }
}
