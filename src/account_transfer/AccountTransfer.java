package account_transfer;

import java.util.UUID;

public class AccountTransfer {

    public void transfer(Account from, Account to, Long amount) {

        synchronized (from.compareTo(to) > 0 ? from : to) {
            synchronized (from.compareTo(to) <= 0 ? from : to) {
                if (from.amount < amount) {
                    throw new IllegalStateException();
                }
                from.amount -= amount;
                to.amount += amount;
            }
        }
    }

    static class Account implements Comparable<Account> {
        private final UUID id;
        private Long amount;

        Account(final UUID id) {
            this.id = id;
        }

        @Override
        public int compareTo(final Account o) {
            return this.id.compareTo(o.id);
        }
    }
}
