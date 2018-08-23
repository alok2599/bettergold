package in.bettergold.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import in.bettergold.model.Balance;
import in.bettergold.model.Order;

public interface BalanceRepository extends JpaRepository<Balance, Long> {
	  // public Order findByOrderId(String orderId);
	   public Balance findByUsername(String username);
	   //public  S save(Order order);
	
	}