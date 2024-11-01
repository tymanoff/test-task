package model

import model.OrderType.OrderType
import model.StockType.StockType

case class Order(
                  clientName: String,
                  orderType: OrderType,
                  stock: StockType,
                  price: Int,
                  quantity: Int
                )
