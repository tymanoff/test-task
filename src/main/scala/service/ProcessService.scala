package service

import model.{Client, Order, OrderType, StockType}

object ProcessService {

  def processOrders(clients: Map[String, Client], orders: List[Order]): Map[String, Client] = {
    var updatedClients = clients

    val buyOrders = scala.collection.mutable.ListBuffer(orders.filter(_.orderType == OrderType.BUY): _*)
    val sellOrders = scala.collection.mutable.ListBuffer(orders.filter(_.orderType == OrderType.SELL): _*)

    var i = 0
    while (i < buyOrders.length) {
      val buyOrder = buyOrders(i)
      var matchFound = false
      var j = 0
      while (j < sellOrders.length && !matchFound) {
        val sellOrder = sellOrders(j)
        if (buyOrder.stock == sellOrder.stock &&
          buyOrder.price == sellOrder.price &&
          buyOrder.quantity == sellOrder.quantity &&
          buyOrder.clientName != sellOrder.clientName) {

          updatedClients = updatedClients.updated(buyOrder.clientName, {
            val buyer = updatedClients(buyOrder.clientName)
            updateStockBalance(buyer, buyOrder.stock, buyOrder.quantity).copy(
              balanceUSD = buyer.balanceUSD - buyOrder.price * buyOrder.quantity
            )
          })

          updatedClients = updatedClients.updated(sellOrder.clientName, {
            val seller = updatedClients(sellOrder.clientName)
            updateStockBalance(seller, sellOrder.stock, -sellOrder.quantity).copy(
              balanceUSD = seller.balanceUSD + sellOrder.price * sellOrder.quantity
            )
          })

          sellOrders.remove(j)
          buyOrders.remove(i)
          if (i > 0) i -= 1
          matchFound = true
        } else {
          j += 1
        }
      }
      if (!matchFound) {
        i += 1
      }
    }

    updatedClients
  }

  private def updateStockBalance(client: Client, stock: StockType.Value, quantity: Int): Client = {
    stock match {
      case StockType.A => client.copy(aBalance = client.aBalance + quantity)
      case StockType.B => client.copy(bBalance = client.bBalance + quantity)
      case StockType.C => client.copy(cBalance = client.cBalance + quantity)
      case StockType.D => client.copy(dBalance = client.dBalance + quantity)
    }
  }
}
