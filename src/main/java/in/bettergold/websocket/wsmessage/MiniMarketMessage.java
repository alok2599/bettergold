package in.bettergold.websocket.wsmessage;

import java.math.BigDecimal;

import in.bettergold.model.Symbol;

public class MiniMarketMessage{
	Symbol symbol;
	BigDecimal price;
	BigDecimal volume;
	 
	
	public Symbol getSymbol() {
		return symbol;
	}
	public void setSymbol(Symbol symbol) {
		this.symbol = symbol;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public BigDecimal getVolume() {
		return volume;
	}
	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}
	
	@Override
    public boolean equals(Object o) {
 
        if (o == this) {
            return true;
        }
        if (o == null) {
            return false;
        }
 
        if (!(o instanceof MiniMarketMessage)) {
            return false;
        }
        
        return  this.getSymbol().equals( ((MiniMarketMessage) o).getSymbol() );
    }
	
	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + (( symbol== null) ? 0 : symbol.hashCode());
        return result;
    }
}