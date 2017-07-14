package lhk.bdssp;

import com.datastax.driver.core.*;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by dmitry on 3/15/17.
 */
public class Application10M {

    private static Cluster cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
    private static Session session = cluster.connect("bdssp");

    private static char[] vals = {'a', 'b', 'c', 'd', 'e', 'f', 'j', 'k', 'l', 'm',  };

    public static void main(String[] args)
    {
        Integer idx = 0;

        long startTimeInsert = System.currentTimeMillis();

        for (int i = 0 ; i < 1_000/*_000*/ ; ++i) {
            for (int symbol = 0 ; symbol < vals.length ; ++symbol){

                Insert insert = QueryBuilder.insertInto ( "bdssp", "filedata" );

                insert.value("id", idx++);
                insert.value("filename", StringUtils.repeat(vals[symbol], 10));
                insert.value("filetext", StringUtils.repeat(vals[symbol], 1000));

                ResultSet result = session.execute(insert.toString());
            }
        }

        long endTimeInsert = System.currentTimeMillis();

        System.out.println("Insert time, ms: " + ( endTimeInsert - startTimeInsert ) );
        System.out.println("Insert count: " + idx );

        long startTimeSelect = System.currentTimeMillis();

        Statement selectQuery = QueryBuilder.select().from("bdssp", "filedata").where(QueryBuilder.eq("id",1));

        ResultSet results= session.execute(selectQuery);

        for(Row r:results.all()){
            System.out.println(r.toString());
        }

        long stopTimeSelect = System.currentTimeMillis();

        System.out.println("Select time, ms: " + ( stopTimeSelect - startTimeSelect ) );

        long startTimeUpdate = System.currentTimeMillis();

        Statement updateQuery = QueryBuilder.update("bdssp", "filedata")
                .with(QueryBuilder.set("filetext", StringUtils.repeat('X', 1000)))
                .where(QueryBuilder.eq("id", 1));
        session.execute(updateQuery);

        long stopTimeUpdate = System.currentTimeMillis();

        System.out.println("Update time, ms: " + ( stopTimeUpdate - startTimeUpdate ) );

        startTimeSelect = System.currentTimeMillis();

        selectQuery = QueryBuilder.select().from("bdssp", "filedata").where(QueryBuilder.eq("id",1));

        results= session.execute(selectQuery);

        for(Row r:results.all()){
            System.out.println(r.toString());
        }

        stopTimeSelect = System.currentTimeMillis();

        System.out.println("Select again time, ms: " + ( stopTimeSelect - startTimeSelect ) );
        session.close();
        cluster.close();
    }
}
