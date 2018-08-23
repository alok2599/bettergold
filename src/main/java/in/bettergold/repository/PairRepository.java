package in.bettergold.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import in.bettergold.model.Order;
import in.bettergold.model.Pair;
import in.bettergold.model.Symbol;


public interface PairRepository extends JpaRepository<Pair, Long> {
	   
	   public Pair findBySymbol(Symbol symbol);
	  // public Pair findByBaseCurrency(Symbol quoteCurr);
	   //public  S save(Order order);
	}