package in.bettergold.controller.trade;

 import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import in.bettergold.model.Pair;
import in.bettergold.repository.BalanceRepository;
import in.bettergold.repository.OrderRepository;
import in.bettergold.repository.PairRepository;

@RestController
 
public class StaticController {

	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
    BalanceRepository balanceRepository;
	
	@Autowired
	PairRepository pairRepository;
 
	@GetMapping("/symbols")
	public List<Pair> getSymbols(){
		
		return pairRepository.findAll();
		
	}

 
	
}
