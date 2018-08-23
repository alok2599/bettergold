package in.bettergold.engine;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import in.bettergold.engine.execution.InvalidOrderSideException;
import in.bettergold.engine.execution.LobImpl;
import in.bettergold.engine.execution.OrderBookEntry;
import in.bettergold.model.Pair;
import in.bettergold.model.Symbol;
import in.bettergold.repository.PairRepository;

@Component
public class OrderHandler implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//create LOBs for each symbol and keep them in a map
    //consume orders from MQ
	// assign orders to respective LOBs
	@Autowired
	PairRepository pairRepository;
	Map<Symbol,LobImpl> pairsMap;
	
	  
	ApplicationContext ctx;
	
	@Autowired
	public OrderHandler(PairRepository pr, ApplicationContext actx){
		pairRepository = pr;
		ctx = actx;
		pairsMap = new HashMap<Symbol, LobImpl>();
		create();
	}
	
	void create(){
		List<Pair> pairs = pairRepository.findAll();
		for(Pair p: pairs) {
			LobImpl l = ctx.getBean(LobImpl.class);
			l.setSymbol(p.getSymbol());
			pairsMap.put(p.getSymbol(), l);
		}
		
		
		
		
	}
	@JmsListener(destination = "toLob.queue")
	private void orderConsumer(OrderBookEntry order) {
		//System.out.println("Received New order: "+order.getTradingPair()+": "+order.getLimitPrice()+": "+order.getOrderId());
		try {
			pairsMap.get(order.getSymbol()).submit(order);
			
		} catch (InvalidOrderSideException e) {
			e.printStackTrace();
		}
			
	}
	
}
