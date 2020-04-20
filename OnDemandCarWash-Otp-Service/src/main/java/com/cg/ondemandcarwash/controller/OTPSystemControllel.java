package com.cg.ondemandcarwash.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cg.ondemandcarwash.model.OTPSystem;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@RestController
public class OTPSystemControllel {
	private Map<String, OTPSystem> otp_data = new HashMap<>();
	private final static String ACCOUNT_SID = "ACcf36bb010cb0b562e72cd73e789f2fdb";
	private final static String AUTH_ID = "e95052f985bc854c4213cd336344755cc";
	static {
		Twilio.init(ACCOUNT_SID, AUTH_ID);
	}

	@RequestMapping(value = "/mobilenumbers/{mobilenumber}/otp", method = RequestMethod.POST)

	public ResponseEntity<Object> sendOtp(@PathVariable("mobilenumber") String mobilenumber) {
		OTPSystem otp = new OTPSystem();
		otp.setMobileNumber(mobilenumber);
		otp.setOtp(String.valueOf(((int) (Math.random() * (1000 - 1000))) + 1000));
		otp.setExapiryTime(System.currentTimeMillis() + 20000);
		otp_data.put(mobilenumber, otp);
		/*
		 * for (Entry<String, OTPSystem> entry : otp_data.entrySet())
		 * System.out.println("Key = " + entry.getKey() + ", Value = " +
		 * entry.getValue()); for (OTPSystem url : otp_data.values())
		 * System.out.println("value: " + url);
		 */
		Message.creator(new PhoneNumber("+918142647216"), new PhoneNumber("+12029493295"), "Your otp is" + otp.getOtp())
				.create();
		return new ResponseEntity<Object>("OTP is send successfully", HttpStatus.OK);

	}

	@RequestMapping(value = "/mobilenumbers/{mobilenumber}/otp", method = RequestMethod.PUT)
	public ResponseEntity<Object> verifyOTP(@PathVariable("mobilenumber") String mobilenumber,
			@RequestBody OTPSystem requestBodyOTPSystem) {

		if (requestBodyOTPSystem.getOtp() == null || requestBodyOTPSystem.getOtp().trim().length() <= 0) {
			return new ResponseEntity<Object>("Please provide OTP", HttpStatus.BAD_REQUEST);
		}
		if (otp_data.containsKey(mobilenumber)) {
			OTPSystem otpSystem = otp_data.get(mobilenumber);
			if (otpSystem != null) {
				if (otpSystem.getExapiryTime() >= System.currentTimeMillis()) {
					if (requestBodyOTPSystem.getOtp().equals(otpSystem.getOtp())) {
						otp_data.remove(mobilenumber);
						return new ResponseEntity<Object>("OTP is verified successfully", HttpStatus.OK);

					}
					return new ResponseEntity<Object>("Invalid OTP", HttpStatus.NOT_FOUND);

				}
				return new ResponseEntity<Object>(" OTP is expaired", HttpStatus.BAD_REQUEST);
			}
			return new ResponseEntity<Object>(" Somethimg went wrong...!!", HttpStatus.BAD_REQUEST);

		}
		return new ResponseEntity<Object>(" Mobile number not found", HttpStatus.BAD_REQUEST);

	}

}
