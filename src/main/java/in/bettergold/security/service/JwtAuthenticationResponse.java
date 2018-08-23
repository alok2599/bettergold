package in.bettergold.security.service;

import java.io.Serializable;

/**
 * Created by stephan on 20.03.16.
 */
public class JwtAuthenticationResponse implements Serializable {

    private static final long serialVersionUID = 1250166508152483573L;

    private final String token;
    private final String status;
    private final String userChannel;
   

	public JwtAuthenticationResponse(String token, String userChannel, String status) {
        this.token = token;
        this.userChannel = userChannel;
        this.status = status;
    }

    public String getToken() {
        return this.token;
    }
    
    public String getStatus() {
		return status;
	}

	public String getUserChannel() {
		return userChannel;
	}

	 
    
    
}
