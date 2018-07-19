package ua.nure.yaritenko.SummaryTask4.db;
/**
 * Holder for fields names of DB tables and beans.
 *
 * @author V.Yaritenko
 *
 */
public final class Fields {
    // entities
    public static final String ENTITY_ID = "id";
    public static final String USER_LOGIN = "login";
    public static final String USER_PASSWORD = "password";
    public static final String USER_FIRST_NAME = "first_name";
    public static final String USER_LAST_NAME = "last_name";
    public static final String USER_EMAIL = "email";
    public static final String USER_BILL = "bill";
    public static final String USER_ROLE_ID = "roles_id";
    public static final String USER_LOCK_ID = "user_lock_id";
    public static final String ORDER_BILL = "bill";
    public static final String ORDER_USER_ID = "users_id";
    public static final String ORDER_STATUS_ID = "statuses_id";
    public static final String CATEGORY_NAME = "name";
    public static final String EDITION_PRICE = "price";
    public static final String EDITION_NAME = "name";
    public static final String EDITION_CATEGORY_ID = "categories_id";
    public static final String ORDERS_EDITIONS_ORDERS_ID = "orders_id";
    public static final String ORDERS_EDITIONS_EDITIONS_ID = "editions_id";
    // beans
    public static final String USER_ORDER_BEAN_ORDER_ID = "id";
    public static final String USER_ORDER_BEAN_USER_FIRST_NAME = "first_name";
    public static final String USER_ORDER_BEAN_USER_LAST_NAME = "last_name";
    public static final String USER_ORDER_BEAN_ORDER_BILL = "bill";
    public static final String USER_ORDER_BEAN_STATUS_NAME = "name";
}
