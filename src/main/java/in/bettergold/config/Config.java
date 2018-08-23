package in.bettergold.config;

import javax.jms.Queue;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class Config {

	@Bean // Serialize message content to json using TextMessage
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }
	
	@Bean(name = "toLobQueue")
	public Queue toLobQueue() {
		return new ActiveMQQueue("toLob.queue");
	}
	
	@Bean(name = "fromLobExecutedQueue")
	public Queue fromLobExecutedQueue() {
		return new ActiveMQQueue("fromLob.executed.queue");
	}
	
	@Bean(name = "fromLobCancelledQueue")
	public Queue fromLobCancelledQueue() {
		return new ActiveMQQueue("fromLob.cancelled.queue");
	}
	 
	@Bean(name = "fromLobWSDepthQueue")
	public Queue fromLobWSDepthQueue() {
		return new ActiveMQQueue("fromLob.ws.depth.queue");
	}
	  
	@Bean(name = "fromLobWSTradeQueue")
	public Queue fromLobWSTradeQueue() {
		return new ActiveMQQueue("fromLob.ws.trade.queue");
	}
	
	@Bean(name = "fromLobWSMiniMarketTradeQueue")
	public Queue fromLobWSMiniMarketTradeQueue() {
		return new ActiveMQQueue("fromLob.ws.mini.market.trade.queue");
	}
	
	@Bean(name = "userWSOrderStatusQueue")
	public Queue userWSOrderStatusQueue() {
		return new ActiveMQQueue("user.ws.order.status.queue");
	}
	
	@Bean(name = "userWSBalanceStatusQueue")
	public Queue userWSBalanceStatusQueue() {
		return new ActiveMQQueue("user.ws.balance.status.queue");
	}
	
	 
}
