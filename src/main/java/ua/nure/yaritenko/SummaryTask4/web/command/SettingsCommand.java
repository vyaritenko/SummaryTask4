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

public class SettingsCommand extends Command {
    private static final Logger LOG = Logger.getLogger(SettingsCommand.class);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, AppException {
        LOG.debug("Command 'settingsCommand' starts");
        HttpSession httpSession = request.getSession(false);
        User user = (User) httpSession.getAttribute("user");
        LOG.trace("Get the session attribute 'user' --> " + user);
        String forward = null;
        if(user == null){
            forward = Path.OPEN_PAGE_LOGIN;
        }else {
            DBManager manager = DBManager.getInstance();

            String password = request.getParameter("password");
            LOG.trace("Request parameter: password");
            String firstName = request.getParameter("firstName");
            LOG.trace("Request parameter: firstName --> " + firstName);
            String lastName = request.getParameter("lastName");
            LOG.trace("Request parameter: lastName --> " + lastName);
            String email = request.getParameter("email");
            LOG.trace("Request parameter: email --> " + email);
            String hashPassword = null;
            try {
                LOG.trace("Start hashing  of password");
                hashPassword = Password.hash(password);
                LOG.trace("Password has hashed");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                LOG.equals("There is no such algorithm for password hashing" + e);
            }
            if (manager.updateUserByLogin(User.createUser(user.getLogin(), hashPassword, firstName, lastName, email))) {
                LOG.trace("User " + user.getLogin() + "updated");
                user = manager.findUserByLogin(user.getLogin());
                httpSession.setAttribute("user", user);
                LOG.trace("Set the session attribute: user --> " + user);
                forward = Path.COMMAND_SETTINGS;
            }
        }
        LOG.debug("Command 'settingsCommand' finished");
        return forward;
    }
}
