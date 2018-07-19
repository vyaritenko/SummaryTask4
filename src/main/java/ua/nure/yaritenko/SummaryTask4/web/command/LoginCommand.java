package ua.nure.yaritenko.SummaryTask4.web.command;

import org.apache.log4j.Logger;
import ua.nure.yaritenko.SummaryTask4.Path;
import ua.nure.yaritenko.SummaryTask4.db.DBManager;
import ua.nure.yaritenko.SummaryTask4.db.Role;
import ua.nure.yaritenko.SummaryTask4.db.UserLock;
import ua.nure.yaritenko.SummaryTask4.db.entity.User;
import ua.nure.yaritenko.SummaryTask4.exception.AppException;
import ua.nure.yaritenko.SummaryTask4.password.Password;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class LoginCommand extends Command {
    private static final Logger LOG = Logger.getLogger(LoginCommand.class);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, AppException {
        LOG.debug("Command 'loginCommand' starts");
        HttpSession httpSession = request.getSession(false);
        // obtain login and password from a request
        DBManager manager = DBManager.getInstance();
        String login = request.getParameter("login");
        LOG.trace("Request parameter: loging --> " + login);
        String password = request.getParameter("password");
        User user = manager.findUserByLogin(login);
        LOG.trace("Found in DB: user --> " + user);
        String local = null;
        if (httpSession.getAttribute("local") != null) {
            local = httpSession.getAttribute("local").toString();
        }
        String hashPassword = null;
        try {
            LOG.trace("Start hashing of password");
            hashPassword = Password.hash(password);
            LOG.trace("Password hashed");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            LOG.equals("There is no such algorithm for password hashing" + e);
        }
        if (user == null || !hashPassword.equals(user.getPassword())) {
            if ((local != null) && (local.equals("en"))) {
                throw new AppException("Cannot find user with such login/password");
            } else if ((local == null) || (local.equals("ru"))) {
                throw new AppException("По такой логину/паролю пользователя не существует");
            }
        }
        UserLock userLock = UserLock.getUserLock(user);
        LOG.trace("userLock --> " + userLock);
        if (userLock == UserLock.LOCK) {
            if ((local != null) && (local.equals("en"))) {
                throw new AppException("User locked");
            } else if ((local == null) || (local.equals("ru"))) {
                throw new AppException("Пользователь заблокирован");
            }
        }
        Role userRole = Role.getRole(user);
        LOG.trace("userRole --> " + userRole);
        String forward = Path.PAGE_ERROR_PAGE;
        if (userRole == Role.ADMIN) {
            forward = Path.COMMAND_ADMIN;
        }
        if (userRole == Role.CLIENT) {
            forward = Path.COMMAND_USER;
        }
        httpSession.setAttribute("user", user);
        LOG.trace("Set the session attribute: user --> " + user);
        httpSession.setAttribute("userRole", userRole);
        LOG.trace("Set the session attribute: userRole --> " + userRole);
        LOG.info("User " + user + " logged as " + userRole.toString().toLowerCase());
        LOG.debug("forward --> " + forward);
        LOG.debug("Command 'loginCommand' finished");
        return forward;
    }
}
