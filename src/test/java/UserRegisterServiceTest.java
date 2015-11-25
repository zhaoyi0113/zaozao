import com.education.db.DBConnection;
import com.education.db.DBUtil;
import com.education.ws.ResponseStatus;
import com.education.ws.UserQuery;
import com.education.ws.UserRegisterService;
import com.education.ws.UserRegisterBean;
import org.hibernate.Session;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by yzzhao on 11/1/15.
 */
public class UserRegisterServiceTest {

    @Before
    public void before(){
        System.setProperty("HIBERNATE_CFG_FILE", "hibernate_test.cfg.xml");
        Session session = DBConnection.getCurrentSession();
        DBUtil.clearTable(session, "user");
    }

    @Test
    @Ignore
    public void testRegisterUser(){
        UserRegisterService register = new UserRegisterService();
        UserRegisterBean bean = new UserRegisterBean();
        bean.setAge(10);
        bean.setUserName("张三");
        bean.setPassword("123456");
        bean.setGender("1");
        register.registerNewUser(bean);

        UserQuery query = new UserQuery();
        List<UserRegisterBean> users = query.getAllUsers();
        Assert.assertEquals(1, users.size());
    }

    @Test
    public void testQueryUser(){
        UserQuery query = new UserQuery();
        List<UserRegisterBean> allUsers = query.getAllUsers();
        Assert.assertEquals(0, allUsers.size());
    }

    @Test
    public void testRegisterExistedUser(){
        UserRegisterService register = new UserRegisterService();
        UserRegisterBean bean = new UserRegisterBean();
        bean.setAge(10);
        bean.setUserName("张三");
        bean.setPassword("123456");
        bean.setGender("1");
        ResponseStatus status = register.registerUser(bean);
        Assert.assertEquals(ResponseStatus.SUCCESS, status);

        status = register.registerUser(bean);
        Assert.assertEquals(ResponseStatus.USER_EXISTED, status);

    }
}
