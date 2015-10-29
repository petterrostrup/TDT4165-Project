
object Main extends App {

  def thread(body: =>Unit): Thread = {
      val t = new Thread {
        override def run() = body
      }
      t.start
      t
    }
  
  val n = 2
  val accounts = for (i <- 0 to n) yield {new Account(Math.round(Math.random*1000): Double)}
	val rand = new java.util.Random(System.nanoTime());
	var randInt = 0;
	var randInt2 = 0;

	for (i <- 0 to n) {
		randInt = rand.nextInt(accounts.length)
		randInt2 = rand.nextInt(accounts.length)
		val t = thread(Bank.transaction(accounts(randInt), accounts(randInt2), Math.round(Math.random*50): Double))
	}

  	accounts.foreach{acc => println(acc.getBalanceAmount)}
  
  
}