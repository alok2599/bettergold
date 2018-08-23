package in.bettergold.controller.trade;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import in.bettergold.security.jwtauthentication.JwtTokenUtil;
import in.bettergold.security.jwtauthentication.JwtUser;


@RestController
public class Test {
	@Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;
	@GetMapping("/test")
	public String test() {

		return "SpringTest";

	}
	 
	@GetMapping("/authstatus")
	public JwtUser getAuthenticatedUser(HttpServletRequest request) {
        
		String token = request.getHeader(tokenHeader).substring(7);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);
        return user;
    }
}
