package ua.nure.yaritenko.SummaryTask4.web.command;

import org.apache.log4j.Logger;
import ua.nure.yaritenko.SummaryTask4.Path;
import ua.nure.yaritenko.SummaryTask4.db.DBManager;
import ua.nure.yaritenko.SummaryTask4.db.Role;
import ua.nure.yaritenko.SummaryTask4.db.entity.Category;
import ua.nure.yaritenko.SummaryTask4.db.entity.Edition;
import ua.nure.yaritenko.SummaryTask4.exception.AppException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;


public class LoadingEditionCommand extends Command {
    private static final Logger LOG = Logger.getLogger(LoadingEditionCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, AppException {
        LOG.debug("Command 'loadingEditionCommand' starts");
        DBManager manager = DBManager.getInstance();
        HttpSession httpSession = request.getSession();
        String forward = null;
        String selectSorting = request.getParameter("selectSorting");
        LOG.trace("Request parameter: selectSorting --> " + selectSorting);
        String selectTopic = request.getParameter("selectTopic");
        LOG.trace("Request parameter: selectTopic --> " + selectTopic);
        String searchEdition = request.getParameter("searchEdition");
        LOG.trace("Request parameter: searchEdition --> " + searchEdition);
        List <Edition> editionList = null;
        Map <Integer, Category> categoryMap = manager.findCategories();
        if (selectSorting == null & selectTopic == null & searchEdition == null) {
            editionList = manager.findAllEdition();
        } else if (selectSorting != null & selectTopic != null & searchEdition == null) {
            if (selectSorting.equals("Не выбрано") & selectTopic.equals("Все категории")) {
                editionList = manager.findAllEdition();
            } else if (selectSorting.equals("Не выбрано") & !selectTopic.equals("Все категории")) {
                editionList = manager.findEditionByTopic(selectTopic);
            } else if (!selectSorting.equals("Не выбрано") & selectTopic.equals("Все категории")) {
                editionList = manager.findEditionBySorting(selectSorting);
            } else if (!selectSorting.equals("Не выбрано") & !selectTopic.equals("Все категории")) {
                editionList = manager.findEditionBySortingAndTopic(selectTopic, selectSorting);
            }
        } else if (selectSorting == null & selectTopic == null & searchEdition != null) {
            editionList = manager.findEditionBySearch(searchEdition);
        }
        httpSession.setAttribute("editionList", editionList);
        LOG.trace("Set the session attribute: editionList --> " + editionList);
        httpSession.setAttribute("categoryList", categoryMap);
        LOG.trace("Set the session attribute: categoryList --> " + categoryMap);
        if (request.getParameter("local") != null) {
            String local = request.getParameter("local");
            httpSession.setAttribute("local", local);
            LOG.trace("Set the session attribute: local --> " + local);
        }
        if (httpSession.getAttribute("user") == null) {
            forward = Path.OPEN_PAGE_INDEX;
        } else if ((httpSession.getAttribute("user") != null) & (httpSession.getAttribute("userRole") == Role.CLIENT)) {
            forward = Path.PAGE_USER;
        } else if ((httpSession.getAttribute("user") != null) & (httpSession.getAttribute("userRole") == Role.ADMIN)) {
            forward = Path.PAGE_ADMIN;
        }
        LOG.trace("forward --> " + forward);
        LOG.debug("Command 'loadingEditionCommand' finished");
        return forward;
    }
}
