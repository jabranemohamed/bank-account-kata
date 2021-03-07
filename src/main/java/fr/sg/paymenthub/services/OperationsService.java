package fr.sg.paymenthub.services;


import fr.sg.paymenthub.exceptions.AccountNotFoundException;
import fr.sg.paymenthub.exceptions.InvalidOperationException;

import java.math.BigDecimal;

public interface OperationsService {

    void deposit(String account, BigDecimal amount, String description) throws InvalidOperationException, AccountNotFoundException;

    void withdraw(String account, BigDecimal amount, String description) throws InvalidOperationException, AccountNotFoundException;

    //It return string because we can imagine sending the output to some PDF,emails utility
    String printHistory(String account) throws AccountNotFoundException;

    String printStatement(String account) throws AccountNotFoundException;
}
