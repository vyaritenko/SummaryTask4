package ua.nure.yaritenko.SummaryTask4.web.command;

import org.apache.log4j.Logger;
import ua.nure.yaritenko.SummaryTask4.Path;
import ua.nure.yaritenko.SummaryTask4.db.DBManager;
import ua.nure.yaritenko.SummaryTask4.db.entity.User;
import ua.nure.yaritenko.SummaryTask4.exception.AppException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchUserCommand extends Command {
    private static final Logger LOG = Logger.getLogger(SearchUserCommand.class);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, AppException {
        LOG.debug("Command 'searchUserCommand' starts");
        HttpSession httpSession = request.getSession(false);
        String forward = null;
        User user = (User)httpSession.getAttribute("user");
        if(user == null){
            forward = Path.OPEN_PAGE_LOGIN;
        }else {
            DBManager manager = DBManager.getInstance();
            String search = request.getParameter("search");
            LOG.trace("Request parameter: search --> " + search);
            List<User> userList = new ArrayList<>();
            if (!search.isEmpty()) {
                userList.add(manager.findUserBySearch(search));
                httpSession.setAttribute("allUser", userList);
                LOG.trace("Set the session attribute: allUser --> " + userList);
                forward = Path.OPEN_PAGE_LOCK;
            }
        }
        LOG.debug("Command 'searchUserCommand' finished");
        return forward;
    }
}
