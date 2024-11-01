package service

import model.{Client, Order, OrderType, StockType}
import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.should.Matchers

import java.io.{File, FileNotFoundException, PrintWriter}

class ReadServiceTest extends AnyFunSuiteLike with Matchers {

  test("ReadService should correctly read clients from a file") {
    val testFile = File.createTempFile("testClients", ".txt")
    val writer = new PrintWriter(testFile)
    writer.println("C1\t1000\t130\t240\t760\t320")
    writer.println("C2\t4350\t370\t120\t950\t560")
    writer.close()

    val clients = ReadService.readClients(testFile.getAbsolutePath)
    assert(clients.size === 2)
    assert(clients("C1") === Client("C1", 1000, 130, 240, 760, 320))
    assert(clients("C2") === Client("C2", 4350, 370, 120, 950, 560))

    testFile.delete()
  }

  test("ReadService should correctly read orders from a file") {
    val testFile = File.createTempFile("testOrders", ".txt")
    val writer = new PrintWriter(testFile)
    writer.println("C1\tb\tA\t10\t100")
    writer.println("C2\ts\tC\t14\t5")
    writer.close()

    val orders = ReadService.readOrders(testFile.getAbsolutePath)
    assert(orders.size === 2)
    assert(orders.head === Order("C1", OrderType.BUY, StockType.A, 10, 100))
    assert(orders(1) === Order("C2", OrderType.SELL, StockType.C, 14, 5))

    testFile.delete()
  }

  test("ReadService should return empty map for invalid data in clients file") {
    val testFile = File.createTempFile("testClientsInvalid", ".txt")
    val writer = new PrintWriter(testFile)
    writer.println("C1\t1000\t130\t240") // Incorrect format
    writer.close()

    val stringToClient = ReadService.readClients(testFile.getAbsolutePath)

    assert(stringToClient.isEmpty)
    testFile.delete()
  }

  test("ReadService should return empty map for invalid data in orders file") {
    val testFile = File.createTempFile("testClientsInvalid", ".txt")
    val writer = new PrintWriter(testFile)
    writer.println("C1\tb\tA\t10")
    writer.close()

    val orders = ReadService.readOrders(testFile.getAbsolutePath)

    assert(orders.isEmpty)
    testFile.delete()
  }
}
