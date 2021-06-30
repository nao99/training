package com.luxoft.atm;

import com.luxoft.atm.domain.ATMService;
import com.luxoft.atm.domain.ATMServiceImpl;
import com.luxoft.atm.domain.model.Denomination;
import com.luxoft.atm.domain.model.atm.ATM;
import com.luxoft.atm.domain.model.atm.ATMException;
import com.luxoft.atm.domain.model.atm.DefaultATM;
import com.luxoft.atm.domain.model.banknote.Banknote;
import com.luxoft.atm.domain.model.banknote.BanknotesBox;
import com.luxoft.atm.domain.model.banknote.DefaultBanknotesBox;

import java.util.HashSet;
import java.util.Set;

/**
 * DemonstrationApplication class
 *
 * @author  Nikolai Osipov <nao99.dev@gmail.com>
 * @version 1.0.0
 * @since   2021-06-29
 */
public class DemonstrationApplication {
    public static void main(String[] args) {
        // 1. Create boxes for banknotes
        var banknoteBox1 = DefaultBanknotesBox.builder()
            .denomination(Denomination.DENOMINATION_2000)
            .build();

        var banknoteBox2 = DefaultBanknotesBox.builder()
            .denomination(Denomination.DENOMINATION_1000)
            .build();

        var banknoteBox3 = DefaultBanknotesBox.builder()
            .denomination(Denomination.DENOMINATION_5000)
            .build();

        var banknoteBox4 = DefaultBanknotesBox.builder()
            .denomination(Denomination.DENOMINATION_500)
            .build();

        Set<BanknotesBox> banknoteBoxes = new HashSet<>() {{
            add(banknoteBox1);
            add(banknoteBox2);
            add(banknoteBox3);
            add(banknoteBox4);
        }};

        // 2. Create an ATM
        ATM atm = DefaultATM.builder()
            .banknoteBoxes(banknoteBoxes)
            .build();

        // 3. Create an ATMService
        ATMService service = new ATMServiceImpl();

        // 4. Check ATM balance
        var atmBalance = service.checkBalance(atm);
        System.out.printf("ATM Balance: %d%n", atmBalance);

        // 5. Deposit some banknotes to this ATM
        Set<Banknote> banknotes = new HashSet<>() {{
            var banknote1 = Banknote.builder()
                .denomination(Denomination.DENOMINATION_5000)
                .build();

            var banknote2 = Banknote.builder()
                .denomination(Denomination.DENOMINATION_5000)
                .build();

            var banknote3 = Banknote.builder()
                .denomination(Denomination.DENOMINATION_100)
                .build();

            add(banknote1);
            add(banknote2);
            add(banknote3);
        }};

        var nonDepositedBanknotes = service.deposit(banknotes, atm);
        System.out.printf("Non deposited banknotes: %s", nonDepositedBanknotes);

        // 6. Check ATM balance again
        atmBalance = service.checkBalance(atm);
        System.out.printf("ATM Balance: %d%n", atmBalance);

        // 7. Try to withdraw 500 from the ATM
        try {
            var withdrawnBanknotes = service.withdraw(500, atm);
            System.out.println(withdrawnBanknotes);
        } catch (ATMException e) {
            System.out.println(e.getMessage());
        }

        // 8. Check ATM balance again
        atmBalance = service.checkBalance(atm);
        System.out.printf("ATM Balance: %d%n", atmBalance);

        // 9. Withdraw 5000 from the ATM
        var withdrawnBanknotes = service.withdraw(5000, atm);
        System.out.println(withdrawnBanknotes);

        // 10. Check ATM balance again
        atmBalance = service.checkBalance(atm);
        System.out.printf("ATM Balance: %d%n", atmBalance);
    }
}
