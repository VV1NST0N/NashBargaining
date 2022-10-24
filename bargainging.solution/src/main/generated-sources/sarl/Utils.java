import io.sarl.lang.annotation.SarlElementType;
import io.sarl.lang.annotation.SarlSpecification;
import io.sarl.lang.annotation.SyntheticMember;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import java.util.function.Consumer;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.DoubleExtensions;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.Pair;
import org.eclipse.xtext.xbase.lib.Pure;
import org.paukov.combinatorics3.Generator;

@SarlSpecification("0.11")
@SarlElementType(10)
@SuppressWarnings("all")
public class Utils {
  @SarlSpecification("0.11")
  @SarlElementType(10)
  public static class CustomComparator<K extends Object, V extends Comparable> implements Comparator<K> {
    private Map<K, V> map;
    
    public CustomComparator(final Map<K, V> map) {
      this.map = map;
    }
    
    public int compare(final K o1, final K o2) {
      return this.map.get(o1).compareTo(this.map.get(o2));
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
  }
  
  /**
   * based on the nash product list we can find fair bargain solutions:  product of utility gains -> https://www.researchgate.net/publication/237532301_From_Bilateral_Barter_to_Money_Exchange_Nash%27s_Bargaining_Problem_Reconsidered
   */
  private static LinkedHashMap<UUID, LinkedHashMap<UUID, ArrayList<Double>>> nashProduct = CollectionLiterals.<UUID, LinkedHashMap<UUID, ArrayList<Double>>>newLinkedHashMap();
  
  private static LinkedHashMap<UUID, LinkedHashMap<UUID, LinkedHashMap<UUID, LinkedHashMap<String, Object>>>> singleUtilities = CollectionLiterals.<UUID, LinkedHashMap<UUID, LinkedHashMap<UUID, LinkedHashMap<String, Object>>>>newLinkedHashMap();
  
  private static LinkedHashMap<UUID, LinkedHashMap<String, Double>> itemValuesAllPlayers;
  
  public static void calcUtils(final UUID trader, final List<String> traderItems, final UUID buyer, final List<String> buyerItems) {
    LinkedHashMap<String, Double> itemValsTrader = Utils.itemValuesAllPlayers.get(trader);
    LinkedHashMap<String, Double> itemValsBuyer = Utils.itemValuesAllPlayers.get(buyer);
    List<String> itemsTrader = traderItems;
    List<String> itemsBuyer = buyerItems;
    ArrayList<ArrayList<String>> possibleCombTrader = CollectionLiterals.<ArrayList<String>>newArrayList();
    ArrayList<ArrayList<String>> possibleCombBuyer = CollectionLiterals.<ArrayList<String>>newArrayList();
    for (int i = 2; (i >= 1); i--) {
      final Consumer<List<String>> _function = (List<String> it) -> {
        possibleCombTrader.add(((ArrayList) it));
      };
      Generator.<String>combination(itemsTrader).simple(i).stream().forEach(_function);
    }
    for (int i = 2; (i >= 1); i--) {
      final Consumer<List<String>> _function = (List<String> it) -> {
        possibleCombBuyer.add(((ArrayList) it));
      };
      Generator.<String>combination(itemsBuyer).simple(i).stream().forEach(_function);
    }
    for (final ArrayList<String> itemTrader : possibleCombTrader) {
      for (final ArrayList<String> itemBuyer : possibleCombBuyer) {
        Utils.calcUtiltyForCombinations(itemBuyer, itemTrader, itemValsTrader, itemValsBuyer, trader, buyer);
      }
    }
    Set<UUID> _keySet = Utils.nashProduct.keySet();
    for (final UUID traders : _keySet) {
      Set<UUID> _keySet_1 = Utils.nashProduct.get(traders).keySet();
      for (final UUID buyers : _keySet_1) {
        {
          Collections.<Double>sort(Utils.nashProduct.get(traders).get(buyers));
          Collections.reverse(Utils.nashProduct.get(traders).get(buyers));
        }
      }
    }
  }
  
  public static LinkedHashMap<String, Double> calcUtilForOffer(final Offer offer) {
    LinkedHashMap<String, Double> itemValsBuyer = Utils.itemValuesAllPlayers.get(offer.getClientId());
    LinkedHashMap<String, Double> itemValsTrader = Utils.itemValuesAllPlayers.get(offer.getTraderId());
    Double utilBuyer = Double.valueOf(0.0);
    Double utilTrader = Double.valueOf(0.0);
    List<String> _offerItems = offer.getOfferItems();
    for (final String item : _offerItems) {
      {
        Double _get = itemValsTrader.get(item);
        double _plus = DoubleExtensions.operator_plus(utilTrader, _get);
        utilTrader = Double.valueOf(_plus);
        Double _get_1 = itemValsBuyer.get(item);
        double _minus = DoubleExtensions.operator_minus(utilBuyer, _get_1);
        utilBuyer = Double.valueOf(_minus);
      }
    }
    List<String> _traderItems = offer.getTraderItems();
    for (final String item_1 : _traderItems) {
      {
        Double _get = itemValsTrader.get(item_1);
        double _minus = DoubleExtensions.operator_minus(utilTrader, _get);
        utilTrader = Double.valueOf(_minus);
        Double _get_1 = itemValsBuyer.get(item_1);
        double _plus = DoubleExtensions.operator_plus(utilBuyer, _get_1);
        utilBuyer = Double.valueOf(_plus);
      }
    }
    Double _offerMoney = offer.getOfferMoney();
    double _plus = DoubleExtensions.operator_plus(utilTrader, _offerMoney);
    utilTrader = Double.valueOf(_plus);
    Double genUtil = Double.valueOf(DoubleExtensions.operator_multiply(utilTrader, utilBuyer));
    LinkedHashMap<String, Double> result = CollectionLiterals.<String, Double>newLinkedHashMap();
    result.put("utilTrader", utilTrader);
    result.put("utilBuyer", utilBuyer);
    result.put("genUtil", genUtil);
    return result;
  }
  
  private static LinkedHashMap<String, Object> calcUtiltyForCombinations(final ArrayList<String> buyerItems, final ArrayList<String> traderItems, final LinkedHashMap<String, Double> itemValsTrader, final LinkedHashMap<String, Double> itemValsBuyer, final UUID trader, final UUID buyer) {
    LinkedHashMap<String, Object> _xblockexpression = null;
    {
      Double utilBuyer = Double.valueOf(0.0);
      Double utilTrader = Double.valueOf(0.0);
      for (final String item : buyerItems) {
        {
          Double _get = itemValsTrader.get(item);
          double _plus = DoubleExtensions.operator_plus(utilTrader, _get);
          utilTrader = Double.valueOf(_plus);
          Double _get_1 = itemValsBuyer.get(item);
          double _minus = DoubleExtensions.operator_minus(utilBuyer, _get_1);
          utilBuyer = Double.valueOf(_minus);
        }
      }
      for (final String item_1 : traderItems) {
        {
          Double _get = itemValsTrader.get(item_1);
          double _minus = DoubleExtensions.operator_minus(utilTrader, _get);
          utilTrader = Double.valueOf(_minus);
          Double _get_1 = itemValsBuyer.get(item_1);
          double _plus = DoubleExtensions.operator_plus(utilBuyer, _get_1);
          utilBuyer = Double.valueOf(_plus);
        }
      }
      double genUtil = DoubleExtensions.operator_multiply(utilTrader, utilBuyer);
      LinkedHashMap<UUID, ArrayList<Double>> _get = Utils.nashProduct.get(trader);
      if ((_get == null)) {
        Utils.nashProduct.put(trader, CollectionLiterals.<UUID, ArrayList<Double>>newLinkedHashMap());
      }
      if ((genUtil > 0)) {
        ArrayList<Double> _get_1 = Utils.nashProduct.get(trader).get(buyer);
        if ((_get_1 != null)) {
          Utils.nashProduct.get(trader).get(buyer).add(Double.valueOf(genUtil));
        } else {
          Utils.nashProduct.get(trader).put(buyer, CollectionLiterals.<Double>newArrayList(Double.valueOf(genUtil)));
        }
      }
      LinkedHashMap<UUID, LinkedHashMap<UUID, LinkedHashMap<String, Object>>> _get_2 = Utils.singleUtilities.get(trader);
      if ((_get_2 == null)) {
        Utils.singleUtilities.put(trader, CollectionLiterals.<UUID, LinkedHashMap<UUID, LinkedHashMap<String, Object>>>newLinkedHashMap());
      }
      LinkedHashMap<UUID, LinkedHashMap<String, Object>> _get_3 = Utils.singleUtilities.get(trader).get(buyer);
      if ((_get_3 == null)) {
        Utils.singleUtilities.get(trader).put(buyer, CollectionLiterals.<UUID, LinkedHashMap<String, Object>>newLinkedHashMap());
      }
      if ((utilTrader.doubleValue() > 0)) {
        UUID id = UUID.randomUUID();
        Pair<String, Object> _mappedTo = Pair.<String, Object>of("utility", utilTrader);
        Pair<String, Object> _mappedTo_1 = Pair.<String, Object>of("tradeItems", traderItems);
        Pair<String, Object> _mappedTo_2 = Pair.<String, Object>of("offerItems", buyerItems);
        Utils.singleUtilities.get(trader).get(buyer).put(id, 
          CollectionLiterals.<String, Object>newLinkedHashMap(_mappedTo, _mappedTo_1, _mappedTo_2));
      }
      LinkedHashMap<UUID, LinkedHashMap<UUID, LinkedHashMap<String, Object>>> _get_4 = Utils.singleUtilities.get(buyer);
      if ((_get_4 == null)) {
        Utils.singleUtilities.put(buyer, CollectionLiterals.<UUID, LinkedHashMap<UUID, LinkedHashMap<String, Object>>>newLinkedHashMap());
      }
      LinkedHashMap<UUID, LinkedHashMap<String, Object>> _get_5 = Utils.singleUtilities.get(buyer).get(trader);
      if ((_get_5 == null)) {
        Utils.singleUtilities.get(buyer).put(trader, CollectionLiterals.<UUID, LinkedHashMap<String, Object>>newLinkedHashMap());
      }
      LinkedHashMap<String, Object> _xifexpression = null;
      if ((utilBuyer.doubleValue() > 0)) {
        LinkedHashMap<String, Object> _xblockexpression_1 = null;
        {
          UUID id_1 = UUID.randomUUID();
          Pair<String, Object> _mappedTo_3 = Pair.<String, Object>of("utility", utilBuyer);
          Pair<String, Object> _mappedTo_4 = Pair.<String, Object>of("tradeItems", traderItems);
          Pair<String, Object> _mappedTo_5 = Pair.<String, Object>of("offerItems", buyerItems);
          _xblockexpression_1 = Utils.singleUtilities.get(buyer).get(trader).put(id_1, 
            CollectionLiterals.<String, Object>newLinkedHashMap(_mappedTo_3, _mappedTo_4, _mappedTo_5));
        }
        _xifexpression = _xblockexpression_1;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  @Pure
  public static LinkedHashMap<UUID, LinkedHashMap<UUID, LinkedHashMap<String, Object>>> getUtilsForActor(final UUID actorId) {
    return Utils.singleUtilities.get(actorId);
  }
  
  @Pure
  public static TreeMap<UUID, Double> getSortedUtilsForOpponent(final LinkedHashMap<UUID, LinkedHashMap<String, Object>> map) {
    TreeMap<UUID, Double> sortedUUIDsByValue = new TreeMap<UUID, Double>();
    final Consumer<UUID> _function = (UUID it) -> {
      Object _get = map.get(it).get("utility");
      sortedUUIDsByValue.put(it, ((Double) _get));
    };
    map.keySet().forEach(_function);
    return Utils.sortByValue(sortedUUIDsByValue);
  }
  
  public static TreeMap<UUID, Double> sortByValue(final TreeMap<UUID, Double> map) {
    Utils.CustomComparator comparator = new Utils.CustomComparator<UUID, Double>(map);
    TreeMap sortedMap = new TreeMap<Object, Object>(comparator);
    sortedMap.putAll(map);
    return sortedMap;
  }
  
  @Pure
  public static Double getMaxUtil(final Offer offer) {
    ArrayList<Double> list = Utils.nashProduct.get(offer.getTraderId()).get(offer.getClientId());
    ArrayList<Double> _get = Utils.nashProduct.get(offer.getTraderId()).get(offer.getClientId());
    if ((_get == null)) {
      list = CollectionLiterals.<Double>newArrayList(Double.valueOf(0.0));
    }
    return list.get(0);
  }
  
  public static LinkedHashMap<UUID, LinkedHashMap<UUID, ArrayList<Double>>> clean() {
    LinkedHashMap<UUID, LinkedHashMap<UUID, ArrayList<Double>>> _xblockexpression = null;
    {
      Utils.singleUtilities = CollectionLiterals.<UUID, LinkedHashMap<UUID, LinkedHashMap<UUID, LinkedHashMap<String, Object>>>>newLinkedHashMap();
      _xblockexpression = Utils.nashProduct = CollectionLiterals.<UUID, LinkedHashMap<UUID, ArrayList<Double>>>newLinkedHashMap();
    }
    return _xblockexpression;
  }
  
  public static void setItemsVals(final LinkedHashMap<UUID, LinkedHashMap<String, Double>> itemValues) {
    Utils.itemValuesAllPlayers = itemValues;
  }
  
  public static void printUtils() {
    Set<UUID> _keySet = Utils.singleUtilities.keySet();
    for (final UUID actor : _keySet) {
      {
        LinkedHashMap<UUID, LinkedHashMap<UUID, LinkedHashMap<String, Object>>> utils = Utils.singleUtilities.get(actor);
        InputOutput.<String>println(("Actor:" + actor));
        final Consumer<UUID> _function = (UUID it) -> {
          InputOutput.<LinkedHashMap<UUID, LinkedHashMap<String, Object>>>println(utils.get(it));
        };
        utils.keySet().forEach(_function);
      }
    }
  }
  
  @SyntheticMember
  public Utils() {
    super();
  }
}
