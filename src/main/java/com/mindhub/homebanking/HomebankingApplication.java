package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@SpringBootApplication
public class HomebankingApplication {

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);

	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository, CardRepository cardRepository, PaymentRepository paymentRepository){
		return (args) -> {

			Client newClient1 = new Client("Melba", "Lorenzo","melba@mindhub.com", passwordEncoder.encode("melba"));
			clientRepository.save(newClient1);

			Account account1 = new Account("VIN001", AccountType.SAVING);
			newClient1.addAccount(account1);
			

			Transaction transaccion = new Transaction(TransactionType.CREDIT, 200, "Dinero recibido",(account1.getBalance()+200));
			account1.addTransaction(transaccion);
			Transaction transaccion2 = new Transaction(TransactionType.DEBIT, -30, "Pago de Servicios",(account1.getBalance()-30));
			transaccion2.setDate(LocalDateTime.now().minusDays(2));
			account1.addTransaction(transaccion2);
			

			accountRepository.save(account1);
			transactionRepository.save(transaccion);
			transactionRepository.save(transaccion2);


			Account account2 = new Account("VIN002", AccountType.CHECKING);
			account2.setCreationDate(LocalDateTime.now().plusDays(1));
			newClient1.addAccount(account2);
			

			transaccion = new Transaction(TransactionType.CREDIT, 100.34, "Venta", (account2.getBalance()+100.34));
			transaccion.setDate(transaccion.getDate().minusDays(2));
			Transaction transaccion33 = new Transaction(TransactionType.CREDIT, 80.34, "otra cosa", (account2.getBalance()+80.34));
			transaccion33.setDate(transaccion.getDate().minusDays(3));
			account2.addTransaction(transaccion);
			account2.addTransaction(transaccion33);
			transaccion2 = new Transaction(TransactionType.CREDIT, 50, "Sueldo",(account2.getBalance()+50));
			account2.addTransaction(transaccion2);
			accountRepository.save(account2);
			transactionRepository.save(transaccion);
			transactionRepository.save(transaccion2);
			transactionRepository.save(transaccion33);


		Client newClient2 = new Client("Maria", "Perez", "maria@mindhub.com", passwordEncoder.encode("maria"));
		clientRepository.save(newClient2);
		
		account1 = new Account("AA0001", AccountType.SAVING);
		newClient2.addAccount(account1);
		
			transaccion = new Transaction(TransactionType.CREDIT, 30.5, "operacion de crédito",(account1.getBalance()+30.5));
			account1.addTransaction(transaccion);
			
			transaccion2 = new Transaction(TransactionType.CREDIT, 500, "operacion de crédito",(account1.getBalance()+500));
			account1.addTransaction(transaccion2);
			
			Transaction transaccion4 = new Transaction(TransactionType.DEBIT, -10.5, "operacion de débito",(account1.getBalance()-10.5));
			account1.addTransaction(transaccion4);
			
		
		accountRepository.save(account1);
		transactionRepository.save(transaccion);
		transactionRepository.save(transaccion2);
		transactionRepository.save(transaccion4);
		

		 account2 = new Account("AA0002", AccountType.CHECKING);
		 account2.setCreationDate(LocalDateTime.now().plusDays(1));
			newClient2.addAccount(account2);
		 accountRepository.save(account2);

		//  loans

		 Set<Integer> payments1 = new HashSet<>();
		 payments1.add(12);
		 payments1.add(24);
		 payments1.add(36);
		 payments1.add(48);
		 payments1.add(60);
		 Loan newLoan1 = new Loan("Hipotecario",500000,payments1, 20);
		 loanRepository.save(newLoan1);

		Set<Integer> payments2 = new HashSet<>();
		 payments2.add(6);
		 payments2.add(12);
		 payments2.add(24);
		 Loan newLoan2 = new Loan("Personal",100000,payments2,30);
		 loanRepository.save(newLoan2);

		 Set<Integer> payments = new HashSet<>();
		 payments.add(6);
		 payments.add(12);
		 payments.add(24);
		 payments.add(36);
		 Loan newLoan = new Loan("Automotriz",300000,payments,40);
		 loanRepository.save(newLoan);

		//  client loans

		 ClientLoan prestamoAsignado = new ClientLoan(newClient1, newLoan1, 400000,60);
		 clientLoanRepository.save(prestamoAsignado);
		 
		 ClientLoan prestamoAsignado2 = new ClientLoan(newClient1, newLoan2, 50000,12);
		 clientLoanRepository.save(prestamoAsignado2);

		 //  cards
		 Card card1 = new Card(CardType.DEBIT, CardColor.GOLD, newClient1, "0000000000001234", 123);
		 cardRepository.save(card1);

		 Card card2 = new Card(CardType.CREDIT,CardColor.TITANIUM, newClient1, "9999999999999999",123);
			card2.setThruDate(LocalDateTime.now().minusDays(2));
		 cardRepository.save(card2);

		 ClientLoan prestamoAsignado3 = new ClientLoan(newClient2, newLoan2, 100000,24);
		 clientLoanRepository.save(prestamoAsignado3);
		 ClientLoan prestamoAsignado4 = new ClientLoan(newClient2, newLoan, 200000,36);
		 clientLoanRepository.save(prestamoAsignado4);

		 Card card4 = new Card(CardType.CREDIT, CardColor.SILVER,newClient2, "9876987698769876", 345);
		 cardRepository.save(card4);


		Client nuevoCliente = new Client("admin","admin","admin@admin", passwordEncoder.encode("admin"));
		clientRepository.save(nuevoCliente);

		Payment payment = new Payment(100, "prueba", card1, LocalDateTime.now(), "VIN001");
		paymentRepository.save(payment);
		};


	}
}
