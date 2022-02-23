package account_transfer;

import java.math.BigInteger;
import java.util.Objects;
import java.util.UUID;

/*
Live coding session with Revolut on 2022-02-22
 */
public class BalanceTransferService {

    void transfer(Account from, Account to, BigInteger amount) {

        if (amount.compareTo(BigInteger.ZERO) < 0) {
            throw new IllegalArgumentException("Transfer amount must not be negative");
        }

        /*
        Questions:
        1) If it were a DB what isolation level should suffice?
        2) If it were a DB what will be transactional boundaries?
        3) If it were a DB is it possible to achieve correct behavior without REPEATABLE_READ.
        Hint: pre-select with versioning can be out of tx scope.
         */
        synchronized (from.compareTo(to) < 0 ? from : to) {
            synchronized (from.compareTo(to) >= 0 ? to : from) {
                if (from.balance.compareTo(amount) < 0) {
                    throw new IllegalStateException("Insufficient funds");
                }
                from.subtract(amount);
                to.add(amount);
            }
        }

    }

    public static void main(String[] args) {

    }

    static class Account implements Comparable<Account>{
        private final UUID id;
        private BigInteger balance;

        public Account(final UUID id, final BigInteger balance) {
            this.id = id;
            this.balance = balance;
        }


        public void subtract(final BigInteger amount) {
            balance = balance.subtract(amount);
        }

        public void add(final BigInteger amount) {
            balance = balance.add(amount);
        }

        @Override
        public int compareTo(final Account account) {
            return this.id.compareTo(account.id);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final Account account = (Account) o;
            return Objects.equals(id, account.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }

}
