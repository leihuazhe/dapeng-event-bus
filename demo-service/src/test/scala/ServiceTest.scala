import com.today.api.demo.scala.request.{LoginUserRequest, RegisterUserRequest}
import com.today.api.demo.scala.service.UserService
import com.today.service.commons.test.SpringServiceTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

/**
  *
  * Desc: 测试类，在服务端测试代码使用
  * 只需要继承SpringServiceTest 即可
  *
  * author: maple
  * Date: 2018-01-12 12:48
  *
  */
class ServiceTest extends SpringServiceTest {


  @Autowired
  var service: UserService = _

  /**
    * 用户注册测试
    */
  @Test
  def test(): Unit = {
    service.registerUser(RegisterUserRequest("maple", "123456aa", "18507120992"))
  }
}
