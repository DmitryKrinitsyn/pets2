import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

class SspReducer extends Reducer<IntWritable, SspData,IntWritable, SspData> {

    @Override
    public void reduce(IntWritable key, Iterable<SspData> values, Context context) throws IOException, InterruptedException {

        Float total = 0f;
        String urls = "";

        for (SspData value: values) {
            total += value.getTotal();
            urls += value.getUrl();
        }

        context.write(key, new SspData(key.get(), total, urls));
    }
}