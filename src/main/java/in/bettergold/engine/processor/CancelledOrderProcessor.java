package in.bettergold.engine.processor;

import java.math.BigDecimal;
import java.util.Date;

import javax.jms.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import in.bettergold.engine.OrderHandler;
import in.bettergold.engine.execution.CancelledOrderMessage;
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

@Component
public class CancelledOrderProcessor {

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	BalanceRepository balanceRepository;

	@Autowired
	PairRepository pairRepository;

	@JmsListener(destination = "fromLob.cancelled.queue")
	private void orderConsumer(CancelledOrderMessage cancelledOrderMsg) {
		 
		long timeInMillis = System.currentTimeMillis();

		Order orderToCancel = orderRepository.findByOrderId(cancelledOrderMsg.getOrderId());
		orderToCancel.setLastModifiedTimestamp(new Date(timeInMillis));
		orderToCancel.setStatus(OrderStatus.CANCELLED);
		orderRepository.save(orderToCancel);

		updateBalance(orderToCancel, cancelledOrderMsg.getCancelledQuantity());

	}

	private void updateBalance(Order orderToCancel, BigDecimal cancelledQuantity) {

		Balance balance = balanceRepository.findByUsername(orderToCancel.getUsername());
		Pair pair = pairRepository.findBySymbol(orderToCancel.getSymbol());

		if (balance == null || pair == null) {

		}

		// Ticker quoteCurrency = null;
		Ticker baseCurrency = null;
		BigDecimal quoteCurrencyBalance = null;
		BigDecimal baseCurrencyBalance = null;

		if (orderToCancel.getSide() == OrderSide.BUY) {
			// quoteCurrency = pair.getQuoteCurrency();
			quoteCurrencyBalance = balance.getInr();
			BigDecimal cancelledValue = orderToCancel.getLimitPrice().multiply(cancelledQuantity);
			balance.setInr(quoteCurrencyBalance.add(cancelledValue));
			balanceRepository.save(balance);

		} else {
			baseCurrency = pair.getBaseCurrency();

			if (baseCurrency == Ticker.BTC) {
				baseCurrencyBalance = balance.getBtc();
				balance.setBtc(baseCurrencyBalance.add(cancelledQuantity));
			} else if (baseCurrency == Ticker.ETH) {
				baseCurrencyBalance = balance.getEth();
				balance.setEth(baseCurrencyBalance.add(cancelledQuantity));
			}

			else if (baseCurrency == Ticker.XRP) {
				baseCurrencyBalance = balance.getXrp();
				balance.setXrp(baseCurrencyBalance.add(cancelledQuantity));
			}

			balanceRepository.save(balance);

		}

	}
}
