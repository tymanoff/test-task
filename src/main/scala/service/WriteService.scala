package service

import constant.ConstantString.{NEWLINE, SEPARATOR}
import model.Client
import org.slf4j.LoggerFactory

import java.io.{File, PrintWriter}
import scala.util.{Failure, Success, Using}

object WriteService {
  private val logger = LoggerFactory.getLogger(this.getClass)

  def writeResult(filename: String, clients: Map[String, Client]): Unit = {
    val result = Using(new PrintWriter(new File(filename))) { writer =>
      clients.values.map { client =>
        s"${client.name}$SEPARATOR${client.balanceUSD}$SEPARATOR${client.aBalance}$SEPARATOR${client.bBalance}$SEPARATOR${client.cBalance}$SEPARATOR${client.dBalance}$NEWLINE"
      }.foreach(writer.write)
    }

    result match {
      case Success(_) => logger.info("File written successfully.")
      case Failure(e) => logger.error(s"Failed to write file: ${e.getMessage}", e)
    }
  }
}
