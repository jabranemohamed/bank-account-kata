package fr.sg.paymenthub;

import fr.sg.paymenthub.services.OperationsService;
import fr.sg.paymenthub.utilities.LoadData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import java.util.Arrays;

@SpringBootApplication
public class BankAccountKataApplication implements CommandLineRunner {

    @Autowired
    private Environment env;

    @Autowired
    private LoadData loadData;

    @Autowired
    private OperationsService operationsService;

    public static void main(String[] args) {
        SpringApplication.run(BankAccountKataApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        if (Arrays.stream(env.getActiveProfiles()).anyMatch(p -> p.contains("demo"))) {
            loadData.populateAccount();
            loadData.populateOperations();
            System.out.println("===== Current Account Balance ==== ");
            System.out.println(operationsService.printStatement(loadData.ACCOUNT_IBAN));

            System.out.println("===== My Account History ==== ");
            System.out.println(operationsService.printHistory(loadData.ACCOUNT_IBAN));

        }

    }
}
