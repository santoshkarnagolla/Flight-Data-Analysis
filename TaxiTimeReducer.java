import java.io.IOException;

import java.util.ArrayList;

import java.util.Collections;

import java.util.Comparator;

import java.util.HashMap;

import java.util.Iterator;

import java.util.List;

import java.util.Map;

import java.util.Map.Entry;

import java.util.TreeMap;

import java.util.regex.Matcher;

import java.util.regex.Pattern;

import org.apache.hadoop.io.Text;

import org.apache.hadoop.mapreduce.Reducer;

public class TaxiTimeReducer extends Reducer<Text, Text, Text, Text> {

	private Map<String, Double> map = new TreeMap<String, Double>();

	private Map<String, Double> execptionMap = new TreeMap<String, Double>();

	@Override
	protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

		Iterator<Text> iter = values.iterator();

		int total = 0;

		Integer regular = 0;

		while (iter.hasNext()) {

			String tmp = iter.next().toString();
			
			int aa = Integer.parseInt(tmp);
			
			regular = regular + aa;
			
			total = total + 1;
		
		}
		
		double res = regular * 1.0 / total;
		
		if(res == 0.0){
			
			execptionMap.put(key.toString(), res);
		
		}

		else{

			map.put(key.toString(), Double.valueOf(res));
		}

	}

	@Override
	protected void cleanup(Reducer<Text, Text, Text, Text>.Context context) throws IOException, InterruptedException {

		if (!map.isEmpty()) {
			
			List<Entry<String, Double>> list = new ArrayList<Entry<String, Double>>(map.entrySet());
			
			Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
			
				public int compare(Entry<String, Double> o1, Entry<String, Double> o2) {
			
					return o2.getValue().compareTo(o1.getValue());
			
				}
			
			});
			
			context.write(new Text("highest"), new Text(""));
			
			for (int i = 0; i < 3; i++) {
			
				Entry<String, Double> enter = list.get(i);
			
				context.write(new Text(enter.getKey()), new Text(enter.getValue() + ""));
			
			}
			
			context.write(new Text("lowest"), new Text(""));
			
			int length = list.size();
			
			for (int j = length - 1; j > length - 4; j--) {
			
				Entry<String, Double> enter = list.get(j);
			
				context.write(new Text(enter.getKey()), new Text(enter.getValue() + ""));
			
			}

			context.write(new Text("zero data"), new Text(""));

			if(execptionMap.isEmpty()){
			
				context.write(new Text("NONE"), new Text(""));
			
			}
			
			else{
			
				for (Entry<String, Double> enter:execptionMap.entrySet()) {
			
					context.write(new Text(enter.getKey()), new Text(enter.getValue() + ""));
			
				}
			
			}
		}

		else{
		
			context.write(new Text("There is no value can be used, so no output."), new Text(""));
		
			}

	}

}
