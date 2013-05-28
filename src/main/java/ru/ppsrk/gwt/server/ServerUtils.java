package ru.ppsrk.gwt.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.dozer.DozerBeanMapper;
import org.hibernate.Session;

import ru.ppsrk.gwt.client.ClientAuthenticationException;
import ru.ppsrk.gwt.client.LogicException;

public class ServerUtils {

    private static DozerBeanMapper mapper = new DozerBeanMapper();

    public static void setMappingFiles(List<String> files){
        mapper.setMappingFiles(files);
    }
    
    public static <ST, DT> List<DT> mapArray(Collection<ST> list, Class<DT> destClass) {
        List<DT> result = new ArrayList<DT>();
        for (ST elem : list) {
            if (elem != null)
                result.add(mapper.map(elem, destClass));
        }
        return result;
    }

    public static <T> T mapModel(Object value, Class<T> classDTO) {
        if (value == null)
            try {
                return classDTO.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        return mapper.map(value, classDTO);
    }

    public static void printStackTrace() {
        System.out.println("--------------------------");
        for (StackTraceElement ste : Thread.currentThread().getStackTrace()) {
            System.out.println(ste);
        }
        System.out.println("--------------------------");
    }

    protected static void cleanup() {
        System.out.println("Cleaning up ServerUtils...");
        mapper.destroy();
        mapper = null;
    }

    public static String getParamFromReq(HttpServletRequest req, String paramName) {
        if (req.getParameter(paramName) == null) {
            return "";
        } else {
            return req.getParameter(paramName);
        }
    }

    public static List<String> ean13(String code) {
        if (code.length() == 12) { // calc 13th number
            int n = 0, sum = 0;
            for (char c : code.toCharArray()) {
                sum += (c - 48) * (n++ % 2 == 1 ? 3 : 1);
            }
            code += (10 - sum % 10) % 10;
        } else if (code.length() != 13) {
            return new ArrayList<String>();
        }

        List<String> schemas = Arrays.asList("LLLLLL", "LLGLGG", "LLGGLG", "LLGGGL", "LGLLGG", "LGGLLG", "LGGGLL", "LGLGLG", "LGLGGL", "LGGLGL");
        ArrayList<String> elements = new ArrayList<String>();
        elements.add("S");
        int pos = 0;
        for (char c : code.substring(1, 7).toCharArray()) {
            elements.add(schemas.get(code.charAt(0) - 48).charAt(pos++) + String.valueOf(c));
        }
        elements.add("SM");
        for (char c : code.substring(7, 13).toCharArray()) {
            elements.add("R" + String.valueOf(c));
        }
        elements.add("S");
        return elements;
    }

    // public static void startTestCopyDB(String originDBConfig, String
    // targetDBConfig, List<String> classnames) {
    // HibernateUtil.initSessionFactory(targetDBConfig);
    // HibernateUtil.initSessionFactory(originDBConfig);
    // Factory<SecurityManager> factory = new
    // IniSecurityManagerFactory("classpath:shiro.ini");
    // SecurityManager sm = factory.getInstance();
    // ThreadContext.bind(sm);
    // StatelessSession session1 =
    // HibernateUtil.getSessionFactory(0).openStatelessSession();
    // session1.beginTransaction();
    // session1.createSQLQuery("SET DATABASE REFERENTIAL INTEGRITY FALSE").executeUpdate();
    // for (String classname : classnames) {
    // Session session2 = HibernateUtil.getSessionFactory(1).openSession();
    // session2.beginTransaction();
    // System.out.println("Copying " + classname);
    // @SuppressWarnings("rawtypes")
    // List items = session2.createQuery("from " + classname).list();
    // session2.getTransaction().commit();
    // session2.close();
    // for (Object object : items) {
    // // object = session1.save(object);
    // session1.insert(object);
    // // session1.merge(object);
    // }
    // }
    // session1.createSQLQuery("SET DATABASE REFERENTIAL INTEGRITY TRUE").executeUpdate();
    // session1.getTransaction().commit();
    // session1.close();
    // }

    public static void resetTables(final String[] tables) throws LogicException, ClientAuthenticationException {
        HibernateUtil.exec(new HibernateCallback<Void>() {

            @Override
            public Void run(Session session) throws LogicException, ClientAuthenticationException {
                session.createSQLQuery("SET DATABASE REFERENTIAL INTEGRITY FALSE").executeUpdate();
                for (String table : tables) {
                    session.createSQLQuery("truncate table " + table + " restart identity").executeUpdate();
                }
                session.createSQLQuery("SET DATABASE REFERENTIAL INTEGRITY TRUE").executeUpdate();
                return null;
            }
        });

    }
}