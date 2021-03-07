package fr.sg.paymenthub.services.impl;

import fr.sg.paymenthub.entities.Account;
import fr.sg.paymenthub.entities.Transaction;
import fr.sg.paymenthub.entities.enums.TransactionStatus;
import fr.sg.paymenthub.entities.enums.TransactionType;
import fr.sg.paymenthub.exceptions.AccountNotFoundException;
import fr.sg.paymenthub.exceptions.InvalidOperationException;
import fr.sg.paymenthub.repositories.AccountRepository;
import fr.sg.paymenthub.repositories.TransactionRepository;
import fr.sg.paymenthub.services.OperationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Service
public class OperationsServiceImpl implements OperationsService {

    public static final String ACCOUNT_NOT_FOUND = "Account not found !";

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    @Override
    public void deposit(String account, BigDecimal amount, String description)
            throws InvalidOperationException, AccountNotFoundException {
        this.process(account, amount, TransactionType.DEPOSIT, description);
    }

    @Transactional
    @Override
    public void withdraw(String account, BigDecimal amount, String description) throws InvalidOperationException, AccountNotFoundException {
        this.process(account, amount, TransactionType.WITHDRAWAL, description);
    }

    @Override
    public String printHistory(String account) throws AccountNotFoundException {
        Account accountObj = accountRepository.findById(account).orElseThrow(() -> new AccountNotFoundException(ACCOUNT_NOT_FOUND));
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/mm/yyyy HH:mm:ss");
        String headers = String.format("%-20s\t%-15s\t%10s\t%15s\t%15s\t%15s%n", "Date", "Operation", "Status", "Amount", "Balance", "Description");
        StringBuilder sb = new StringBuilder(headers);
        String transactionListAsString = accountObj.getTransactionList()
                .stream()
                .map(t -> String.format("%-20s\t%-15s\t%10s\t%15s\t%15s\t%15s", dateFormatter.format(t.getDate()), t.getType(), t.getStatus(), t.getAmount(), t.getAvailableBalance(), t.getDescription()))
                .collect(Collectors.joining("\n"));
        sb.append(transactionListAsString);
        return sb.toString();
    }

    @Override
    public String printStatement(String account) throws AccountNotFoundException {
        Account accountObj = accountRepository.findById(account).orElseThrow(() -> new AccountNotFoundException(ACCOUNT_NOT_FOUND));
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/mm/yyyy HH:mm:ss");
        String headers = String.format("%-20s\t%-15s%n", "Date", "Balance");
        StringBuilder sb = new StringBuilder(headers);
        String formatedAccountBalance = String.format("%-20s\t%-15s", dateFormatter.format(accountObj.getLastModified()), accountObj.getBalance());
        sb.append(formatedAccountBalance);
        return sb.toString();
    }

    private void process(String account, BigDecimal amount, TransactionType transactionType, String description)
            throws InvalidOperationException, AccountNotFoundException {

        if (BigDecimal.ZERO.compareTo(amount) >= 0) {
            throw new InvalidOperationException(" Transaction amount must be greater than 0 !");
        }
        BigDecimal currentAmount = TransactionType.DEPOSIT.equals(transactionType) ? amount : amount.negate();
        Account accountObj = accountRepository.findById(account).orElseThrow(() -> new AccountNotFoundException(ACCOUNT_NOT_FOUND));
        BigDecimal expectedBalance = currentAmount.add(accountObj.getBalance());

        if (expectedBalance.signum() < 0) {
            throw new InvalidOperationException("Overdraft not authorised : " + accountObj.getBalance());
        }
        accountObj.setBalance(expectedBalance);
        accountObj.setLastModified(LocalDateTime.now());
        accountRepository.save(accountObj);

        Transaction transaction = new Transaction().builder()
                .account(accountObj)
                .amount(amount)
                .date(LocalDateTime.now())
                .availableBalance(accountObj.getBalance())
                .type(transactionType)
                .status(TransactionStatus.ACCEPTED)
                .description(description)
                .build();
        transactionRepository.save(transaction);

    }
}
