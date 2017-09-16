package com.ja0ck5.netty;


import com.twilio.sdk.Twilio;
import com.twilio.sdk.resource.api.v2010.account.Message;
import com.twilio.sdk.type.PhoneNumber;


/**
 * Hello world!
 *
 */
public class App {
	// Find your Account Sid and Token at twilio.com/user/account
	public static final String ACCOUNT_SID = "AC5ef872f6da5a21de157d80997a64bd33";
	public static final String AUTH_TOKEN = "your_auth_token";

	public static void main(String[] args) {
		Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
		Message message = Message
				.create(ACCOUNT_SID,new PhoneNumber("+16518675309"), new PhoneNumber("+14158141829"),
						"Tomorrow's forecast in Financial District, San Francisco is Clear").execute();
//				.setMediaUrl("https://climacons.herokuapp.com/clear.png").create();
		System.out.println(message.getSid());
	}
}
