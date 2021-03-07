package fr.sg.paymenthub.services;

import fr.sg.paymenthub.entities.Account;
import fr.sg.paymenthub.entities.Transaction;
import fr.sg.paymenthub.entities.enums.AccountState;
import fr.sg.paymenthub.entities.enums.AccountType;
import fr.sg.paymenthub.entities.enums.CurrencyCode;
import fr.sg.paymenthub.entities.enums.TransactionType;
import fr.sg.paymenthub.exceptions.AccountNotFoundException;
import fr.sg.paymenthub.exceptions.InvalidOperationException;
import fr.sg.paymenthub.repositories.AccountRepository;
import fr.sg.paymenthub.repositories.TransactionRepository;
import fr.sg.paymenthub.services.impl.OperationsServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OperationsServiceTest {

    @InjectMocks
    private OperationsServiceImpl operationsService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    private static Account account;

    public static final String ACCOUNT_IBAN = "FR1420041010050500013M02606";

    @BeforeAll
    static void createAccount() {
        account = new Account().builder().id(ACCOUNT_IBAN).currency(CurrencyCode.EURO)
                .state(AccountState.ACTIVE).type(AccountType.CUSTOMER).iban(ACCOUNT_IBAN)
                .customerID(new BigInteger("5000000000000"))
                .lastModified(LocalDateTime.now()).balance(new BigDecimal(1000)).build();//On initalise un compte par un depot normalement
    }

    @Test
    void depositWithSuccess() throws InvalidOperationException, AccountNotFoundException {
        when(accountRepository.findById(any())).thenReturn(Optional.ofNullable(account));
        operationsService.deposit(ACCOUNT_IBAN, new BigDecimal(1000), "depot");

        assertEquals(new BigDecimal(2000), account.getBalance());

        verify(transactionRepository, times(1)).save(any());
        verify(accountRepository, times(1)).save(any());
    }

    @Test
    void depositWithFailed_InvalidOperation() {
        InvalidOperationException exception = assertThrows(InvalidOperationException.class, () -> {
            operationsService.deposit(ACCOUNT_IBAN, new BigDecimal(-1000), "depot");
        });
        String expectedMessage = "Transaction amount must be greater than 0 !";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    void depositWithFailed_InvalidAccount() {
        when(accountRepository.findById(any())).thenReturn(Optional.empty());
        AccountNotFoundException exception = assertThrows(AccountNotFoundException.class, () -> {
            operationsService.deposit(ACCOUNT_IBAN, new BigDecimal(1000), "depot");
        });
        String expectedMessage = "Account not found !";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    void withdrawWithSuccess() throws InvalidOperationException, AccountNotFoundException {

        when(accountRepository.findById(any())).thenReturn(Optional.ofNullable(account));

        operationsService.withdraw(ACCOUNT_IBAN, new BigDecimal(500), "retrait");

        assertEquals(new BigDecimal(1500), account.getBalance());

        verify(transactionRepository, times(1)).save(any());
        verify(accountRepository, times(1)).save(any());
    }

    @Test
    void withdrawn_failed_Overdraft() {
        when(accountRepository.findById(any())).thenReturn(Optional.ofNullable(account));
        InvalidOperationException exception = assertThrows(InvalidOperationException.class, () -> {
            operationsService.withdraw(ACCOUNT_IBAN, new BigDecimal(2000), "retrait");
        });
        String expectedMessage = "Overdraft not authorised";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    void printHistory() throws AccountNotFoundException, InvalidOperationException {

        //Stubbing Transaction
        account.setTransactionList(List.of(new Transaction().builder()
                .type(TransactionType.DEPOSIT)
                .date(LocalDateTime.now())
                .availableBalance(new BigDecimal(2000))
                .build()));
        when(accountRepository.findById(any())).thenReturn(Optional.ofNullable(account));

        String history = operationsService.printHistory(ACCOUNT_IBAN);
        assertNotNull(history);
        assertTrue(history.contains(String.format("%-20s\t%-15s\t%10s\t%15s\t%15s\t%15s\n", "Date", "Operation", "Status", "Amount", "Balance", "Description")));
    }

    @Test
    void printStatement() throws AccountNotFoundException {
        //Stubbing Transaction
        account.setTransactionList(List.of(new Transaction().builder()
                .type(TransactionType.DEPOSIT)
                .date(LocalDateTime.now())
                .availableBalance(new BigDecimal(2000))
                .build()));
        when(accountRepository.findById(any())).thenReturn(Optional.ofNullable(account));

        String history = operationsService.printStatement(ACCOUNT_IBAN);
        assertNotNull(history);
        assertTrue(history.contains(String.format("%-20s\t%-15s\n", "Date", "Balance")));
    }
}
