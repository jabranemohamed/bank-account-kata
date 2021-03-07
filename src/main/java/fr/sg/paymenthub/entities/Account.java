package fr.sg.paymenthub.entities;

import fr.sg.paymenthub.entities.enums.AccountState;
import fr.sg.paymenthub.entities.enums.AccountType;
import fr.sg.paymenthub.entities.enums.CurrencyCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Account {

    /**
     * Account id
     */
    @Id
    private String id;

    /**
     * Current account balance
     */
    private BigDecimal balance;

    /**
     * Customer ID , A customer can have multiple account in the same bank
     */
    private BigInteger customerID;

    /**
     * Current account currency (euro,dollar), used in paiement and transaction
     */
    @Enumerated(EnumType.STRING)
    private CurrencyCode currency;

    /**
     * Current Account Iban, used in transaction.It's also unique
     */
    private String iban;

    /**
     * Customer or internal Account belong to the back itself
     */
    @Enumerated(EnumType.STRING)
    private AccountType type;

    /**
     * Last modification date/time
     */

    private LocalDateTime lastModified;

    /**
     * Current account state (ACTIVE,CLOSED,INACTIVE) can be used in transaction to verify the account
     */
    @Enumerated(EnumType.STRING)
    private AccountState state;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Transaction> transactionList;

    public BigDecimal getBalance() {
        return balance == null ? BigDecimal.ZERO : balance;
    }
}
