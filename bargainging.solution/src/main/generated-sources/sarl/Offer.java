import io.sarl.lang.annotation.SarlElementType;
import io.sarl.lang.annotation.SarlSpecification;
import io.sarl.lang.annotation.SyntheticMember;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.eclipse.xtext.xbase.lib.Pure;

@SarlSpecification("0.11")
@SarlElementType(10)
@SuppressWarnings("all")
public class Offer {
  private UUID offerId;
  
  private UUID clientId;
  
  private UUID traderId;
  
  private String clientName;
  
  private String traderName;
  
  private List<String> offerItems;
  
  private List<String> traderItems;
  
  private Double offerMoney;
  
  private Integer numBargains;
  
  private Integer numBargainRounds;
  
  private Double traderTrus;
  
  private Double buyerTrus;
  
  public Offer(final UUID offerId, final UUID traderId, final UUID clientId, final List<String> offerItems, final List<String> traderItems, final Double offerMoney, final String traderName, final String clientName) {
    this.offerId = offerId;
    this.traderId = traderId;
    this.clientId = clientId;
    this.traderName = traderName;
    this.clientName = clientName;
    this.offerItems = offerItems;
    this.traderItems = traderItems;
    this.offerMoney = offerMoney;
    this.numBargains = Integer.valueOf(0);
    this.numBargainRounds = Integer.valueOf(0);
  }
  
  public void setTraderTrustScore(final Double traderScore) {
    this.traderTrus = traderScore;
  }
  
  public void setBuyerTrustScore(final Double buyerScore) {
    this.buyerTrus = buyerScore;
  }
  
  @Pure
  protected Double getBuyerTrustScore() {
    return this.buyerTrus;
  }
  
  @Pure
  protected Double getTraderTrustScore() {
    return this.traderTrus;
  }
  
  @Pure
  public UUID getClientId() {
    return this.clientId;
  }
  
  @Pure
  public UUID getTraderId() {
    return this.traderId;
  }
  
  @Pure
  public String getClientName() {
    return this.clientName;
  }
  
  @Pure
  public String getTraderName() {
    return this.traderName;
  }
  
  @Pure
  public List<String> getOfferItems() {
    return this.offerItems;
  }
  
  @Pure
  public List<String> getTraderItems() {
    return this.traderItems;
  }
  
  @Pure
  public Double getOfferMoney() {
    return this.offerMoney;
  }
  
  @Pure
  public UUID getOfferId() {
    return this.offerId;
  }
  
  @Pure
  public Integer getNumBargains() {
    return this.numBargains;
  }
  
  @Pure
  public Integer getNumBargainRounds() {
    return this.numBargainRounds;
  }
  
  public Integer increaseNumBargains() {
    return this.numBargains++;
  }
  
  public Integer increaseNumBargainRounds() {
    return this.numBargainRounds++;
  }
  
  public void setOfferId(final UUID uuid) {
    this.offerId = uuid;
  }
  
  public void setTraderName(final String name) {
    this.traderName = name;
  }
  
  public void setClientName(final String name) {
    this.clientName = name;
  }
  
  public void setTraderId(final UUID id) {
    this.traderId = id;
  }
  
  public void setClientId(final UUID id) {
    this.clientId = id;
  }
  
  public void setOfferItems(final List<String> offerItems) {
    this.offerItems = offerItems;
  }
  
  public void setTraderItems(final List<String> tradeItems) {
    this.traderItems = tradeItems;
  }
  
  public void setOfferMoney(final Double money) {
    this.offerMoney = money;
  }
  
  @Pure
  public String toString() {
    return ((("Trader Items: " + this.traderItems) + "\n Buyer Items: ") + this.offerItems);
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
    Offer other = (Offer) obj;
    if (!Objects.equals(this.offerId, other.offerId))
      return false;
    if (!Objects.equals(this.clientId, other.clientId))
      return false;
    if (!Objects.equals(this.traderId, other.traderId))
      return false;
    if (!Objects.equals(this.clientName, other.clientName))
      return false;
    if (!Objects.equals(this.traderName, other.traderName))
      return false;
    if (other.offerMoney == null) {
      if (this.offerMoney != null)
        return false;
    } else if (this.offerMoney == null)
      return false;
    if (other.offerMoney != null && Double.doubleToLongBits(other.offerMoney.doubleValue()) != Double.doubleToLongBits(this.offerMoney.doubleValue()))
      return false;
    if (other.numBargains == null) {
      if (this.numBargains != null)
        return false;
    } else if (this.numBargains == null)
      return false;
    if (other.numBargains != null && other.numBargains.intValue() != this.numBargains.intValue())
      return false;
    if (other.numBargainRounds == null) {
      if (this.numBargainRounds != null)
        return false;
    } else if (this.numBargainRounds == null)
      return false;
    if (other.numBargainRounds != null && other.numBargainRounds.intValue() != this.numBargainRounds.intValue())
      return false;
    if (other.traderTrus == null) {
      if (this.traderTrus != null)
        return false;
    } else if (this.traderTrus == null)
      return false;
    if (other.traderTrus != null && Double.doubleToLongBits(other.traderTrus.doubleValue()) != Double.doubleToLongBits(this.traderTrus.doubleValue()))
      return false;
    if (other.buyerTrus == null) {
      if (this.buyerTrus != null)
        return false;
    } else if (this.buyerTrus == null)
      return false;
    if (other.buyerTrus != null && Double.doubleToLongBits(other.buyerTrus.doubleValue()) != Double.doubleToLongBits(this.buyerTrus.doubleValue()))
      return false;
    return super.equals(obj);
  }
  
  @Override
  @Pure
  @SyntheticMember
  public int hashCode() {
    int result = super.hashCode();
    final int prime = 31;
    result = prime * result + Objects.hashCode(this.offerId);
    result = prime * result + Objects.hashCode(this.clientId);
    result = prime * result + Objects.hashCode(this.traderId);
    result = prime * result + Objects.hashCode(this.clientName);
    result = prime * result + Objects.hashCode(this.traderName);
    result = prime * result + Objects.hashCode(this.offerMoney);
    result = prime * result + Objects.hashCode(this.numBargains);
    result = prime * result + Objects.hashCode(this.numBargainRounds);
    result = prime * result + Objects.hashCode(this.traderTrus);
    result = prime * result + Objects.hashCode(this.buyerTrus);
    return result;
  }
}
