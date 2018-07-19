package ua.nure.yaritenko.SummaryTask4.web.command;

import org.apache.log4j.Logger;

import java.util.Map;
import java.util.TreeMap;

/**
 * Holder for all commands.
 *
 * @author V.Yaritenko
 */
public class CommandContainer {
    private static Map <String, Command> commands = new TreeMap <>();
    private static final Logger LOG = Logger.getLogger(CommandContainer.class);

    static {
        // common commands
        commands.put("login", new LoginCommand());
        commands.put("loadingEdition", new LoadingEditionCommand());
        commands.put("registration", new RegistrationCommand());
        commands.put("registrationSuccessfully", new RegistrationSuccessfullyCommand());
        commands.put("logout", new LogoutCommand());
        commands.put("subscribe", new SubscribeCommand());
        commands.put("unsubscribe", new UnsubscribeCommand());
        commands.put("noCommand", new NoCommand());
        //client commands
        commands.put("user", new OpenUserCommand());
        commands.put("account", new AccountCommand());
        commands.put("fundAccount", new FundAccountCommand());
        commands.put("replenish", new ReplenishCommand());
        commands.put("settings", new SettingsCommand());
        commands.put("openSettings", new OpenSettingsCommand());
        // admin commands
        commands.put("admin", new OpenAdminCommand());
        commands.put("addEdition", new AddEditionCommand());
        commands.put("editEdition", new EditEditionCommand());
        commands.put("deleteEdition", new DeleteCommand());
        commands.put("saveEditEdition", new SaveEditEditionCommand());
        commands.put("searchUser", new SearchUserCommand());
        commands.put("lockUnlock", new LockUnlockCommand());
    }

    /**
     * Returns command object with the given name.
     *
     * @param commandName Name of the command.
     * @return Command object.
     */
    public static Command get(String commandName) {
        if (commandName == null || !commands.containsKey(commandName)) {
            LOG.trace("Command not found, name --> " + commandName);
            return commands.get("noCommand");
        }
        return commands.get(commandName);
    }
}
