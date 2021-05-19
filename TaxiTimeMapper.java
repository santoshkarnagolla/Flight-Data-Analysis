import java.io.IOException;

import org.apache.hadoop.io.Text;

import org.apache.hadoop.mapreduce.Mapper;

public class TaxiTimeMapper extends Mapper<Object, Text, Text, Text> {

	@Override
	protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {

		String[] inf = value.toString().split(",");

		if (!"Year".equals(inf[0])) {

			if (!"NA".equals(inf[20])) {

				context.write(new Text(inf[16]), new Text(inf[20]));

			}

			if (!"NA".equals(inf[19])) {

				context.write(new Text(inf[17]), new Text(inf[19]));

			}

		}

	}

}
