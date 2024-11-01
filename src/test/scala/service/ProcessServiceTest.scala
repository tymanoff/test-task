package service

import model.{Client, Order, OrderType, StockType}
import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.should.Matchers
import org.scalamock.scalatest.MockFactory

class ProcessServiceTest extends AnyFunSuiteLike with Matchers with MockFactory {

  test("Test order matching logic") {
    val clients = Map(
      "C1" -> Client("C1", 1000, 10, 10, 10, 10),
      "C2" -> Client("C2", 1000, 5, 5, 5, 5),
      "C3" -> Client("C3", 1000, 10, 10, 10, 10),
    )
    val orders = List(
      Order("C1", OrderType.BUY, StockType.A, 10, 10),
      Order("C2", OrderType.SELL, StockType.B, 5, 5),
      Order("C3", OrderType.SELL, StockType.C, 10, 10),
      Order("C2", OrderType.BUY, StockType.B, 5, 5),
      Order("C3", OrderType.SELL, StockType.A, 10, 10),
      Order("C3", OrderType.SELL, StockType.A, 10, 10),
      Order("C3", OrderType.SELL, StockType.A, 10, 10),
      Order("C1", OrderType.BUY, StockType.C, 10, 10),
      Order("C2", OrderType.BUY, StockType.B, 5, 5),
      Order("C3", OrderType.BUY, StockType.A, 10, 10),
      Order("C1", OrderType.SELL, StockType.A, 10, 10),
      Order("C1", OrderType.BUY, StockType.A, 10, 10),
      Order("C3", OrderType.SELL, StockType.A, 10, 10),
      Order("C2", OrderType.SELL, StockType.A, 10, 10),
      Order("C2", OrderType.SELL, StockType.A, 10, 10),
    )
    val result = ProcessService.processOrders(clients, orders)
    result("C1") shouldEqual Client("C1", 800, 20, 10, 20, 10)
    result("C2") shouldEqual Client("C2", 1000, 5, 5, 5, 5)
    result("C3") shouldEqual Client("C3", 1200, 0, 10, 0, 10)
  }
}
