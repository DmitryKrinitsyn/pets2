
import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;

import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


class SspReducerParquet extends Reducer<IntWritable, SspData, Void, GenericRecord> {

    public static final Schema SCHEMA = new Schema.Parser().parse( "{" +
                    "  \"type\": \"record\", " +
                    "  \"name\": \"stat\", " +
                    "  \"fields\": [" +
                    "       {\"name\": \"id\", \"type\": \"long\"}," +
                    "       {\"name\": \"total\", \"type\": \"float\"}," +
                    "       {\"name\": \"stores\", \"type\": \"string\"}" +
                    "    ] "+
            "}");


    //private static GraphiteLogger logger = new GraphiteLogger("172.18.0.10", 2003);

    private static final MetricRegistry metrics = new MetricRegistry();

    private static Integer length = 0;

    private static void startReport() {

        final Graphite graphite = new Graphite(new InetSocketAddress("172.18.0.10", 2003));
        final GraphiteReporter reporter2 = GraphiteReporter.forRegistry(metrics)
                .prefixedWith("ssp1")
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .filter(MetricFilter.ALL)
                .build(graphite);

        reporter2.start(1, TimeUnit.SECONDS);


        metrics.register( MetricRegistry.name(SspReducerParquet.class, "reducer.values", "length"),
                new Gauge<Integer>() {
                    @Override
                    public Integer getValue() {
                        return length;
                    }
                });
    }

    @Override
    public void reduce(IntWritable key, Iterable<SspData> values, Context context) throws IOException, InterruptedException {

        startReport();

        Float total = 0f;
        List<String> urls = new ArrayList<>();

        length = 0;

        for (SspData value: values) {
            total += value.getTotal();
            urls.add( value.getUrl() );
            ++length;
        }

        //logger.logToGraphite("reducer.values.length", i);

        GenericRecord record = new GenericData.Record(SCHEMA);

        record.put("id", key.get());
        record.put("total", total);
        record.put("stores", "\"" +String.join("\",\"", urls )+"\"");

        context.write(null, record);
    }
}


