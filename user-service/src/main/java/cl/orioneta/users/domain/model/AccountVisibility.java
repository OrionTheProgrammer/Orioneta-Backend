package cl.orioneta.users.domain.model;

/**
 * Defines how discoverable a user profile is for social features.
 *
 * <p>The enum belongs to {@code user-service} because visibility is part of the
 * user's public identity. Services such as {@code friendship-service} should
 * query user data instead of duplicating this rule.
 */
public enum AccountVisibility {
    PUBLIC,
    FRIENDS_ONLY,
    PRIVATE
}
