import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

class SspMapper extends Mapper<Object, Text, IntWritable, SspData> {

    private static Optional<SspData> parseLine (String fileLine){
        String[] lineParts = fileLine.split(" ");

        if( null == lineParts
                || lineParts.length != 5
                || Arrays.stream(lineParts).anyMatch(part -> part.length() == 0) ) {

            return Optional.empty();
        }

        try {
            Integer id = Integer.parseInt(lineParts[2]);
            Float total = Float.parseFloat(lineParts[3]);
            String url = lineParts[4];

            return Optional.of(new SspData(id, total, url));

        }catch (Exception ex) {
            return Optional.empty();
        }
    }

    @Override
    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {

        Optional<SspData> data = parseLine( value.toString() );

        if(data.isPresent()) {
            context.write(new IntWritable (data.get().getId()), data.get());
        }
    }
}