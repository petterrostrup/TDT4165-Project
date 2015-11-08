import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.{Executor, ExecutorService}
import scala.concurrent.forkjoin.ForkJoinPool
import scala.concurrent.ExecutionContext
import scala.annotation.tailrec

class Bank(val allowedAttempts: Integer = 3) {

  private val uid: AtomicInteger = new AtomicInteger
  private val transactionsQueue: TransactionQueue = new TransactionQueue()
  private val processedTransactions: TransactionQueue = new TransactionQueue()
  private val executorContext = ExecutionContext.global

  def addTransactionToQueue(from: Account, to: Account, amount: Double): Unit = {
    transactionsQueue push new Transaction(
      transactionsQueue, processedTransactions, from, to, amount, allowedAttempts)
  }

  def generateAccountId: Int = {
    uid.incrementAndGet
  }

  Main.thread(processTransactions)
  @tailrec
  private def processTransactions: Unit = {
    executorContext.execute(transactionsQueue.pop)
    //Thread.sleep(10)

    processTransactions
  }

  def addAccount(initialBalance: Double): Account = {
    new Account(this, initialBalance)
  }

  def getProcessedTransactionsAsList: List[Transaction] = {
    processedTransactions.iterator.toList
  }

}
