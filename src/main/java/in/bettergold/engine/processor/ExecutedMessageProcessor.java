package in.bettergold.engine.processor;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.jms.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import in.bettergold.engine.execution.ExecutedOrderMessage;
import in.bettergold.model.Balance;
import in.bettergold.model.Order;
import in.bettergold.model.OrderSide;
import in.bettergold.model.OrderStatus;
import in.bettergold.model.Pair;
import in.bettergold.model.Symbol;
import in.bettergold.model.Ticker;
import in.bettergold.model.Trade;
import in.bettergold.repository.BalanceRepository;
import in.bettergold.repository.OrderRepository;
import in.bettergold.repository.PairRepository;
import in.bettergold.repository.TradeRepository;
import in.bettergold.repository.UserRepository;
import in.bettergold.websocket.wsmessage.BalanceWSMessage;
import in.bettergold.websocket.wsmessage.GenericTradeMessageWS;
import in.bettergold.websocket.wsmessage.MiniMarketMessage;
import in.bettergold.websocket.wsmessage.MiniMarketWSMessage;
import in.bettergold.websocket.wsmessage.PendingOrder;
import in.bettergold.websocket.wsmessage.PendingOrderWSMessage;
import in.bettergold.websocket.wsmessage.UserBalanceStatusMessageWS;
import in.bettergold.websocket.wsmessage.UserOrderStatusMessageWS;

@Component
public class ExecutedMessageProcessor {

	@Autowired
	TradeRepository tradeRepository;
	
	@Autowired
	UserRepository userRepository;

	@Autowired
	BalanceRepository balanceRepository;

	@Autowired
	PairRepository pairRepository;

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	JmsTemplate jmsTemplate;
	
	@Autowired
	@Qualifier(value="fromLobWSTradeQueue")
	Queue fromLobWSTradeQueue;
	
	@Autowired
	@Qualifier(value="fromLobWSMiniMarketTradeQueue")
	Queue fromLobWSMiniMarketTradeQueue;
	
	@Autowired
	@Qualifier(value="userWSOrderStatusQueue")
	Queue userWSOrderStatusQueue;
	
	@Autowired
	@Qualifier(value="userWSBalanceStatusQueue")
	Queue userWSBalanceStatusQueue;
	
	@JmsListener(destination = "fromLob.executed.queue")
	private void orderConsumer(ExecutedOrderMessage executedOrderMsg) {

		Order buyerExecutedOrder = orderRepository.findByOrderId(executedOrderMsg.getBuyBookOrderId());
		Order sellerExecutedOrder = orderRepository.findByOrderId(executedOrderMsg.getSellBookOrderId());
		
		Trade trade = new Trade();

		trade.setTradeId(executedOrderMsg.getTradeId());
		trade.setBuyOrderId(executedOrderMsg.getBuyBookOrderId());
		trade.setSellOrderId(executedOrderMsg.getSellBookOrderId());
		trade.setExecutionPrice(executedOrderMsg.getExecutionPrice());
		trade.setQuantity(executedOrderMsg.getExecutionQuantity());
		trade.setTradeTimestamp(new Date(executedOrderMsg.getExecutionTimestamp()));
		trade.setSymbol(buyerExecutedOrder.getSymbol());
		tradeRepository.save(trade);
		List<Trade> ltp = tradeRepository.findLastTradeOfAllSymbolsWithVolume();
		MiniMarketWSMessage miniMarketWSMessage = new MiniMarketWSMessage();
		MiniMarketMessage miniMarketMessage;

		for(Trade t:ltp) {
			miniMarketMessage = new MiniMarketMessage();
			miniMarketMessage.setPrice(t.getExecutionPrice());
			miniMarketMessage.setSymbol(t.getSymbol());
			miniMarketMessage.setVolume(t.getQuantity());
			System.out.println("miniMarketTradeData: "+miniMarketMessage.getSymbol());
			miniMarketWSMessage.add(miniMarketMessage);
			 	
		}
		 
		jmsTemplate.convertAndSend(fromLobWSMiniMarketTradeQueue, miniMarketWSMessage);

		//update executed quantity in the Order table and update balance for the buyer
		
		buyerExecutedOrder.setExecutedQuantity(buyerExecutedOrder.getExecutedQuantity().add(executedOrderMsg.getExecutionQuantity()));
	 	 
		if(buyerExecutedOrder.getExecutedQuantity().compareTo(buyerExecutedOrder.getQuantity()) == 0){
			buyerExecutedOrder.setStatus(OrderStatus.FILLED);
		}
		else
			buyerExecutedOrder.setStatus(OrderStatus.PARTIALLY_FILLED);
	
		orderRepository.save(buyerExecutedOrder);
		updateBalance(buyerExecutedOrder, executedOrderMsg.getExecutionQuantity(), executedOrderMsg.getExecutionPrice());

		//update executed quantity in the Order table and update balance for the seller
		
		sellerExecutedOrder.setExecutedQuantity(sellerExecutedOrder.getExecutedQuantity().add(executedOrderMsg.getExecutionQuantity()));
		if(sellerExecutedOrder.getExecutedQuantity().compareTo(sellerExecutedOrder.getQuantity()) == 0){
			sellerExecutedOrder.setStatus(OrderStatus.FILLED);
		}
		else
			sellerExecutedOrder.setStatus(OrderStatus.PARTIALLY_FILLED);
		
		orderRepository.save(sellerExecutedOrder);
		updateBalance(sellerExecutedOrder, executedOrderMsg.getExecutionQuantity(), executedOrderMsg.getExecutionPrice());
		
		GenericTradeMessageWS tradeMessageWS = new GenericTradeMessageWS();
		tradeMessageWS.setSymbol(sellerExecutedOrder.getSymbol());
		tradeMessageWS.setPrice(executedOrderMsg.getExecutionPrice());
		tradeMessageWS.setQuantity(executedOrderMsg.getExecutionQuantity());
		tradeMessageWS.setTime(new SimpleDateFormat("HH:mm:ss:SSS").format(new Date(executedOrderMsg.getExecutionTimestamp())));
		//send trade message to generic ws channel
		jmsTemplate.convertAndSend(fromLobWSTradeQueue, tradeMessageWS);
		
		//executedOrder = orderRepository.findByOrderId(executedOrderMsg.getBuyBookOrderId());
		 
		
		String buyerChannel = userRepository.findByUsername(buyerExecutedOrder.getUsername()).getUserChannel(); 
		System.out.println("buyer channel:"+ buyerChannel);
		jmsTemplate.convertAndSend(userWSOrderStatusQueue, new UserOrderStatusMessageWS(new PendingOrderWSMessage(getPendingOrders(buyerExecutedOrder.getUsername())), buyerChannel));
		
		System.out.println(getPendingOrders(buyerExecutedOrder.getUsername()));
		String sellerChannel = userRepository.findByUsername(sellerExecutedOrder.getUsername()).getUserChannel();
		System.out.println("seller channel:"+ sellerChannel);
		jmsTemplate.convertAndSend(userWSOrderStatusQueue, new UserOrderStatusMessageWS(new PendingOrderWSMessage(getPendingOrders(sellerExecutedOrder.getUsername())), sellerChannel));

		Balance buyerNewBalance =  balanceRepository.findByUsername(buyerExecutedOrder.getUsername());
		BalanceWSMessage balanceWSMessage = new BalanceWSMessage();
		balanceWSMessage.setBtc(buyerNewBalance.getBtc());
		balanceWSMessage.setEth(buyerNewBalance.getEth());
		balanceWSMessage.setXrp(buyerNewBalance.getXrp());
		balanceWSMessage.setInr(buyerNewBalance.getInr());
		
		jmsTemplate.convertAndSend(userWSBalanceStatusQueue, new UserBalanceStatusMessageWS(balanceWSMessage, buyerChannel));
		
		Balance sellerNewBalance =  balanceRepository.findByUsername(sellerExecutedOrder.getUsername());
		balanceWSMessage = new BalanceWSMessage();
		balanceWSMessage.setBtc(sellerNewBalance.getBtc());
		balanceWSMessage.setEth(sellerNewBalance.getEth());
		balanceWSMessage.setXrp(sellerNewBalance.getXrp());
		balanceWSMessage.setInr(sellerNewBalance.getInr());
		jmsTemplate.convertAndSend(userWSBalanceStatusQueue, new UserBalanceStatusMessageWS(balanceWSMessage, sellerChannel));
		
		
	}

	 

	 

	private List<PendingOrder> getPendingOrders(String username) {
		List<Order> buyerPendingOrders = (List<Order>) orderRepository.findAllPendingOrdersByUsername(username);
		List<PendingOrder> pendingOrders = new ArrayList<PendingOrder>();
		PendingOrder pOrder;
		for(Order o : buyerPendingOrders) {
			pOrder = new PendingOrder();
			pOrder.setSymbol(o.getSymbol());
			pOrder.setSide(o.getSide());
			pOrder.setOrderQuantity(o.getQuantity());
			pOrder.setExecQuantity(o.getExecutedQuantity());
			pOrder.setStatus(o.getStatus());
			pOrder.setTimestamp(o.getOrderTimestamp());
			pOrder.setOrderId(o.getOrderId());
			
			
			pendingOrders.add(pOrder);
		}
		
		return pendingOrders;
		
		
	}





	private Balance updateBalance(final Order executedOrder, final BigDecimal executedQuantity,
			final BigDecimal executionPrice) {

		Balance balance = balanceRepository.findByUsername(executedOrder.getUsername());
		Pair pair = pairRepository.findBySymbol(executedOrder.getSymbol());

		if (balance == null || pair == null) {

		}

		// Ticker quoteCurrency = null;
		Ticker baseCurrency = null;
		BigDecimal quoteCurrencyBalance = null;
		BigDecimal baseCurrencyBalance = null;

		if (executedOrder.getSide() == OrderSide.BUY) {

			baseCurrency = pair.getBaseCurrency();

			if (baseCurrency == Ticker.BTC) {
				baseCurrencyBalance = balance.getBtc();
				balance.setBtc(baseCurrencyBalance.add(executedQuantity));
			} else if (baseCurrency == Ticker.ETH) {
				baseCurrencyBalance = balance.getEth();
				balance.setEth(baseCurrencyBalance.add(executedQuantity));
			}

			else if (baseCurrency == Ticker.XRP) {
				baseCurrencyBalance = balance.getXrp();
				balance.setXrp(baseCurrencyBalance.add(executedQuantity));
			}

			balanceRepository.save(balance);

		} else {
			// quoteCurrency = pair.getQuoteCurrency();
			quoteCurrencyBalance = balance.getInr();
			BigDecimal tradeValue = executionPrice.multiply(executedQuantity);
			balance.setInr(quoteCurrencyBalance.add(tradeValue));
			balanceRepository.save(balance);

		}
		
		return balance;

	}

}