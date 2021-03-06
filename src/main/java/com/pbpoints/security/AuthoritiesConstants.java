package com.pbpoints.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_USER";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    public static final String SCOREKEEPER = "ROLE_SCOREKEEPER";

    public static final String ROLE_OWNER_TOURNAMENT = "ROLE_OWNER_TOURNAMENT";

    private AuthoritiesConstants() {}
}
