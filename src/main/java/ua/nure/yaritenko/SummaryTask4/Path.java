package ua.nure.yaritenko.SummaryTask4;
/**
 * Path holder (jsp pages, controller commands).
 *
 * @author V.Yaritenko
 *
 */
public final class Path {
    // pages
    public static final String OPEN_PAGE_LOGIN = "/post/jsp/login.jsp";
    public static final String PAGE_USER = "/post/jsp/client/user.jsp";
    public static final String PAGE_ADMIN = "/post/jsp/admin/admin.jsp";
    public static final String PAGE_REGSUC = "/WEB-INF/jsp/regSuc.jsp";
    public static final String PAGE_ACCOUNT = "/post/jsp/account.jsp";
    public static final String PAGE_FUND_ACCOUNT = "/post/jsp/fundAccount.jsp";
    public static final String PAGE_ERROR_PAGE = "/WEB-INF/jsp/error_page.jsp";
    public static final String OPEN_PAGE_INDEX = "/index.jsp";
    public static final String OPEN_PAGE_ADD_EDITION = "/post/jsp/admin/addEdition.jsp";
    public static final String OPEN_PAGE_EDIT_EDITION = "/post/jsp/admin/editEdition.jsp";
    public static final String OPEN_PAGE_LOCK = "/post/jsp/admin/lock.jsp";
    public static final String PAGE_SETTINGS = "/post/jsp/settings.jsp";
    // commands
    public static final String COMMAND_ADMIN = "/controller?command=admin";
    public static final String COMMAND_USER = "/controller?command=user";
    public static final String COMMAND_REGISTRATION_SUCCESSFULLY = "/controller?command=registrationSuccessfully&createUser=1";
    public static final String COMMAND_FUND_ACCOUNT = "/controller?command=fundAccount";
    public static final String COMMAND_SETTINGS = "/controller?command=openSettings";
    public static final String COMMAND_ACCOUNT = "/controller?command=account";
    public static final String COMMAND_ADD_EDITION = "/controller?command=addEdition";
    public static final String COMMAND_EDIT_EDITION = "/controller?command=editEdition";
    public static final String COMMAND_LOCK_USER = "/controller?command=admin&lock=lock";
}
