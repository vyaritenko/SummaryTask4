package ua.nure.yaritenko.SummaryTask4.web.command;

import org.apache.log4j.Logger;
import ua.nure.yaritenko.SummaryTask4.Path;
import ua.nure.yaritenko.SummaryTask4.exception.AppException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class RegistrationSuccessfullyCommand extends Command {
    private static final Logger LOG = Logger.getLogger(RegistrationSuccessfullyCommand.class);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, AppException {
        LOG.debug("Command 'registrationSuccessfullyCommand' starts");
        HttpSession httpSession = request.getSession(false);
        if (httpSession.getAttribute("createUser") != null){
            httpSession.removeAttribute("createUser");
            LOG.trace("Remove the session attribute: createUser");
        }
        LOG.debug("Command 'registrationSuccessfullyCommand' finished");
        return Path.PAGE_REGSUC;
    }
}
