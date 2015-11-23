
import com.education.db.DBConnection;
import com.education.db.DBUtil;
import com.education.ws.LoginService;
import com.education.ws.UserRegisterBean;
import com.education.ws.UserRegisterService;
import org.hibernate.Session;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * Created by yzzhao on 11/1/15.
 */

@ContextConfiguration(locations = {"classpath*:applicationContext.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class LoginServiceTest {

    @Autowired
    private UserRegisterService register;

    @Autowired
    private LoginService login;

//    @Before
//    public void before() {
//        System.setProperty("HIBERNATE_CFG_FILE", "hibernate_test.cfg.xml");
//        Session session = DBConnection.getCurrentSession();
//        DBUtil.clearTable(session, "user");
//    }

    @Test
    public void loginTest() {
        UserRegisterBean bean = new UserRegisterBean();
        bean.setAge(10);
        String user = "张三";
        bean.setUserName(user);
        String password = "123456";
        bean.setPassword(password);
        bean.setGender("1");
        register.registerNewUser(bean);
        boolean ret = login.validateUser(user, password);
        Assert.assertTrue(ret);
        ret = login.validateUser(user, "aaaaa");
        Assert.assertFalse(ret);
    }

    @Test
    public void failedLoginTest() {
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
