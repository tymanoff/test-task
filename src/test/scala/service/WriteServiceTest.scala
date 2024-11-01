package service

import model.Client
import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import org.scalatest.matchers.must.Matchers.contain

import java.io.{BufferedReader, File, FileReader}

class WriteServiceTest extends AnyFunSuiteLike {

  test("writeResult should correctly write clients data to a file") {
    val tempFile = File.createTempFile("testWriteResult", ".txt")
    tempFile.deleteOnExit()

    val clients = Map(
      "C1" -> Client("C1", 1000, 10, 20, 30, 40),
      "C2" -> Client("C2", 2000, 15, 25, 35, 45)
    )

    WriteService.writeResult(tempFile.getAbsolutePath, clients)

    val reader = new BufferedReader(new FileReader(tempFile))
    val lines = Iterator.continually(reader.readLine()).takeWhile(_ != null).toList
    reader.close()

    lines should contain theSameElementsAs List(
      s"C1\t1000\t10\t20\t30\t40",
      s"C2\t2000\t15\t25\t35\t45"
    )

    assert(lines.nonEmpty, "The file should not be empty.")
  }
}
