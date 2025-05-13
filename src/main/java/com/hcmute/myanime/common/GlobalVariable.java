package com.hcmute.myanime.common;

import java.util.Random;

public class GlobalVariable {

    public static final String DEFAULT_PAGE = "1";
    public static final String DEFAULT_LIMIT = "9";
    public static String GetOTP() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    public static final String EMAIL_CONFIRMATION_STATUS_PENDING = "pending";
    public static final String EMAIL_CONFIRMATION_STATUS_USED = "Used";

    public static final String ATTEMPT_LOGS_LOGIN_FAIL = "login_fail";
    public static final int MAX_ATTEMPT_LOGIN_ALLOW = 5;

    public static final String ATTEMPT_LOGS_RESET_PASSWORD_FAIL = "reset_password_fail";
    public static final int MAX_ATTEMPT_RESET_PASSWORD_ALLOW = 5;

    public static final String PREMIUM_USER = "premium_user";
    public static final String NORMAL_USER = "normal_user";

    // The rule is that each view of a client can only be increased at least once every 30 minutes
    public static final long MINIMUM_SECONDS_VIEW = 1800;
}
