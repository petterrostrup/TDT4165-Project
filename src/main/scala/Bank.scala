import java.util.concurrent.{ExecutorService, Executors}

import scala.concurrent.forkjoin.ForkJoinPool
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.{Executor, ExecutorService}

class Bank(val allowedAttempts: Integer = 3) {

  private val uid: AtomicInteger = new AtomicInteger
  private val transactionsQueue: TransactionQueue = new TransactionQueue()
  private val processedTransactions: TransactionQueue = new TransactionQueue()
  private val executorContext = ???

  def addTransactionToQueue(from: Account, to: Account, amount: Double): Unit = {
    transactionsQueue push new Transaction(
      transactionsQueue, processedTransactions, from, to, amount, allowedAttempts)
  }

  def generateAccountId: Int = {
    uid.incrementAndGet
  }

  private def processTransactions(): Unit = {
    
    while (true) {
      // This will block until a connection comes in.
      if (!transactionsQueue.isEmpty)
        {
          val transaction = transactionsQueue.pop
          transaction.run
        }
    }
  }

  def addAccount(initialBalance: Double): Account = {
    new Account(this, initialBalance)
  }

  def getProcessedTransactionsAsList: List[Transaction] = {
    processedTransactions.iterator.toList
  }

}
