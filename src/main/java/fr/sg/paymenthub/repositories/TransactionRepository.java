package fr.sg.paymenthub.repositories;

import fr.sg.paymenthub.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
}
