object Doobie1 {

  import doobie._
  import doobie.implicits._
  import doobie.util.ExecutionContexts
  import cats._
  import cats.data._
  import cats.effect._
  import cats.implicits._
  import fs2.Stream

  // We need a ContextShift[IO] before we can construct a Transactor[IO]. The passed ExecutionContext
  // is where nonblocking operations will be executed. For testing here we're using a synchronous EC.
  implicit val cs = IO.contextShift(ExecutionContexts.synchronous)

  // A transactor that gets connections from java.sql.DriverManager and executes blocking operations
  // on an our synchronous EC. See the chapter on connection handling for more info.
  val xa = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver",     // driver classname
    "jdbc:postgresql://127.0.0.1:5432/scala-crud",     // connect URL (driver-specific)
    "postgres",                  // user
    "123456",                          // password
    Blocker.liftExecutionContext(ExecutionContexts.synchronous) // just for testing
  )

  def main(args: Array[String]): Unit = {
//    s"jdbc:postgresql://${properties.serverName}:${properties.portNumber}/${properties.databaseName}"
    sql"select username from users"
      .query[String]    // Query0[String]
      .to[List]         // ConnectionIO[List[String]]
      .transact(xa)     // IO[List[String]]
      .unsafeRunSync    // List[String]
      .take(5)          // List[String]
      .foreach(println) // Unit
  }
}
