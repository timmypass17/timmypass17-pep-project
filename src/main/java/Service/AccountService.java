package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAO();
    }
    /**
     * Constructor for a AuthorService when a AccountDAO is provided.
     * This is used for when a mock AccountDAO that exhibits mock behavior is used in the test cases.
     * This would allow the testing of AuthorService independently of AccountDAO.
     * There is no need to modify this constructor.
     * @param accountDAO
     */
    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    /**
     * Use the AccountDAO to persist an account. The given Account will not have an id provided.
     * The registration will be successful if and only if the username is not blank, the password
     *  is at least 4 characters long, and an Account with that username does not already exist. 
     * If all these conditions are met, the response body should contain a JSON of the Account, including
     *  its account_id. The response status should be 200 OK, which is the default. The new account should
     *  be persisted to the database. If the registration is not successful, the response status should be 400. (Client error)
     * @param author an author object.
     * @return The persisted author if the persistence is successful.
     */
    public Account addAccount(Account account) {
        if (account.getUsername().isBlank()) {
            return null;
        }

        if (account.getPassword().length() < 4) {
            return null;
        }

        if (accountDAO.getAccountByUsername(account.username) != null) {
            return null;
        }

        return accountDAO.insertAccount(account);
    }

    public Account getAccount(String username, String password) {
        return accountDAO.getAccount(username, password);
    }

}
