/**
  *
  * 描述:
  *
  * @author hz.lei
  * @date 2018年04月27日 上午11:32
  */
object NumTest {

  def main(args: Array[String]): Unit = {
    /* println(getId(12))


     println(getId(1222))

     println(getId(122122))*/


    val res1 = getIdGraceful("1")
    val res2 = getIdGraceful("12")
    val res3 = getIdGraceful("124")
    val res4 = getIdGraceful("1234")
    val res5 = getIdGraceful("12345")
    val res6 = getIdGraceful("12346")
    println(res1)
    println(res2)
    println(res3)
    println(res4)
    println(res5)
    println(res6)
  }


  def getId(id: Int): String = {
    val array = Array("00000", "0000", "000", "00", "0", "")

    val num = id.toString

    val length: Int = num.length

    (array(length - 1) + num)

  }

  def getIdGraceful(code: String): String = {
    f"${code.toInt}%06d"
  }

}
