package ua.nure.yaritenko.SummaryTask4.web.command;

import org.apache.log4j.Logger;
import ua.nure.yaritenko.SummaryTask4.Path;
import ua.nure.yaritenko.SummaryTask4.db.DBManager;
import ua.nure.yaritenko.SummaryTask4.db.entity.Category;
import ua.nure.yaritenko.SummaryTask4.db.entity.Edition;
import ua.nure.yaritenko.SummaryTask4.db.entity.User;
import ua.nure.yaritenko.SummaryTask4.exception.AppException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class OpenAdminCommand extends Command {
    private static final Logger LOG = Logger.getLogger(OpenAdminCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, AppException {
        LOG.debug("Command 'openAdminCommand' starts");
        HttpSession httpSession = request.getSession();
        DBManager manager = DBManager.getInstance();
        String forward = Path.PAGE_ADMIN;
        User user = (User) httpSession.getAttribute("user");
        LOG.trace("Get the session attribute 'user' --> " + user);
        if ((user == null) && (request.getParameter("add") != null)) {
            forward = Path.OPEN_PAGE_LOGIN;
        }else if(user == null){
            forward = Path.OPEN_PAGE_LOGIN;
        }  else{
            user = manager.findUserByLogin(user.getLogin());
            LOG.trace("Update attribute 'user' --> " + user);
            httpSession.setAttribute("user", user);
            LOG.trace("Set the session attribute: user --> " + user);
            String selectSorting = request.getParameter("selectSorting");
            LOG.trace("Request parameter: selectSorting --> " + selectSorting);
            String selectTopic = request.getParameter("selectTopic");
            LOG.trace("Request parameter: selectTopic --> " + selectTopic);
            String searchEdition = request.getParameter("searchEdition");
            LOG.trace("Request parameter: searchEdition --> " + searchEdition);
            List<Edition> editionList;
            Map<Integer, Category> categoryList = manager.findCategories();
            if (selectSorting == null & selectTopic == null & searchEdition == null) {
                editionList = manager.findAllEdition();
                httpSession.setAttribute("editionList", editionList);
                LOG.trace("Set the session attribute: editionList --> " + editionList);
                httpSession.setAttribute("categoryList", categoryList);
                LOG.trace("Set the session attribute: categoryList --> " + categoryList);
            }
            if (request.getParameter("add") != null) {
                forward = Path.OPEN_PAGE_ADD_EDITION;
            }

            if (request.getParameter("lock") != null) {
                String[] role = {"admin", "client"};
                String[] userLock = {"lock", "unlock"};
                List<User> listAllUser = manager.findAllUser();
                httpSession.setAttribute("allUser", listAllUser);
                LOG.trace("Set the session attribute: allUser --> " + listAllUser);
                httpSession.setAttribute("role", role);
                LOG.trace("Set the session attribute: role --> " + role);
                httpSession.setAttribute("userLock", userLock);
                LOG.trace("Set the session attribute: userLock --> " + userLock);
                forward = Path.OPEN_PAGE_LOCK;
            }
        }
        LOG.debug("forward --> " + forward);
        LOG.debug("Command 'openAdminCommand' finished");
        return forward;
    }
}
