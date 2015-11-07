
import exceptions._
import scala.collection.mutable.Queue

object TransactionStatus extends Enumeration {
  val SUCCESS, PENDING, FAILED = Value
}

class TransactionQueue {
  val queue = new Queue[Transaction]
  // Remove and return the first element from the queue
  def pop: Transaction = queue.dequeue();

  // Return whether the queue is empty
  def isEmpty: Boolean = queue.length == 0;

  // Add new element to the back of the queue
  def push(t: Transaction): Unit = queue.enqueue(t);

  // Return the first element from the queue without removing it
  def peek: Transaction = queue.front;

  // Return an iterator to allow you to iterate over the queue
  def iterator: Iterator[Transaction] = queue.toIterator;
}

class Transaction(val transactionsQueue: TransactionQueue,
                  val processedTransactions: TransactionQueue,
                  val from: Account,
                  val to: Account,
                  val amount: Double,
                  val allowedAttemps: Int) extends Runnable {

  var status: TransactionStatus.Value = TransactionStatus.PENDING

  override def run: Unit = {

    def doTransaction() = {
      from withdraw amount
      to deposit amount
    }
    val transaction = transactionsQueue.pop;
    var i = 0
    var transaction_passed = false;
    while((i < allowedAttemps) && (!transaction_passed)){
      try{
        if (from.uid < to.uid) from synchronized {
          to synchronized {
            doTransaction
          }
        } else to synchronized {
          from synchronized {
            doTransaction
          }
        }
        transaction_passed = true;

      } catch {
        case ex: IllegalAmountException => {
        }
        case ex: NoSufficientFundsException => {
        }
      }
      i = i + 1
    }

    if (transaction_passed){
      this.status = TransactionStatus.SUCCESS
    } else {
      this.status = TransactionStatus.FAILED
    }

    processedTransactions.push(transaction)
  }
}


