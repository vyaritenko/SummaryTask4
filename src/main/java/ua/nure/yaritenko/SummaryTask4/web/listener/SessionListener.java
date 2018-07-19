package ua.nure.yaritenko.SummaryTask4.web.listener;

import org.apache.log4j.Logger;
import ua.nure.yaritenko.SummaryTask4.db.DBManager;
import ua.nure.yaritenko.SummaryTask4.db.entity.Category;
import ua.nure.yaritenko.SummaryTask4.db.entity.Edition;
import ua.nure.yaritenko.SummaryTask4.exception.DBException;
import ua.nure.yaritenko.SummaryTask4.exception.Messages;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SessionListener implements HttpSessionListener {
    private static final Logger LOG = Logger.getLogger(SessionListener.class);
    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        LOG.debug("Start session");
        HttpSession httpSession = httpSessionEvent.getSession();
        String id = httpSession.getId();
        if (httpSession.isNew()) {
            httpSession.setAttribute("id", id);
        }

        DBManager manager = DBManager.getInstance();
        LOG.debug("Start loading of all edition");
        List<Edition> editionList = null;
        Map<Integer, Category> categoryMap = null;
        try {
            categoryMap = manager.findCategories();
            editionList = manager.findAllEdition();
        } catch (DBException e) {
            LOG.error(Messages.ERR_CANNOT_OBTAIN_CATEGORIES, e);
            e.printStackTrace();
        }
        httpSession.setAttribute("editionList", editionList);
        httpSession.setAttribute("categoryList", categoryMap);
        LOG.debug("All editions are loaded");
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        HttpSession httpSession = httpSessionEvent.getSession();
        if(httpSession.getAttribute("id") != null) {
            LOG.debug("Session destroyed");
            httpSession.removeAttribute("id");
        }


    }
}
