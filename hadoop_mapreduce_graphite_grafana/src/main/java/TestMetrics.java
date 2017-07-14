
import com.codahale.metrics.*;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

public class TestMetrics {
static final MetricRegistry metrics = new MetricRegistry();

    private static Integer value = 1;

    public static void main(String args[]) throws Throwable {
        startReport();
        /*
        Meter requests = metrics.meter("requests");
        //requests.mark();

        requests.mark(20);
        requests.mark(30);
        Thread.sleep(1000);
        requests.mark(40);
        */





        metrics.register( MetricRegistry.name(TestMetrics.class, "value", "size"),
                new Gauge<Integer>() {
                    @Override
                    public Integer getValue() {
                        return value;
                    }
                });

        //wait5Seconds();


        Thread.sleep(1000);
        value = 2;

        Thread.sleep(1000);
        value = 3;

        Thread.sleep(1000);
        value = 4;

        Thread.sleep(1000);
        value = 5;

        Thread.sleep(1000);
        value = 6;


        Thread.sleep(1000);
        value = 2;

        Thread.sleep(1000);
        value = 3;

        Thread.sleep(1000);
        value = 4;

        Thread.sleep(1000);
        value = 5;

        Thread.sleep(1000);
        value = 6;

        Thread.sleep(1000);
        value = 7;

        Thread.sleep(1000);
        value = 2;

        Thread.sleep(1000);
        value = 3;

        Thread.sleep(1000);
        value = 4;

    }

    static void startReport() {
        ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();

        reporter.start(1, TimeUnit.SECONDS);

        final Graphite graphite = new Graphite(new InetSocketAddress("localhost", 2003));
        final GraphiteReporter reporter2 = GraphiteReporter.forRegistry(metrics)
                .prefixedWith("web1.example.com")
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .filter(MetricFilter.ALL)
                .build(graphite);

        reporter2.start(1, TimeUnit.SECONDS);


    }

    static void wait5Seconds() {
        try {
            Thread.sleep(5*1000);
        }
        catch(InterruptedException e) {}
    }
}