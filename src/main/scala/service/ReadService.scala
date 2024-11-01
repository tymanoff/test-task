package service

import constant.ConstantString.SEPARATOR
import model.OrderType.{BUY, OrderType, SELL}
import model.StockType.StockType
import model.{Client, Order, StockType}
import org.slf4j.LoggerFactory

import scala.io.Source
import scala.util.{Failure, Success, Using}

object ReadService {
  private val logger = LoggerFactory.getLogger(this.getClass)

  def readClients(filename: String): Map[String, Client] = {
    logger.info(s"Reading clients from file: $filename")
    val result = Using(Source.fromFile(filename)) { source =>
      source.getLines().map { line =>
        val Array(name, balanceUSD, aBalance, bBalance, cBalance, dBalance) = line.split(SEPARATOR)
        name -> Client(name, balanceUSD.toInt, aBalance.toInt, bBalance.toInt, cBalance.toInt, dBalance.toInt)
      }.toMap
    }
    result match {
      case Success(clients) =>
        logger.info("Clients read successfully.")
        clients
      case Failure(e) =>
        logger.error(s"Failed to read file: $filename", e)
        Map.empty
    }
  }

  def readOrders(filename: String): List[Order] = {
    logger.info(s"Reading orders from file: $filename")
    val result = Using(Source.fromFile(filename)) { source =>
      source.getLines().map { line =>
        val Array(clientName, orderType, stock, price, quantity) = line.split(SEPARATOR)
        Order(clientName, fromOrderType(orderType), fromStockType(stock), price.toInt, quantity.toInt)
      }.toList
    }
    result match {
      case Success(orders) =>
        logger.info("Orders read successfully.")
        orders
      case Failure(e) =>
        logger.error(s"Failed to read file: $filename", e)
        List.empty
    }
  }

  private def fromOrderType(s: String): OrderType = s match {
    case "b" => BUY
    case "s" => SELL
    case _ =>
      logger.error(s"Unknown order type: $s")
      throw new IllegalArgumentException(s"Unknown order type: $s")
  }

  private def fromStockType(s: String): StockType = s match {
    case "A" => StockType.A
    case "B" => StockType.B
    case "C" => StockType.C
    case "D" => StockType.D
    case _ =>
      logger.error(s"Unknown stock type: $s")
      throw new IllegalArgumentException(s"Unknown stock type: $s")
  }
}
