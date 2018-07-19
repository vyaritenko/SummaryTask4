package ua.nure.yaritenko.SummaryTask4.web.command;

import org.apache.log4j.Logger;
import ua.nure.yaritenko.SummaryTask4.Path;
import ua.nure.yaritenko.SummaryTask4.db.DBManager;
import ua.nure.yaritenko.SummaryTask4.db.Role;
import ua.nure.yaritenko.SummaryTask4.db.UserLock;
import ua.nure.yaritenko.SummaryTask4.db.entity.Category;
import ua.nure.yaritenko.SummaryTask4.db.entity.Edition;
import ua.nure.yaritenko.SummaryTask4.db.entity.User;
import ua.nure.yaritenko.SummaryTask4.exception.AppException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class OpenUserCommand extends Command {
    private static final Logger LOG = Logger.getLogger(OpenUserCommand.class);

    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, AppException {
        LOG.debug("Command 'openUserCommand' starts");
        HttpSession httpSession = request.getSession();
        DBManager manager = DBManager.getInstance();
        String forward;
        User user = (User) httpSession.getAttribute("user");
        LOG.trace("Get the attribute 'user' --> " + user);
        if (user == null) {
            forward = Path.OPEN_PAGE_LOGIN;
        } else {
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
            String selectSorting = request.getParameter("selectSorting");
            LOG.trace("Request parameter: selectSorting --> " + selectSorting);
            String selectTopic = request.getParameter("selectTopic");
            LOG.trace("Request parameter: selectTopic --> " + selectTopic);
            String searchEdition = request.getParameter("searchEdition");
            LOG.trace("Request parameter: searchEdition --> " + searchEdition);
            List <Edition> editionList;
            Map <Integer, Category> categoryList = manager.findCategories();
            if (selectSorting == null & selectTopic == null & searchEdition == null) {
                editionList = manager.findAllEdition();
                httpSession.setAttribute("editionList", editionList);
                LOG.trace("Set the session attribute 'editionList' --> " + editionList);
                httpSession.setAttribute("categoryList", categoryList);
                LOG.trace("Set the session attribute 'categoryList' --> " + categoryList);
            }
            forward = Path.PAGE_USER;
        }
        LOG.trace("forward --> " + forward);
        LOG.debug("Command 'openUserCommand' finished");
        return forward;
    }
}
