import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class FirstExample {

	public static void main(String[] args) throws Exception {
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		DataStream<String> rawSource = env.fromCollection(generateRandomStrings());
		DataStream<String> countSource = rawSource.map(x -> x + ": " + x.length());

		countSource.print();

		env.execute();
	}

	private static Collection<String> generateRandomStrings() {
		Collection<String> retVal = new ArrayList<>();
		Random random = new Random(13);
		for (int i = 0; i < 5000; i++) {
			StringBuilder builder = new StringBuilder();
			for (int j = 0; j < random.nextInt(20); j++) {
				char newChar = (char)(97 + random.nextInt(26));
				builder.append(newChar);
			}
			retVal.add(builder.toString());
		}
		return retVal;
	}

}
