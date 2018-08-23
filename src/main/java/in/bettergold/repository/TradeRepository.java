package in.bettergold.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.bettergold.model.Trade;

public interface TradeRepository extends JpaRepository<Trade, Long> {
	  // public Order findByOrderId(String orderId);
	   public Trade findByBuyOrderId(String orderId);
	   public Trade findBySellOrderId(String orderId);
	   public Trade findByTradeId(String orderId);
	   @Query(value = "select  * from (select symbol,  max(timestamp) as maxtimestamp from trade group by symbol) as t2 inner join trade as t1 on t1.symbol= t2.symbol and t1.timestamp= t2.maxtimestamp", nativeQuery = true)
	   public List<Trade> findLastTradeOfAllSymbolsWithVolume();
	   
	   
	
	}