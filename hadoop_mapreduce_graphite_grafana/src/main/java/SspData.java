import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class SspData implements Writable {

    private Integer id;
    private Float total;
    private String url;

    public SspData(){
    }

    public SspData(Integer id, Float total, String url){
        this.id = id;
        this.total = total;
        this.url = url;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    @Override
    public String toString(){
        return "SspData( " + id +" , " + total + ", " + url + " )";
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(id);
        dataOutput.writeFloat(total);
        dataOutput.writeBytes(url);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        id = dataInput.readInt();
        total = dataInput.readFloat();
        url = dataInput.readLine();
    }

    public static SspData read(DataInput in) throws IOException {
        SspData data = new SspData();
        data.readFields(in);
        return data;
    }
}
