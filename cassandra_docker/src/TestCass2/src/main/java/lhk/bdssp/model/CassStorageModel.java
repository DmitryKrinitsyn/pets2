package lhk.bdssp.model;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.SimpleStatement;
import lhk.bdssp.beans.Book;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dmitry on 2/27/17.
 */
@Component
public class CassStorageModel implements StorageModel {

    private Cluster cluster1 = Cluster.builder().addContactPoint("127.0.0.1").build();
    private Session session = cluster1.connect("bdssp");

    @Override
    synchronized public StatusType storeBook(Book book) {

        try {
            SimpleStatement insertStatement =
                    new SimpleStatement("INSERT INTO filedata (id, filename, filetext) VALUES (1, ?, ?)", book.getName(), book.getText());

            session.execute(insertStatement);
        }
        catch (Throwable ex)
        {
            return StatusType.failed;
        }

        return StatusType.ok;
    }

    private List<Book> processStatementInternal(SimpleStatement statement){
        ResultSet resultSet = session.execute(statement);
        List<Book> result = new ArrayList<>();
        resultSet.forEach(row -> result.add(new Book(row.getString("filename"), row.getString("filetext"))));
        return result;
    }

    private SimpleStatement prepareStatement(String searchPattern, String columnName)
    {
        return new SimpleStatement("SELECT * FROM filedata WHERE expr(lucene_index, '{filter:[ {type:\"wildcard\", field:\""+columnName+"\", value:\""+searchPattern+"\"}]}')");
    }

    @Override
    synchronized public List<Book> searchBookByName(String searchPattern) {

        SimpleStatement selectStatement = prepareStatement(searchPattern, "filename");
        return processStatementInternal(selectStatement);
    }

    @Override
    synchronized public List<Book> searchBookByContent(String searchPattern) {
        SimpleStatement selectStatement = prepareStatement(searchPattern, "filetext");
        return processStatementInternal(selectStatement);
    }
}
