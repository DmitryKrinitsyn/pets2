import lhk.bdssp.beans.Book;
import lhk.bdssp.Application;



import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment=SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CassIntegrationTest {

    private void upload(String filePath) throws Exception {
        HttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

        HttpPost httppost = new HttpPost("http://localhost:8080/cass/upload");
        File file = new File(filePath);

        MultipartEntity mpEntity = new MultipartEntity();
        ContentBody cbFile = new FileBody(file);
        mpEntity.addPart("file", cbFile);


        httppost.setEntity(mpEntity);
        System.out.println("executing request " + httppost.getRequestLine());
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity resEntity = response.getEntity();

        System.out.println(response.getStatusLine());
        if (resEntity != null) {
            System.out.println(EntityUtils.toString(resEntity));
        }
        if (resEntity != null) {
            resEntity.consumeContent();
        }

        httpclient.getConnectionManager().shutdown();
    }

    @Test
    public void testUpload() throws Exception {

        upload("/Users/dmitry/Documents/bigdata/ssp1/aaa.txt");
        upload("/Users/dmitry/Documents/bigdata/ssp1/bbb.txt");

    }

    @Test
    public void testGetByName() {
        RestTemplate template = new RestTemplate();

        ArrayList resultList =
                template.getForObject("http://127.0.0.1:8080/cass/getbyname/aa*", ArrayList.class);

        resultList.forEach(System.out::println);

        Assert.assertTrue(resultList.size() > 0);
    }

    @Test
    public void testGetByText() {
        RestTemplate template = new RestTemplate();

        ArrayList resultList =
                template.getForObject("http://127.0.0.1:8080/cass/getbytext/*yyy*", ArrayList.class);

        resultList.forEach(System.out::println);

        Assert.assertTrue(resultList.size() > 0);
    }
}

