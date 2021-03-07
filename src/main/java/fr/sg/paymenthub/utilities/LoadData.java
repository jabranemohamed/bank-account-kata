package fr.sg.paymenthub.utilities;

import fr.sg.paymenthub.entities.Account;
import fr.sg.paymenthub.entities.enums.AccountState;
import fr.sg.paymenthub.entities.enums.AccountType;
import fr.sg.paymenthub.entities.enums.CurrencyCode;
import fr.sg.paymenthub.exceptions.AccountNotFoundException;
import fr.sg.paymenthub.exceptions.InvalidOperationException;
import fr.sg.paymenthub.repositories.AccountRepository;
import fr.sg.paymenthub.services.OperationsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

/**
 * Utility class to create a fake account and create some transactions(Deposit,Withdrawal)
 */
@Component
public class LoadData {

    public static final String RETRAIT_CASH = "retrait cash";
    Logger logger = LoggerFactory.getLogger(LoadData.class);

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private OperationsService operationsService;

    public static final String ACCOUNT_IBAN = "FR1420041010050500013M02606";

    public void populateAccount() {

        Account myAccount = new Account().builder()
                .id(ACCOUNT_IBAN)
                .currency(CurrencyCode.EURO)
                .state(AccountState.ACTIVE)
                .type(AccountType.CUSTOMER)
                .iban(ACCOUNT_IBAN)
                .customerID(BigInteger.valueOf(500000L))
                .lastModified(LocalDateTime.now()).build();
        logger.info(" =====  Loading Demo Data           ====================");//JUST for the demo
        logger.info(" =====  Account Creation Step begin ====================");

        accountRepository.save(myAccount);

        logger.info(" =====  Created Account ID   " + ACCOUNT_IBAN);
        logger.info(" =====  Account Creation Step done ======================");
    }


    public void populateOperations() throws InvalidOperationException, AccountNotFoundException {

        operationsService.deposit(ACCOUNT_IBAN, new BigDecimal(1000), "initialisation");
        operationsService.deposit(ACCOUNT_IBAN, new BigDecimal(2000), "déposé cash");
        operationsService.deposit(ACCOUNT_IBAN, new BigDecimal(2000), "déposé cash");

        operationsService.withdraw(ACCOUNT_IBAN, new BigDecimal(200), RETRAIT_CASH);
        operationsService.withdraw(ACCOUNT_IBAN, new BigDecimal(300), RETRAIT_CASH);
        operationsService.withdraw(ACCOUNT_IBAN, new BigDecimal(500), RETRAIT_CASH);

        logger.info(" =====  End of loading data  ====================");


    }
}
