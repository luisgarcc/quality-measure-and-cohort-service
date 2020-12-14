import java.util.Properties;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;

public class SecondExample {
	public static void main(String[] args) throws Exception {
		Properties properties = new Properties();
		properties.setProperty("bootstrap.servers", "localhost:9092");
		properties.setProperty("group.id", "test");

		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		FlinkKafkaConsumer consumer = new FlinkKafkaConsumer<>("flink-test-input", new SimpleStringSchema(), properties);
		consumer.setStartFromEarliest();

		DataStream<String> stream = env
				.addSource(consumer);

		FlinkKafkaProducer<String> myProducer = new FlinkKafkaProducer<>(
				"flink-test-output",                  // target topic
				new SimpleStringSchema(),    // serialization schema
				properties);                  // producer config

		stream.addSink(myProducer);
	}
}
