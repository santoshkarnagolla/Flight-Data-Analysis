import java.io.IOException;

import org.apache.hadoop.io.IntWritable;

import org.apache.hadoop.io.Text;

import org.apache.hadoop.mapreduce.Mapper;

public class CancellationsMapper extends Mapper<Object, Text, Text, IntWritable> {

	@Override
	protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {

		String[] inf = value.toString().split(",");

		if (!"Year".equals(inf[0]) && ("1".equals(inf[21])&&!"NA".equals(inf[22])&&inf[22].trim().length()>0) ){

				context.write(new Text(inf[22]), new IntWritable(1));

			}

		}

	}


