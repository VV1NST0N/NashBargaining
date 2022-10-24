import bargainging.solution.JavaUtils;
import com.google.common.base.Objects;
import io.sarl.core.AgentTask;
import io.sarl.core.DefaultContextInteractions;
import io.sarl.core.Initialize;
import io.sarl.core.Lifecycle;
import io.sarl.core.Logging;
import io.sarl.core.OpenEventSpace;
import io.sarl.core.OpenEventSpaceSpecification;
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
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.function.Predicate;
import javax.inject.Inject;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.DoubleExtensions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.Pair;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Pure;

@SarlSpecification("0.11")
@SarlElementType(19)
@SuppressWarnings("all")
public class Spectator extends Agent {
  /**
   * Game Setup
   */
  private LinkedHashMap<String, Object> gameConfig;
  
  private LinkedHashMap<UUID, LinkedHashMap<String, Double>> itemValues = CollectionLiterals.<UUID, LinkedHashMap<String, Double>>newLinkedHashMap();
  
  private List<UUID> buyers = CollectionLiterals.<UUID>newArrayList();
  
  private List<UUID> traders = CollectionLiterals.<UUID>newArrayList();
  
  private LinkedHashMap<UUID, ActorPersonality> actors = CollectionLiterals.<UUID, ActorPersonality>newLinkedHashMap();
  
  private boolean randomVals = true;
  
  private Double min = Double.valueOf(0.0);
  
  private Double max = Double.valueOf(40.0);
  
  private Object lockObj = new Object();
  
  /**
   * Result Setup
   */
  private LinkedHashMap<Integer, LinkedHashMap<String, Object>> results = CollectionLiterals.<Integer, LinkedHashMap<String, Object>>newLinkedHashMap();
  
  private int round = 1;
  
  private File file = new File("D:\\sample.csv");
  
  private Writer writer = new Function0<Writer>() {
    @Override
    public Writer apply() {
      try {
        FileWriter _fileWriter = new FileWriter(Spectator.this.file, true);
        return _fileWriter;
      } catch (Throwable _e) {
        throw Exceptions.sneakyThrow(_e);
      }
    }
  }.apply();
  
  private LinkedHashMap<Integer, LinkedHashMap<String, LinkedHashMap<UUID, String>>> gamePairs = CollectionLiterals.<Integer, LinkedHashMap<String, LinkedHashMap<UUID, String>>>newLinkedHashMap();
  
  private void $behaviorUnit$Initialize$0(final Initialize occurrence) {
    try {
      Logging _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER();
      _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER.setLoggingName("Spectator");
      JavaUtils util = new JavaUtils();
      this.gameConfig = util.loadGameConfig();
      Logging _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER_1 = this.$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER();
      _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER_1.info(Integer.valueOf(this.gameConfig.keySet().size()));
      this.buildGameSetupFromConfig();
      this.bargaining();
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  private void $behaviorUnit$TurnEnd$1(final TurnEnd occurrence) {
    Logging _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER();
    _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER.info("Turn End");
    Set _keySet = occurrence.results.keySet();
    for (final Object gameRound : _keySet) {
      {
        Object _get = occurrence.results.get(gameRound);
        LinkedHashMap roundMap = ((LinkedHashMap) _get);
        Object _get_1 = roundMap.get("itemsTrader");
        List itemsTrader = ((List) _get_1);
        Object _get_2 = roundMap.get("itemsBuyer");
        List itemsBuyer = ((List) _get_2);
        this.results.put(Integer.valueOf(this.round), roundMap);
        this.round++;
        Object _get_3 = roundMap.get("TraderActor");
        ActorPersonality trader = this.rebalanceItems(((ActorPersonality) _get_3), itemsBuyer, itemsTrader);
        Object _get_4 = roundMap.get("BuyerActor");
        ActorPersonality buyer = this.rebalanceItems(((ActorPersonality) _get_4), itemsTrader, itemsBuyer);
        Schedules _$CAPACITY_USE$IO_SARL_CORE_SCHEDULES$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_SCHEDULES$CALLER();
        final Procedure1<Agent> _function = (Agent it) -> {
          this.calculateChangedUtils(trader, buyer);
        };
        _$CAPACITY_USE$IO_SARL_CORE_SCHEDULES$CALLER.in(1000, _function);
      }
    }
  }
  
  protected AgentTask bargaining() {
    AgentTask _xblockexpression = null;
    {
      Schedules _$CAPACITY_USE$IO_SARL_CORE_SCHEDULES$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_SCHEDULES$CALLER();
      AgentTask waitTask = _$CAPACITY_USE$IO_SARL_CORE_SCHEDULES$CALLER.task("wait-task");
      LinkedHashMap<Integer, LinkedHashMap<String, Double>> gameResults = CollectionLiterals.<Integer, LinkedHashMap<String, Double>>newLinkedHashMap();
      UUID spaceId = UUID.randomUUID();
      DefaultContextInteractions _$CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER();
      OpenEventSpace newSpace = _$CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER.getDefaultContext().<OpenEventSpace>createSpace(OpenEventSpaceSpecification.class, spaceId);
      Logging _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER();
      _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER.info(this.actors.keySet());
      Logging _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER_1 = this.$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER();
      _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER_1.info("build pairs");
      this.buildPairs();
      Schedules _$CAPACITY_USE$IO_SARL_CORE_SCHEDULES$CALLER_1 = this.$CAPACITY_USE$IO_SARL_CORE_SCHEDULES$CALLER();
      final Procedure1<Agent> _function = (Agent it) -> {
        Utils.setItemsVals(this.itemValues);
        Set<UUID> _keySet = this.actors.keySet();
        for (final UUID actor : _keySet) {
          {
            ActorPersonality actorPerson = this.actors.get(actor);
            String _role = actorPerson.getRole();
            boolean _equals = Objects.equal(_role, "Trader");
            if (_equals) {
              Lifecycle _$CAPACITY_USE$IO_SARL_CORE_LIFECYCLE$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_LIFECYCLE$CALLER();
              _$CAPACITY_USE$IO_SARL_CORE_LIFECYCLE$CALLER.spawn(Trader.class, spaceId, actor, actorPerson);
            } else {
              Lifecycle _$CAPACITY_USE$IO_SARL_CORE_LIFECYCLE$CALLER_1 = this.$CAPACITY_USE$IO_SARL_CORE_LIFECYCLE$CALLER();
              _$CAPACITY_USE$IO_SARL_CORE_LIFECYCLE$CALLER_1.spawn(
                Buyer.class, spaceId, actor, actorPerson);
            }
          }
        }
        Lifecycle _$CAPACITY_USE$IO_SARL_CORE_LIFECYCLE$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_LIFECYCLE$CALLER();
        _$CAPACITY_USE$IO_SARL_CORE_LIFECYCLE$CALLER.spawn(Controller.class, spaceId, "Controller", Integer.valueOf(1));
      };
      _$CAPACITY_USE$IO_SARL_CORE_SCHEDULES$CALLER_1.in(100, _function);
      Schedules _$CAPACITY_USE$IO_SARL_CORE_SCHEDULES$CALLER_2 = this.$CAPACITY_USE$IO_SARL_CORE_SCHEDULES$CALLER();
      final Procedure1<Agent> _function_1 = (Agent it) -> {
        Logging _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER_2 = this.$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER();
        _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER_2.info("Calculating Utilities");
        this.calculateAllUtils();
      };
      _xblockexpression = _$CAPACITY_USE$IO_SARL_CORE_SCHEDULES$CALLER_2.in(2000, _function_1);
    }
    return _xblockexpression;
  }
  
  /**
   * First computation
   */
  protected void calculateAllUtils() {
    synchronized (this.lockObj) {
      Logging _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER();
      int _size = this.gamePairs.keySet().size();
      _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER.info(("Game Pairs: " + Integer.valueOf(_size)));
      for (final UUID trader : this.traders) {
        final Consumer<UUID> _function = (UUID it) -> {
          Utils.calcUtils(trader, this.actors.get(trader).getItems(), it, this.actors.get(it).getItems());
        };
        this.buyers.forEach(_function);
      }
      Set<UUID> _keySet = this.actors.keySet();
      for (final UUID actor : _keySet) {
        DefaultContextInteractions _$CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER();
        Pair<String, Object> _mappedTo = Pair.<String, Object>of("id", actor);
        List<String> _items = this.actors.get(actor).getItems();
        Pair<String, Object> _mappedTo_1 = Pair.<String, Object>of("items", _items);
        LinkedHashMap<UUID, LinkedHashMap<UUID, LinkedHashMap<String, Object>>> _utilsForActor = Utils.getUtilsForActor(actor);
        Pair<String, Object> _mappedTo_2 = Pair.<String, Object>of("utils", _utilsForActor);
        LinkedHashMap<String, Object> _newLinkedHashMap = CollectionLiterals.<String, Object>newLinkedHashMap(_mappedTo, _mappedTo_1, _mappedTo_2);
        TransportGoods _transportGoods = new TransportGoods(_newLinkedHashMap);
        _$CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER.emit(_transportGoods);
      }
      Logging _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER_1 = this.$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER();
      _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER_1.info("start game");
      DefaultContextInteractions _$CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER_1 = this.$CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER();
      LinkedHashMap<UUID, String> _get = this.gamePairs.get(Integer.valueOf(this.round)).get("Trader");
      LinkedHashMap<UUID, String> _get_1 = this.gamePairs.get(Integer.valueOf(this.round)).get("Buyer");
      StartGame _startGame = new StartGame(_get, "Controller", _get_1, 
        this.actors);
      _$CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER_1.emit(_startGame);
    }
  }
  
  protected AgentTask calculateChangedUtils(final ActorPersonality trader, final ActorPersonality buyer) {
    AgentTask _xblockexpression = null;
    {
      Logging _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER();
      _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER.info("Calculating new Utils");
      AgentTask _xsynchronizedexpression = null;
      synchronized (this.lockObj) {
        AgentTask _xblockexpression_1 = null;
        {
          Utils.clean();
          this.actors.put(trader.getTradingId(), trader);
          this.actors.put(buyer.getTradingId(), buyer);
          for (final UUID traderPerson : this.traders) {
            final Consumer<UUID> _function = (UUID it) -> {
              Utils.calcUtils(traderPerson, this.actors.get(traderPerson).getItems(), it, this.actors.get(it).getItems());
            };
            this.buyers.forEach(_function);
          }
          Set<UUID> _keySet = this.actors.keySet();
          for (final UUID actor : _keySet) {
            DefaultContextInteractions _$CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER();
            Pair<String, Object> _mappedTo = Pair.<String, Object>of("id", actor);
            List<String> _items = this.actors.get(actor).getItems();
            Pair<String, Object> _mappedTo_1 = Pair.<String, Object>of("items", _items);
            LinkedHashMap<UUID, LinkedHashMap<UUID, LinkedHashMap<String, Object>>> _utilsForActor = Utils.getUtilsForActor(actor);
            Pair<String, Object> _mappedTo_2 = Pair.<String, Object>of("utils", _utilsForActor);
            LinkedHashMap<String, Object> _newLinkedHashMap = CollectionLiterals.<String, Object>newLinkedHashMap(_mappedTo, _mappedTo_1, _mappedTo_2);
            TransportGoods _transportGoods = new TransportGoods(_newLinkedHashMap);
            _$CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER.emit(_transportGoods);
          }
          Logging _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER_1 = this.$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER();
          _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER_1.info("finished calculating Utilities");
          Schedules _$CAPACITY_USE$IO_SARL_CORE_SCHEDULES$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_SCHEDULES$CALLER();
          final Procedure1<Agent> _function_1 = (Agent it) -> {
            int _size = this.gamePairs.keySet().size();
            if ((this.round <= _size)) {
              Logging _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER_2 = this.$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER();
              _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER_2.info(("\n\nRound: " + Integer.valueOf(this.round)));
              DefaultContextInteractions _$CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER_1 = this.$CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER();
              LinkedHashMap<UUID, String> _get = this.gamePairs.get(Integer.valueOf(this.round)).get("Trader");
              LinkedHashMap<UUID, String> _get_1 = this.gamePairs.get(Integer.valueOf(this.round)).get("Buyer");
              StartGame _startGame = new StartGame(_get, "Controller", _get_1);
              _$CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER_1.emit(_startGame);
            } else {
              Schedules _$CAPACITY_USE$IO_SARL_CORE_SCHEDULES$CALLER_1 = this.$CAPACITY_USE$IO_SARL_CORE_SCHEDULES$CALLER();
              final Procedure1<Agent> _function_2 = (Agent it_1) -> {
                Logging _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER_3 = this.$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER();
                _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER_3.info("dying");
                DefaultContextInteractions _$CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER_2 = this.$CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER();
                Die _die = new Die();
                _$CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER_2.emit(_die);
                this.csvWrite();
                Schedules _$CAPACITY_USE$IO_SARL_CORE_SCHEDULES$CALLER_2 = this.$CAPACITY_USE$IO_SARL_CORE_SCHEDULES$CALLER();
                final Procedure1<Agent> _function_3 = (Agent it_2) -> {
                  Lifecycle _$CAPACITY_USE$IO_SARL_CORE_LIFECYCLE$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_LIFECYCLE$CALLER();
                  _$CAPACITY_USE$IO_SARL_CORE_LIFECYCLE$CALLER.killMe();
                };
                _$CAPACITY_USE$IO_SARL_CORE_SCHEDULES$CALLER_2.in(800, _function_3);
              };
              _$CAPACITY_USE$IO_SARL_CORE_SCHEDULES$CALLER_1.in(200, _function_2);
            }
          };
          _xblockexpression_1 = _$CAPACITY_USE$IO_SARL_CORE_SCHEDULES$CALLER.in(2000, _function_1);
        }
        _xsynchronizedexpression = _xblockexpression_1;
      }
      _xblockexpression = _xsynchronizedexpression;
    }
    return _xblockexpression;
  }
  
  protected ActorPersonality rebalanceItems(final ActorPersonality actor, final List<String> itemsRemove, final List<String> itemsAdd) {
    synchronized (this.lockObj) {
      List<String> actorItems = actor.getItems();
      Logging _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER = this.$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER();
      String _name = actor.getName();
      _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER.info(((_name + " items before Trade: ") + actorItems));
      for (final String remove : itemsRemove) {
        final Predicate<String> _function = (String it) -> {
          return Objects.equal(it, remove);
        };
        actorItems.removeIf(_function);
      }
      if ((itemsAdd != null)) {
        actorItems.addAll(itemsAdd);
      }
      Logging _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER_1 = this.$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER();
      String _name_1 = actor.getName();
      _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER_1.info(((_name_1 + " items after Trade: ") + actorItems));
      return actor;
    }
  }
  
  /**
   * Configuration before game start
   */
  private Integer num = Integer.valueOf(1);
  
  protected void buildPairs() {
    final Consumer<UUID> _function = (UUID it) -> {
      UUID trader = it;
      final Consumer<UUID> _function_1 = (UUID it_1) -> {
        UUID buyer = it_1;
        ActorPersonality _get = this.actors.get(trader);
        ActorPersonality traderPerson = _get;
        ActorPersonality _get_1 = this.actors.get(buyer);
        ActorPersonality buyerPerson = _get_1;
        String _name = buyerPerson.getName();
        Pair<UUID, String> _mappedTo = Pair.<UUID, String>of(buyer, _name);
        LinkedHashMap<UUID, String> _newLinkedHashMap = CollectionLiterals.<UUID, String>newLinkedHashMap(_mappedTo);
        Pair<String, LinkedHashMap<UUID, String>> _mappedTo_1 = Pair.<String, LinkedHashMap<UUID, String>>of("Buyer", _newLinkedHashMap);
        String _name_1 = traderPerson.getName();
        Pair<UUID, String> _mappedTo_2 = Pair.<UUID, String>of(trader, _name_1);
        LinkedHashMap<UUID, String> _newLinkedHashMap_1 = CollectionLiterals.<UUID, String>newLinkedHashMap(_mappedTo_2);
        Pair<String, LinkedHashMap<UUID, String>> _mappedTo_3 = Pair.<String, LinkedHashMap<UUID, String>>of("Trader", _newLinkedHashMap_1);
        this.gamePairs.put(this.num, CollectionLiterals.<String, LinkedHashMap<UUID, String>>newLinkedHashMap(_mappedTo_1, _mappedTo_3));
        this.num = Integer.valueOf((((this.num) == null ? 0 : (this.num).intValue()) + 1));
      };
      this.buyers.forEach(_function_1);
    };
    this.traders.forEach(_function);
  }
  
  protected void buildGameSetupFromConfig() {
    Set<String> _keySet = this.gameConfig.keySet();
    for (final String conf : _keySet) {
      {
        Object _get = this.gameConfig.get(conf);
        LinkedHashMap<String, Object> confObj = ((LinkedHashMap<String, Object>) _get);
        Object _get_1 = confObj.get(
          "playerItemsValueMap");
        LinkedHashMap<String, Double> itemValsMap = ((LinkedHashMap<String, Double>) _get_1);
        Object _get_2 = confObj.get("itemList");
        List<String> items = ((List<String>) _get_2);
        Object _get_3 = confObj.get("name");
        String name = (_get_3 == null ? null : _get_3.toString());
        Object _get_4 = confObj.get("traderId");
        String idString = (_get_4 == null ? null : _get_4.toString());
        UUID id = UUID.fromString(idString);
        Object _get_5 = confObj.get("role");
        String role = (_get_5 == null ? null : _get_5.toString());
        Object _get_6 = confObj.get("money");
        Double money = ((Double) _get_6);
        Object _get_7 = confObj.get("tolerance");
        Double tolerance = ((Double) _get_7);
        Object _get_8 = confObj.get("trustScore");
        Double trustScore = ((Double) _get_8);
        Object _get_9 = confObj.get("actor");
        String actorType = (_get_9 == null ? null : _get_9.toString());
        Object _get_10 = confObj.get("itemsValShift");
        Double shift = ((Double) _get_10);
        Object _get_11 = confObj.get("ownItemsValShift");
        Double ownShift = ((Double) _get_11);
        if (this.randomVals) {
          itemValsMap = this.changeItemVals(itemValsMap, items, Double.valueOf(0.0), Double.valueOf(0.0));
        }
        ActorPersonality actor = this.getActorPersonality(actorType, id, name, items, money, trustScore, tolerance, role);
        boolean _equals = Objects.equal(role, "Trader");
        if (_equals) {
          this.traders.add(id);
        } else {
          this.buyers.add(id);
        }
        this.actors.put(id, actor);
        this.itemValues.put(id, itemValsMap);
      }
    }
  }
  
  protected LinkedHashMap<String, Double> changeItemVals(final LinkedHashMap<String, Double> itemVals, final List<String> items, final Double shift, final Double ownShift) {
    Set<String> _keySet = itemVals.keySet();
    for (final String item : _keySet) {
      {
        Double itemVal = itemVals.get(item);
        Double randomValueShift = Double.valueOf(ThreadLocalRandom.current().nextDouble(((this.min) == null ? 0 : (this.min).doubleValue()), ((this.max) == null ? 0 : (this.max).doubleValue())));
        boolean _contains = items.contains(item);
        if (_contains) {
          double _minus = DoubleExtensions.operator_minus(itemVal, randomValueShift);
          itemVals.put(item, Double.valueOf((_minus + ((ownShift) == null ? 0 : (ownShift).doubleValue()))));
        } else {
          double _plus = DoubleExtensions.operator_plus(itemVal, randomValueShift);
          itemVals.put(item, Double.valueOf((_plus + ((shift) == null ? 0 : (shift).doubleValue()))));
        }
      }
    }
    return itemVals;
  }
  
  @Pure
  protected ActorPersonality getActorPersonality(final String actor, final UUID id, final String name, final List<String> items, final Double money, final Double trustScore, final Double baseTolerance, final String role) {
    if (actor != null) {
      switch (actor) {
        case "Coop":
          return new CoopActor(id, name, items, money, trustScore, baseTolerance, role);
        case "TitForTat":
          return new TitForTatActor(id, name, items, money, trustScore, baseTolerance, role);
        case "Random":
          return new RandomActor(id, name, items, money, trustScore, baseTolerance, role);
        case "Deflecting":
          return new DeflectingActor(id, name, items, money, trustScore, baseTolerance, role);
      }
    }
    return null;
  }
  
  /**
   * After game ends
   */
  protected void csvWrite() {
    try {
      List<LinkedHashMap<String, Object>> tempList = CollectionLiterals.<LinkedHashMap<String, Object>>newArrayList();
      List<LinkedHashMap<String, String>> list = CollectionLiterals.<LinkedHashMap<String, String>>newArrayList();
      final Consumer<Integer> _function = (Integer it) -> {
        tempList.add(this.results.get(it));
      };
      this.results.keySet().forEach(_function);
      int round = 1;
      for (final LinkedHashMap<String, Object> listEntry : tempList) {
        {
          listEntry.put("itemsBuyer", listEntry.get("itemsBuyer"));
          listEntry.put("itemsTrader", listEntry.get("itemsTrader"));
          listEntry.remove("TraderActor");
          listEntry.remove("BuyerActor");
          listEntry.remove("offer");
          Object _get = listEntry.get("traderId");
          Object _get_1 = this.gameConfig.get((_get == null ? null : _get.toString()));
          LinkedHashMap confObj = ((LinkedHashMap) _get_1);
          Object _get_2 = listEntry.get("traderId");
          Pair<String, String> _mappedTo = Pair.<String, String>of("traderId", (_get_2 == null ? null : _get_2.toString()));
          Object _get_3 = listEntry.get("traderName");
          Pair<String, String> _mappedTo_1 = Pair.<String, String>of("traderName", (_get_3 == null ? null : _get_3.toString()));
          Object _get_4 = listEntry.get("utilTrader");
          Pair<String, String> _mappedTo_2 = Pair.<String, String>of("utilTrader", (_get_4 == null ? null : _get_4.toString()));
          Object _get_5 = listEntry.get("buyerId");
          Pair<String, String> _mappedTo_3 = Pair.<String, String>of("buyerId", (_get_5 == null ? null : _get_5.toString()));
          Object _get_6 = listEntry.get("buyerName");
          Pair<String, String> _mappedTo_4 = Pair.<String, String>of("buyerName", (_get_6 == null ? null : _get_6.toString()));
          Object _get_7 = listEntry.get("buyerUtil");
          Pair<String, String> _mappedTo_5 = Pair.<String, String>of("buyerUtil", (_get_7 == null ? null : _get_7.toString()));
          Object _get_8 = listEntry.get("itemsTrader");
          Pair<String, String> _mappedTo_6 = Pair.<String, String>of("itemsTrader", (_get_8 == null ? null : _get_8.toString()));
          Object _get_9 = listEntry.get("itemsBuyer");
          Pair<String, String> _mappedTo_7 = Pair.<String, String>of("itemsBuyer", (_get_9 == null ? null : _get_9.toString()));
          Object _get_10 = confObj.get("actor");
          Pair<String, String> _mappedTo_8 = Pair.<String, String>of("actorType", (_get_10 == null ? null : _get_10.toString()));
          Pair<String, String> _mappedTo_9 = Pair.<String, String>of("round", Integer.toString(round));
          list.add(
            CollectionLiterals.<String, String>newLinkedHashMap(_mappedTo, _mappedTo_1, _mappedTo_2, _mappedTo_3, _mappedTo_4, _mappedTo_5, _mappedTo_6, _mappedTo_7, _mappedTo_8, _mappedTo_9));
          round++;
        }
      }
      JavaUtils.csvWriter(list, this.writer);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
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
  
  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$Initialize(final Initialize occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$Initialize$0(occurrence));
  }
  
  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$TurnEnd(final TurnEnd occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$TurnEnd$1(occurrence));
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
    Spectator other = (Spectator) obj;
    if (other.randomVals != this.randomVals)
      return false;
    if (other.min == null) {
      if (this.min != null)
        return false;
    } else if (this.min == null)
      return false;
    if (other.min != null && Double.doubleToLongBits(other.min.doubleValue()) != Double.doubleToLongBits(this.min.doubleValue()))
      return false;
    if (other.max == null) {
      if (this.max != null)
        return false;
    } else if (this.max == null)
      return false;
    if (other.max != null && Double.doubleToLongBits(other.max.doubleValue()) != Double.doubleToLongBits(this.max.doubleValue()))
      return false;
    if (other.round != this.round)
      return false;
    if (other.num == null) {
      if (this.num != null)
        return false;
    } else if (this.num == null)
      return false;
    if (other.num != null && other.num.intValue() != this.num.intValue())
      return false;
    return super.equals(obj);
  }
  
  @Override
  @Pure
  @SyntheticMember
  public int hashCode() {
    int result = super.hashCode();
    final int prime = 31;
    result = prime * result + Boolean.hashCode(this.randomVals);
    result = prime * result + java.util.Objects.hashCode(this.min);
    result = prime * result + java.util.Objects.hashCode(this.max);
    result = prime * result + Integer.hashCode(this.round);
    result = prime * result + java.util.Objects.hashCode(this.num);
    return result;
  }
  
  @SyntheticMember
  public Spectator(final UUID parentID, final UUID agentID) {
    super(parentID, agentID);
  }
  
  @SyntheticMember
  @Inject
  @Deprecated
  public Spectator(final BuiltinCapacitiesProvider provider, final UUID parentID, final UUID agentID) {
    super(provider, parentID, agentID);
  }
  
  @SyntheticMember
  @Inject
  public Spectator(final UUID parentID, final UUID agentID, final DynamicSkillProvider skillProvider) {
    super(parentID, agentID, skillProvider);
  }
}
