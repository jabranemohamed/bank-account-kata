package fr.sg.paymenthub.repositories;

import fr.sg.paymenthub.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String>  {
}
