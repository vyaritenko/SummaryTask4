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
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

public class UnsubscribeCommand extends Command {
    private static final Logger LOG = Logger.getLogger(UnsubscribeCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, AppException {
        LOG.debug("Command 'unsubscribeCommand' starts");
        HttpSession httpSession = request.getSession(false);
        User user = (User) httpSession.getAttribute("user");
        LOG.trace("Get the session attribute 'user' --> " + user);
        String forward = null;
        if(user == null){
            forward = Path.OPEN_PAGE_LOGIN;
        }else {
            DBManager manager = DBManager.getInstance();
            List<Integer> listIdSubdcribeEditions = new ArrayList<>();
            Enumeration parameters = request.getParameterNames();
            while (parameters.hasMoreElements()) {
                String parameter = parameters.nextElement().toString();
                LOG.trace("Request parameter 'parameter' --> " + parameter);
                if (parameter.equals("command")) {
                    continue;
                } else {
                    listIdSubdcribeEditions.add(Integer.parseInt(parameter));
                }
            }
            Map<Integer, Integer> idOrderIdEdition = manager.findIdOrderByUserAndEdition(user.getId(), listIdSubdcribeEditions);
            LOG.trace("Find id order and id edition --> " + idOrderIdEdition);
            if (manager.deleteSubscribe(idOrderIdEdition)) {
                forward = Path.COMMAND_ACCOUNT;
                LOG.trace("Subscribes deleted");
            }
        }
        LOG.trace("forward --> " + forward);
        LOG.debug("Command 'unsubscribeCommand' finished");
        return forward;
    }
}
