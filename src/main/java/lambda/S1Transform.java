package lambda;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import saaf.Inspector;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author TCSS562 Trem_Project_GROUP8
 * @author Simerpreet,Sumitha and Sreenavya
 */

public class S1Transform {

	String filename = "";
	String bucketname = "";
	Inspector inspector = new Inspector();


	public HashMap<String, Object> handleRequest(Request request, Context context) throws IOException {
		AmazonS3 s3 = AmazonS3ClientBuilder.standard().build();
		filename = request.getFilename();
		bucketname = request.getBucketname();
		System.out.print("bucketname :: " + bucketname);
		System.out.print("filename :: "  + filename);
		try {
			S3Object s3object = s3.getObject(bucketname, filename);
			displayTextInputStream(s3object.getObjectContent());
		} catch (AmazonServiceException e) {
			System.err.println(e.getErrorMessage());
		}
		return inspector.finish();
	}

	private void displayTextInputStream(InputStream input) throws IOException {
        // Read the text input stream one line at a time and display each line.
		List<List<String>> lines = new ArrayList<>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		String line;
		while ((line = reader.readLine()) != null) {
			String[] values = line.split(",");
			lines.add(Arrays.asList(values));
		}
		transformData(lines);
	}

	public void transformData(List<List<String>> list){
		list = removeDuplicates(list);
		list = list.stream().map(row -> {
			try {
				row = new ArrayList(row.stream().map(x -> {x = x.replaceAll("\'", " ").replaceAll("[^\\x20-\\x7e]", "");; return new String(x.getBytes(StandardCharsets.US_ASCII), StandardCharsets.US_ASCII);}).collect(Collectors.toList()));
				String element = String.valueOf(row.get(4));
				String firstCol = String.valueOf(row.get(0));
				System.out.println("For " + firstCol + " compare value : " + "Region".equals(firstCol));
				switch (firstCol) {
					case "Region": {
						row.add("Gross Margin");
						row.add("Order Processing Time");
						break;
					}
					default: {
						float margin = Float.parseFloat(row.get(13)) / Float.parseFloat(row.get(11));
						row.add(String.valueOf((margin * 100)));

						SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
						Date shipDate = sdf.parse(row.get(7));
						Date orderDate = sdf.parse(row.get(5));

						long days = (shipDate.getTime() - orderDate.getTime()) / (1000 * 60 * 60 * 24);
						row.add(String.valueOf(days));

						if (element.equals("L")) {
							row.set(4, "Low");
						} else if (element.equals("M")) {
							row.set(4, "Medium");
						} else if (element.equals("H")) {
							row.set(4, "High");
						} else if (element.equals("C")) {
							row.set(4, "Critical");
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return row;
		}).filter(x -> x != null).collect(Collectors.toList());
		WriteCSV(list);
	}

	public List<List<String>> removeDuplicates(List<List<String>> list) {
        // Create a new ArrayList
		List<String> knowKeys = new ArrayList<>(list.size());
		return list.stream().filter(row -> {
			String id = row.get(6);
			if (!knowKeys.contains(id)) {
				knowKeys.add(id);
				return true;
			}
			if (knowKeys.contains("Order ID")) {
				System.out.println("Found a header " + row.toString());
			}
			return false;
		}).collect(Collectors.toList());
	}

	public void WriteCSV(List<List<String>> list){
       // the following code lets you iterate through the 2-dimensional array

		StringWriter sw = new StringWriter();
		for(List<String> line: list) {
			int i = 0;
			for (String value: line) {
				sw.append(value);
				if (i++ != line.size() - 1)
					sw.append(',');
			}
			sw.append("\n");
		}

		byte[] bytes = sw.toString().getBytes(StandardCharsets.UTF_8);
		InputStream is = new ByteArrayInputStream(bytes);
		ObjectMetadata meta = new ObjectMetadata();
		meta.setContentLength(bytes.length);

// Create new file on S3
		AmazonS3 s3Client = AmazonS3ClientBuilder.standard().build();
		s3Client.putObject(bucketname, "result.csv", is, meta);
	}

}