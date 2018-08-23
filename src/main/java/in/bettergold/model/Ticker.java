package in.bettergold.model;

public enum Ticker {

	BTC("BITCOIN"),
	ETH("ETHEREUM"),
	XRP("RIPPLE"),
	INR("INDIAN RUPEE");
	
	String description;
	Ticker(String s) {
		description = s;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
