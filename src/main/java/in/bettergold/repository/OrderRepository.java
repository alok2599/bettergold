package in.bettergold.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.bettergold.model.Order;
import in.bettergold.model.OrderStatus;
 
public interface OrderRepository extends JpaRepository<Order, Long> {
   public Order findByOrderId(String orderId);
   public Order findByUsername(String username);
   @Query("select u from Order u where (u.status = 'OPEN' OR u.status = 'PARTIALLY_FILLED') AND u.username = ?1")
   public List<Order> findAllPendingOrdersByUsername(String username);
   //@Query(value = "SELECT o FROM Order o WHERE o.status = 'OPEN' OR o.status = 'PARTIALLY_FILLED' AND o.username = :username")
  // public (@Param("username")String username);
   //
   
}
