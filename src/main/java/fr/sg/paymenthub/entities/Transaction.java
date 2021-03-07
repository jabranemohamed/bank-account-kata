package fr.sg.paymenthub.entities;

import fr.sg.paymenthub.entities.enums.TransactionStatus;
import fr.sg.paymenthub.entities.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private LocalDateTime date =LocalDateTime.now();

    private String description;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @NotNull
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    private BigDecimal availableBalance;

}
