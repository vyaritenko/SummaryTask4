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

public class ReplenishCommand extends Command {
    private static final Logger LOG = Logger.getLogger(ReplenishCommand.class);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, AppException {
        LOG.debug("Command 'replenishCommand' starts");
        HttpSession httpSession = request.getSession(false);

        User user = (User)httpSession.getAttribute("user");
        LOG.trace("Get the attribute 'user' --> " + user);
        String forward = null;
        if(user == null){
            forward = Path.OPEN_PAGE_LOGIN;
        }else {
            DBManager manager = DBManager.getInstance();
            int sum = Integer.parseInt(request.getParameter("summa"));
            LOG.trace("Request parameter: summa --> " + sum);
            int bill = user.getBill() + sum;
            manager.updateUserBill(user, bill);
            LOG.trace("New user bull --> " + bill);
            forward = Path.COMMAND_FUND_ACCOUNT;
        }
        LOG.debug("Command 'replenishCommand' finished");
        return forward;
    }
}
