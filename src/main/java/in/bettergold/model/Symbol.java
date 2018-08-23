package in.bettergold.model;

public enum Symbol {
	
	BTCINR("BTC/INR"),
	ETHINR("ETH/INR"),
	XRPINR("XRP/INR"),
	ETHBTC("ETH/BTC");
	
	String description;
	private Symbol(String s) {
		description = s;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
