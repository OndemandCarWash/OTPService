package com.cg.ondemandcarwash.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OTPSystem {
	private String mobileNumber;

	private String otp;
	private long exapiryTime;
}
