package in.bettergold.engine.execution;

import java.util.Comparator;

public class OrderTimestampComparator implements Comparator {

	OrderBookEntry order1,order2;
	@Override
	public int compare(Object o1, Object o2) {
		
		this.order1 = (OrderBookEntry)o1;
		this.order2 = (OrderBookEntry)o2;
		
		//if( order1.getOrderTimestamp().isBefore(order2.getOrderTimestamp()))
		if( order1.getOrderTimestamp() < (order2.getOrderTimestamp()))
			return 1;
		else  
			return -1;
		//decide based on quantity if both are equal. currently not supported.
	}

}
