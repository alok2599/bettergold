package in.bettergold.controller.trade;

 import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import javax.jms.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.bettergold.engine.execution.OrderBookEntry;
import in.bettergold.model.Balance;
import in.bettergold.model.Order;
import in.bettergold.model.OrderSide;
import in.bettergold.model.OrderStatus;
import in.bettergold.model.Pair;
import in.bettergold.model.Ticker;
import in.bettergold.repository.BalanceRepository;
import in.bettergold.repository.OrderRepository;
import in.bettergold.repository.PairRepository;

@RestController
@RequestMapping("/user/trade")
public class TradeController {

	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
    BalanceRepository balanceRepository;
	
	@Autowired
	PairRepository pairRepository;
	
	 

	@Autowired
	JmsTemplate jmsTemplate;
	
	@Autowired
	@Qualifier(value="toLobQueue")
	Queue toLobQueue;
	
	@PostMapping(path = "/new")
	public ResponseEntity<Order> createNewOrder(@RequestBody Order order) throws InsufficientBalanceException {

		long timeInMillis = System.currentTimeMillis();
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (principal instanceof UserDetails) {
			String username = ((UserDetails) principal).getUsername();
			order.setUsername(username);
		} else {
			String username = principal.toString();
			order.setUsername(username);
		}
		
		
		order.setOrderId(UUID.randomUUID().toString() + "-" + timeInMillis);
		order.setOrderTimestamp(new Date(timeInMillis));
		order.setLastModifiedTimestamp(new Date(timeInMillis));
		order.setStatus(OrderStatus.OPEN);
		order.setExecutedQuantity(new BigDecimal(0));
		
		
		OrderBookEntry orderLob = new OrderBookEntry();
		orderLob.setOrderId(order.getOrderId());
		orderLob.setOrderTimestamp(timeInMillis);
		orderLob.setLimitPrice(order.getLimitPrice());
		orderLob.setQuantity(order.getQuantity());
		
		orderLob.setSide(order.getSide());
	    orderLob.setSymbol(order.getSymbol());
	   // System.out.println(balanceRepository.findByUsername(order.getUsername()).getInr());
	    
	    if(validateAndPlaceOrder(order, orderLob)) {
	    	
	    	return new ResponseEntity<Order>(order, HttpStatus.OK);
	    }
	    else
	    	throw new InsufficientBalanceException("Not enough balance to place this order.");

		
	}

	@PostMapping("/cancel")
	public ResponseEntity<Order> cancelOrder(@RequestBody Order order) throws OrderNotFoundException {

		long timeInMillis = System.currentTimeMillis();
		Order orderToCancel = orderRepository.findByOrderId(order.getOrderId());
		
		if (orderToCancel != null) {
			if (orderToCancel.getStatus() == OrderStatus.OPEN
					|| orderToCancel.getStatus() == OrderStatus.PARTIALLY_FILLED) {

				orderToCancel.setLastModifiedTimestamp(new Date(timeInMillis));

				OrderBookEntry orderLob = new OrderBookEntry();

				orderLob.setCancelRequested(true);
				orderLob.setOrderId(orderToCancel.getOrderId());
				orderLob.setSymbol(orderToCancel.getSymbol());
				orderLob.setLimitPrice(orderToCancel.getLimitPrice());
				orderLob.setQuantity(orderToCancel.getQuantity());
				orderLob.setSide(orderToCancel.getSide());
				orderLob.setOrderTimestamp(timeInMillis);
				jmsTemplate.convertAndSend(toLobQueue, orderLob);

				/*int cancelledQuantity = 0;
				orderToCancel.setStatus(OrderStatus.CANCELLED);
            	orderRepository.save(orderToCancel);*/
				
            	//updateBalance(orderToCancel, cancelledQuantity);
				return new ResponseEntity<Order>(orderToCancel, HttpStatus.ACCEPTED);
			}
		 
	}
		
		throw new OrderNotFoundException("This order does not exist.");
		
	}
 
	private boolean validateAndPlaceOrder(Order order, OrderBookEntry orderLob) {
		
		
		    Balance balance = balanceRepository.findByUsername(order.getUsername());
		    Pair pair = pairRepository.findBySymbol(order.getSymbol());
		    
		    if(balance == null || pair == null)
		    	return false;
		    
		    BigDecimal orderValue;// = order.getLimitPrice().multiply(new BigDecimal(order.getQuantity()));
		    BigDecimal orderQuantity;// = new BigDecimal(order.getQuantity());
		    
		    Ticker quoteCurrency = null;
			Ticker baseCurrency = null;
			BigDecimal quoteCurrencyBalance = null;
			BigDecimal baseCurrencyBalance = null;
			
			if(order.getSide() == OrderSide.BUY) {
				orderValue = order.getLimitPrice().multiply(order.getQuantity());
		    	quoteCurrency = pair.getQuoteCurrency();
		    	
				//if(quoteCurrency == Ticker.INR)
		    		quoteCurrencyBalance = balance.getInr();
		    		if(quoteCurrencyBalance.compareTo(orderValue)>=0) {
		    			balance.setInr(balance.getInr().subtract(orderValue));
		    			balanceRepository.save(balance);
		    			orderRepository.save(order);
		    	    	jmsTemplate.convertAndSend(toLobQueue,orderLob);
		    			return true;
		    		}
		    		 
			}
			else {
				orderQuantity = order.getQuantity();
				baseCurrency = pair.getBaseCurrency();
				
				if(baseCurrency == Ticker.BTC) {
					baseCurrencyBalance = balance.getBtc();
					balance.setBtc(baseCurrencyBalance.subtract(orderQuantity));
				}
				else if(baseCurrency == Ticker.ETH) {
					baseCurrencyBalance = balance.getEth();
					balance.setEth(baseCurrencyBalance.subtract(orderQuantity));
				}
				
				else if(baseCurrency == Ticker.XRP) {
					baseCurrencyBalance = balance.getXrp();
					balance.setXrp(baseCurrencyBalance.subtract(orderQuantity));
				}
				
				if(baseCurrencyBalance.compareTo(orderQuantity)>=0) {
					balanceRepository.save(balance);
	    			orderRepository.save(order);
	    	    	jmsTemplate.convertAndSend(toLobQueue,orderLob);
	    	    	return true;
					
				}
				
				
			}
			
			return false;
			
			
		    
	} 
	
}
