package in.bettergold.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import in.bettergold.model.Symbol;


@Entity
@Table(name = "PAIR")
public class Pair {
	@Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column(name = "PAIR", length = 6)
//    @NotNull
//    private String pair;

	@Column(name = "SYMBOL")
    @NotNull
    @Enumerated(EnumType.STRING)
    private Symbol symbol;
	
    @Column(name = "BASE_CURRENCY")
    @NotNull
    @Enumerated(EnumType.STRING)
    private Ticker baseCurrency;

    @Column(name = "QUOTE_CURRENCY")
    @NotNull
    @Enumerated(EnumType.STRING)
    private Ticker quoteCurrency;

	public Symbol getSymbol() {
		return symbol;
	}

	public void setSymbol(Symbol symbol) {
		this.symbol = symbol;
	}

	public Ticker getBaseCurrency() {
		return baseCurrency;
	}

	public void setBaseCurrency(Ticker baseCurrency) {
		this.baseCurrency = baseCurrency;
	}

	public Ticker getQuoteCurrency() {
		return quoteCurrency;
	}

	public void setQuoteCurrency(Ticker quoteCurrency) {
		this.quoteCurrency = quoteCurrency;
	}
    
	 
    
}
