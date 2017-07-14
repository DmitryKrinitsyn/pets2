
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


class SspReducerJson extends Reducer<IntWritable, SspData,Text, NullWritable> {

    @Override
    public void reduce(IntWritable key, Iterable<SspData> values, Context context) throws IOException, InterruptedException {

        Float total = 0f;
        List<String> urls = new ArrayList<>();

        for (SspData value: values) {
            total += value.getTotal();
            urls.add( value.getUrl() );
        }

        String result = "{"+ "\"id\":" + key.get()
                + ",\"total\":" + total
                + ",\"stores\":[\"" + String.join("\",\"", urls ) + "\"]}";

        context.write(new Text(result), null);
    }
}


