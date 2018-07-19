package ua.nure.yaritenko.SummaryTask4.web.command;

import org.apache.log4j.Logger;
import ua.nure.yaritenko.SummaryTask4.Path;
import ua.nure.yaritenko.SummaryTask4.exception.AppException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LogoutCommand extends Command {
    private static final Logger LOG = Logger.getLogger(LogoutCommand.class);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, AppException {
       LOG.debug("Command 'logoutCommand' starts");
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        LOG.debug("Command 'logoutCommand' finished");
        return Path.OPEN_PAGE_LOGIN;
    }
}
