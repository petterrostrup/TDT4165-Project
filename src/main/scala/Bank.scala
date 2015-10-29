import java.util.concurrent.atomic.AtomicInteger
import exceptions._
object Bank {
  
  private var idCounter: AtomicInteger = new AtomicInteger

	def transaction(from: Account, to: Account, amount: Double): Unit = {
		if (amount > 0) {
			from.withdraw(amount);
			to.deposit(amount);
		} 
		else
		  throw new IllegalAmountException
	}

	def getUniqueId: Integer = {
		idCounter.incrementAndGet
	}
  
}