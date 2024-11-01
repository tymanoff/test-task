import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory
import service.{ProcessService, ReadService, WriteService}

object Main {
  private val logger = LoggerFactory.getLogger(this.getClass)

  def main(args: Array[String]): Unit = {
    logger.info("Starting application")

    logger.info("Reading configuration")
    val config = ConfigFactory.load()
    val clientsPath = config.getString("application.files.clients")
    val ordersPath = config.getString("application.files.orders")
    val resultPath = config.getString("application.files.result")

    val clients = ReadService.readClients(clientsPath)
    val orders = ReadService.readOrders(ordersPath)
    if (clients.isEmpty || orders.isEmpty) {
      logger.error("Failed to read input data")
    } else {
      val finalClients = ProcessService.processOrders(clients, orders)
      WriteService.writeResult(resultPath, finalClients)
      logger.info("Operation completed successfully")
    }
  }
}
