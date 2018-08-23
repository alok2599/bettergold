package in.bettergold.websocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

import in.bettergold.repository.OrderRepository;
import in.bettergold.websocket.wsmessage.GenericDepthMessageWS;
import in.bettergold.websocket.wsmessage.GenericTradeMessageWS;
import in.bettergold.websocket.wsmessage.MiniMarketWSMessage;
import in.bettergold.websocket.wsmessage.UserBalanceStatusMessageWS;
import in.bettergold.websocket.wsmessage.UserOrderStatusMessageWS;

 

@Configuration
@EnableScheduling
public class WsPusher {
       
    @Autowired
	JmsTemplate jmsTemplate;
    
    @Autowired
	OrderRepository orderRepository;
    
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    
	@JmsListener(destination = "fromLob.ws.depth.queue")
	private void depthWSConsumer(GenericDepthMessageWS depth) {
		//send to all users
		simpMessagingTemplate.convertAndSend("/generic/realtime", depth );
		
}
	
	@JmsListener(destination = "fromLob.ws.trade.queue")
	private void tradeWSConsumer(GenericTradeMessageWS trade) {
		//send to all users
		simpMessagingTemplate.convertAndSend("/generic/realtime", trade );
}
	@JmsListener(destination = "fromLob.ws.mini.market.trade.queue")
	private void tradeWSMiniMarketConsumer(MiniMarketWSMessage miniMarketTrade) {
		//send to all users
		simpMessagingTemplate.convertAndSend("/generic/realtime", miniMarketTrade );
}
	
	@JmsListener(destination = "user.ws.order.status.queue")
	private void userTradeWSConsumer(UserOrderStatusMessageWS userOrderStatusMessageWS) {
		//send to specific user
		simpMessagingTemplate.convertAndSendToUser(userOrderStatusMessageWS.getChannel(), "/notify", userOrderStatusMessageWS.getPendingOrderWSMessage() );
}
	//send to specific user
	@JmsListener(destination = "user.ws.balance.status.queue")
	private void userBalanceWSConsumer(UserBalanceStatusMessageWS userBalanceStatusMessageWS) {
		simpMessagingTemplate.convertAndSendToUser(userBalanceStatusMessageWS.getChannel(), "/notify", userBalanceStatusMessageWS.getBalance() );
}
	
 
}

 