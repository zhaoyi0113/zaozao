package com.education.db;

import org.hibernate.SQLQuery;
import org.hibernate.Session;

/**
 * Created by yzzhao on 11/1/15.
 */
public class DBUtil {

    public static void clearTable(Session session, String tableName){
        session.beginTransaction();
        SQLQuery sqlQuery = session.createSQLQuery("delete from " + tableName);
        sqlQuery.executeUpdate();
        session.getTransaction().commit();
    }
}
