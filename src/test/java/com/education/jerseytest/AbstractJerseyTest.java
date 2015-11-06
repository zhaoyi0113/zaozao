package com.education.jerseytest;

import com.education.db.DBConnection;
import com.education.db.DBUtil;
import org.glassfish.jersey.test.JerseyTest;
import org.hibernate.Session;

/**
 * Created by yzzhao on 11/6/15.
 */
public class AbstractJerseyTest extends JerseyTest {

    public AbstractJerseyTest(){
        config();
    }

    private void config(){
        System.setProperty("HIBERNATE_CFG_FILE", "hibernate_test.cfg.xml");
    }

    protected void clearTable(String tableName){
        try {
            Session session = DBConnection.getCurrentSession();
            DBUtil.clearTable(session, tableName);
        }finally {

        }
    }
}
