package ua.nure.yaritenko.SummaryTask4.web.command;

import org.apache.log4j.Logger;
import ua.nure.yaritenko.SummaryTask4.Path;
import ua.nure.yaritenko.SummaryTask4.db.DBManager;
import ua.nure.yaritenko.SummaryTask4.db.entity.User;
import ua.nure.yaritenko.SummaryTask4.exception.AppException;
import ua.nure.yaritenko.SummaryTask4.password.Password;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class RegistrationCommand extends Command {
    private static final Logger LOG = Logger.getLogger(RegistrationCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, AppException {
        LOG.debug("Command 'registrationCommand' starts");
        HttpSession httpSession = request.getSession();
        DBManager manager = DBManager.getInstance();
        String login = request.getParameter("login");
        LOG.trace("Request parameter: loging --> " + login);
        String password = request.getParameter("password");
        LOG.trace("Request parameter: password --> " + password);
        String firstName = request.getParameter("first_name");
        LOG.trace("Request parameter: first_name --> " + firstName);
        String lastName = request.getParameter("last_name");
        LOG.trace("Request parameter: last_name --> " + lastName);
        String email = request.getParameter("email");
        LOG.trace("Request parameter: email --> " + email);
        LOG.trace("Check login");
        boolean isLoginExist = manager.checkLogin(login);
        LOG.trace("Login is --> " + isLoginExist);
        String local = null;
        LOG.trace("Set local");
        if (httpSession.getAttribute("local") != null) {
            local = httpSession.getAttribute("local").toString();
            LOG.trace("Set the session attribute: local --> " + local);
        }
        if (!isLoginExist) {
            if ((local != null) && (local.equals("en"))) {
                throw new AppException("Login exists");
            } else if ((local == null) || (local.equals("ru"))) {
                throw new AppException("Логин существует");
            }
        }
        String hashPassword = null;
        try {
            LOG.trace("Start hashing of password");
            hashPassword = Password.hash(password);
            System.out.println(hashPassword);
            LOG.trace("Password has hashed");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            LOG.equals("There is no such algorithm for password hashing" + e);
        }
        String forward = Path.PAGE_ERROR_PAGE;
        if (manager.insertUser(User.createUser(login, hashPassword, firstName, lastName, email))) {
            LOG.info("User created: users`s login --> " + login);
            forward = Path.COMMAND_REGISTRATION_SUCCESSFULLY;
        }
        LOG.trace("forward -->" + forward);
        LOG.debug("Command 'registrationCommand' finished");
        return forward;
    }
}
