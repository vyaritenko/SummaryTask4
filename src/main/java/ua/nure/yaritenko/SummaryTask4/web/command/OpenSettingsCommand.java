package ua.nure.yaritenko.SummaryTask4.web.command;

import org.apache.log4j.Logger;
import ua.nure.yaritenko.SummaryTask4.Path;
import ua.nure.yaritenko.SummaryTask4.db.DBManager;
import ua.nure.yaritenko.SummaryTask4.db.Role;
import ua.nure.yaritenko.SummaryTask4.db.UserLock;
import ua.nure.yaritenko.SummaryTask4.db.entity.User;
import ua.nure.yaritenko.SummaryTask4.exception.AppException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class OpenSettingsCommand extends Command {
    private static final Logger LOG = Logger.getLogger(OpenSettingsCommand.class);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, AppException {
        LOG.debug("Command 'openSettingsCommand' starts");
        HttpSession httpSession = request.getSession(false);
        User user = (User) httpSession.getAttribute("user");
        LOG.trace("Get the attribute 'user' --> " + user);
        String forward;
        if(user == null){
            forward = Path.OPEN_PAGE_LOGIN;
        }else {
            DBManager manager = DBManager.getInstance();

            user = manager.findUserByLogin(user.getLogin());
            LOG.trace("Update attribute 'user' --> " + user);
            UserLock userLock = UserLock.getUserLock(user);
            LOG.trace("userLock --> " + userLock);
            if (userLock == UserLock.LOCK) {
                throw new AppException("User locked");
            }
            Role userRole = Role.getRole(user);
            LOG.trace("userRole --> " + userRole);
            httpSession.setAttribute("user", user);
            LOG.trace("Set the session attribute: user --> " + user);
            httpSession.setAttribute("userRole", userRole);
            LOG.trace("Set the session attribute: userRole --> " + userRole);
            forward = Path.PAGE_SETTINGS;
        }
        LOG.debug("Command 'openSettingsCommand' finished");
        return forward;
    }
}
