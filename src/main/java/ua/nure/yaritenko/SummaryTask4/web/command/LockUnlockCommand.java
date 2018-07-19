package ua.nure.yaritenko.SummaryTask4.web.command;

import org.apache.log4j.Logger;
import ua.nure.yaritenko.SummaryTask4.Path;
import ua.nure.yaritenko.SummaryTask4.db.DBManager;
import ua.nure.yaritenko.SummaryTask4.db.UserLock;
import ua.nure.yaritenko.SummaryTask4.db.entity.User;
import ua.nure.yaritenko.SummaryTask4.exception.AppException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;

public class LockUnlockCommand extends Command {
    private static final Logger LOG = Logger.getLogger(LockUnlockCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, AppException {
        LOG.debug("Command 'LockUnlockCommand' starts");
        HttpSession httpSession = request.getSession(false);
        User currentUser = (User) httpSession.getAttribute("user");
        LOG.trace("Get the attribute 'user'(current user) --> " + currentUser);
        String forward = null;
        if (currentUser == null) {
            forward = Path.OPEN_PAGE_LOGIN;
        } else {
            DBManager manager = DBManager.getInstance();
            Enumeration parameters = request.getParameterNames();
            while (parameters.hasMoreElements()) {
                String parameter = parameters.nextElement().toString();
                LOG.trace("Request parameter: parameter --> " + parameter);
                if (parameter.equals("command") || parameter.equals("lock")) {
                    continue;
                } else {
                    int idUser = Integer.parseInt(parameter);
                    User user = manager.findUserById(idUser);
                    LOG.trace("Update attribute 'user' --> " + user);
                    UserLock userLock = UserLock.getUserLock(user);
                    LOG.trace("userLock --> " + userLock);
                    if (currentUser.getId() == idUser) {
                        throw new AppException("You cannot block yourself!");
                    }
                    if (userLock == UserLock.UNLOCKED) {
                        if (manager.lockUserById(idUser)) {
                            LOG.trace("User " + user.getLogin() + " locked");
                            forward = Path.COMMAND_LOCK_USER;
                        }
                    } else if (userLock == UserLock.LOCK) {
                        if (manager.unlockUserById(idUser)) {
                            LOG.trace("User " + user.getLogin() + " unlocked");
                            forward = Path.COMMAND_LOCK_USER;
                        }
                    }
                }
            }
        }
        LOG.trace("forward --> " + forward);
        LOG.debug("Command 'LockUnlockCommand' finished");
        return forward;
    }
}
