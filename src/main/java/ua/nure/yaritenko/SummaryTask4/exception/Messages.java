package ua.nure.yaritenko.SummaryTask4.exception;

/**
 * Holder for messages of exceptions.
 *
 * @author V.Yaritenko
 */
public class Messages {
    private Messages() {
        // no op
    }

    public static final String ERR_CANNOT_OBTAIN_DATA_SOURCE = "Cannot obtain the data source";
    public static final String ERR_CANNOT_OBTAIN_CONNECTION = "Cannot obtain a connection from the pool";
    public static final String ERR_CANNOT_OBTAIN_CATEGORIES = "Cannot obtain categories";
    public static final String ERR_CANNOT_OBTAIN_EDITION = "Cannot obtain edition";
    public static final String ERR_CANNOT_OBTAIN_EDITION_BY_SORTING = "Cannot obtain edition by sorting";
    public static final String ERR_CANNOT_OBTAIN_EDITION_BY_ID = "Cannot obtain edition by id";
    public static final String ERR_CANNOT_OBTAIN_EDITION_BY_TOPIC = "Cannot obtain edition by topic";
    public static final String ERR_CANNOT_OBTAIN_EDITION_BY_TOPIC_AND_SORTING = "Cannot obtain edition by topic and sorting";
    public static final String ERR_CANNOT_OBTAIN_EDITION_BY_SEARCH = "Cannot obtain edition by search";
    public static final String ERR_CANNOT_CREATE_USER = "Cannot create user";
    public static final String ERR_CANNOT_CREATE_EDITION = "Cannot create editon";
    public static final String ERR_CANNOT_CREATE_CATEGORY = "Cannot create category";
    public static final String ERR_CANNOT_OBTAIN_ID_CATEGORY = "Cannot obtain id category";
    public static final String ERR_CANNOT_OBTAIN_EXISTING_EDITION = "Cannot obtain existing edition";
    public static final String ERR_CANNOT_CHECK_LOGIN = "Cannot check login";
    public static final String ERR_CANNOT_CREATE_ORDER = "Cannot create order";
    public static final String ERR_CANNOT_UPDATE_USER_BILL = "Cannot update user bill";
    public static final String ERR_CANNOT_INSERT_ORDER = "Cannot insert order. Transaction failed";
    public static final String ERR_CANNOT_CHECK_SUBSCRIBE = "Cannot check subscribe";
    public static final String ERR_CANNOT_DELETE_SUBSCRIBE = "Cannot delete subscribe";
    public static final String ERR_CANNOT_DELETE_EDITION = "Cannot delete edition";
    public static final String ERR_CANNOT_UPDATE_EDITION = "Cannot update edition";
    public static final String ERR_CANNOT_OBTAIN_ALL_USER = "Cannot obtain all user";
    public static final String ERR_CANNOT_LOCK_USER_BY_ID = "Cannot lock user by id";
    public static final String ERR_CANNOT_UNLOCK_USER_BY_ID = "Cannot unlock user by id";
    public static final String ERR_CANNOT_OBTAIN_USER_BY_SEARCH = "Cannot obtain user by search";
    public static final String ERR_CANNOT_OBTAIN_ID_ORDER_BY_USER_AND_EDITION = "Cannot obtain id order by user and edition";
    public static final String ERR_CANNOT_OBTAIN_USER_BY_ID = "Cannot obtain user by id";
    public static final String ERR_CANNOT_OBTAIN_USER_BY_LOGIN = "Cannot obtain a user by its login";
    public static final String ERR_CANNOT_OBTAIN_ORDERS_BY_USER = "Cannot obtain orders by user";
    public static final String ERR_CANNOT_OBTAIN_EDITION_BY_ORDERS = "Cannot obtain edition by orders";
    public static final String ERR_UPDATE_USER_BY_LOGIN = "Cannot update user by login";
    public static final String ERR_CANNOT_CLOSE_CONNECTION = "Cannot close a connection";
    public static final String ERR_CANNOT_CLOSE_RESULTSET = "Cannot close a result set";
    public static final String ERR_CANNOT_CLOSE_STATEMENT = "Cannot close a statement";
    public static final String ERR_CANNOT_FIND_USERS_ID = "Cannot find users_id";
}
