package com.safar.smartmessdevhacks.comman;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {

    private Pattern emailPattern, passPattern, phoneNumberPattern, upiPattern;
    private Matcher matcher;
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{6,}$";
    private static final String PHONE_NUMBER_PATTERN = "^[6-9]\\d{9}$";
    private static final String UPI_PATTERN = "^\\w+@(\\w+\\.)+\\w{2,3}$";

    public Validation() {
        emailPattern = Pattern.compile(EMAIL_PATTERN);
        passPattern = Pattern.compile(PASSWORD_PATTERN);
        phoneNumberPattern = Pattern.compile(PHONE_NUMBER_PATTERN);
        upiPattern = Pattern.compile(UPI_PATTERN);
    }

    public boolean emailValidation(String email){
        matcher = emailPattern.matcher(email);
        return matcher.matches();
    }

    public boolean passwordValidation(final String password) {
        matcher = passPattern.matcher(password);
        return matcher.matches();
    }

    public boolean phoneNumberValidation(final String phoneNumber) {
        matcher = phoneNumberPattern.matcher(phoneNumber);
        return matcher.matches();
    }

    public boolean upiValidation(final String upi) {
        matcher = upiPattern.matcher(upi);
        return matcher.matches();
    }
}
