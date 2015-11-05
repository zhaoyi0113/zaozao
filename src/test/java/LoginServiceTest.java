
import com.education.db.DBConnection;
import com.education.db.DBUtil;
import com.education.ws.LoginService;
import com.education.ws.UserRegisterBean;
import com.education.ws.UserRegisterService;
import org.hibernate.Session;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by yzzhao on 11/1/15.
 */
public class LoginServiceTest {

    @Before
    public void before() {
        System.setProperty("HIBERNATE_CFG_FILE", "hibernate_test.cfg.xml");
        Session session = DBConnection.getCurrentSession();
        DBUtil.clearTable(session, "user");
    }

    @Test
    public void loginTest() {
        UserRegisterService register = new UserRegisterService();
        UserRegisterBean bean = new UserRegisterBean();
        bean.setAge(10);
        String user = "张三";
        bean.setUserName(user);
        String password = "123456";
        bean.setPassword(password);
        bean.setGender("1");
        register.registerNewUser(bean);
        LoginService login = new LoginService();
        boolean ret = login.validateUser(user, password);
        Assert.assertTrue(ret);
        ret = login.validateUser(user, "aaaaa");
        Assert.assertFalse(ret);
    }

    @Test
    public void failedLoginTest() {
        LoginService login = new LoginService();
        boolean ret = login.validateUser("", "");
        Assert.assertFalse(ret);
        ret = login.validateUser(null, "");
        Assert.assertFalse(ret);
        ret = login.validateUser(null, null);
        Assert.assertFalse(ret);
        ret = login.validateUser("", null);
        Assert.assertFalse(ret);
    }


}
