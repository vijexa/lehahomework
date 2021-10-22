package vijexa.lehahomework

import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.EitherValues
import java.time.Instant

class OrderSpec extends AnyFreeSpec with Matchers with EitherValues {
  "Order.fromCSV" - {
    "should return error for invalid CSV" - {
      "some gibberish" in {
        assert(Order.fromCSV("ssdfsd").isLeft)
      }

      "malformed csv" in {
        val csv = """|id,date,name,phone,delivery,orderTotal
                     |1,1620200844881,Ксения Стрелкова,+4 288 332 63 67""".stripMargin

        assert(Order.fromCSV(csv).isLeft)
      }

      "malformed headers" in {
        val csv = """|id,date,name,phone
                     |1,1620200844881,Ксения Стрелкова,+4 288 332 63 67,"163946, Республика Татарстан
                     |Ставрополь, 75072 Katelynn пл.",389999""".stripMargin

        assert(Order.fromCSV(csv).isLeft)
      }
    }

    "should correctly parse csv" - {
      "in simple cases" in {
        val csv = """|id,date,name,phone,delivery,orderTotal
                     |1,1620200844881,Ксения Стрелкова,+4 288 332 63 67,self-pickup,389999
                     |2,1633216223436,Вадим Давыдов,+7 453 377 74 45,self-pickup,214999
                     |3,1616317419346,Евгений Петров,+9 503 756 68 43,self-pickup,549999""".stripMargin

        val expected = List(
          Order(
            1,
            Instant.parse("2021-05-05T07:47:24.881Z"),
            "Ксения Стрелкова",
            "+4 288 332 63 67",
            "self-pickup",
            389999,
          ),
          Order(
            2,
            Instant.parse("2021-10-02T23:10:23.436Z"),
            "Вадим Давыдов",
            "+7 453 377 74 45",
            "self-pickup",
            214999,
          ),
          Order(
            3,
            Instant.parse("2021-03-21T09:03:39.346Z"),
            "Евгений Петров",
            "+9 503 756 68 43",
            "self-pickup",
            549999,
          ),
        )

        assert(Order.fromCSV(csv).value == expected)
      }

      "when there are commas and line separators in strings" in {
        val csv = """|id,date,name,phone,delivery,orderTotal
                     |1,1620200844881,Ксения Стрелкова,+4 288 332 63 67,"163946, Республика Татарстан
                     |Ставрополь, 75072 Katelynn пл.",389999
                     |2,1633216223436,Вадим Давыдов,+7 453 377 74 45,self-pickup,214999
                     |3,1616317419346,Евгений Петров,+9 503 756 68 43,"831558, Республика Мордовия
                     |Саратов, 416 Veda улица",549999""".stripMargin

        val expected = List(
          Order(
            1,
            Instant.parse("2021-05-05T07:47:24.881Z"),
            "Ксения Стрелкова",
            "+4 288 332 63 67",
            "163946, Республика Татарстан\nСтаврополь, 75072 Katelynn пл.",
            389999,
          ),
          Order(
            2,
            Instant.parse("2021-10-02T23:10:23.436Z"),
            "Вадим Давыдов",
            "+7 453 377 74 45",
            "self-pickup",
            214999,
          ),
          Order(
            3,
            Instant.parse("2021-03-21T09:03:39.346Z"),
            "Евгений Петров",
            "+9 503 756 68 43",
            "831558, Республика Мордовия\nСаратов, 416 Veda улица",
            549999,
          ),
        )

        assert(Order.fromCSV(csv).value == expected)
      }
    }
  }
}
