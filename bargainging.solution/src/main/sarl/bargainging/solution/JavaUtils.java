package bargainging.solution;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

public class JavaUtils {

	
	public static void csvWriter(List<LinkedHashMap<String, String>> listOfMap, Writer writer) throws IOException {
	    CsvSchema schema = null;
	    CsvSchema.Builder schemaBuilder = CsvSchema.builder();
	    if (listOfMap != null && !listOfMap.isEmpty()) {
	    	schemaBuilder.addColumn("traderId")
	    	.addColumn("traderName")
	    	.addColumn("utilTrader")
	    	.addColumn("buyerId")
	    	.addColumn("buyerName")
	    	.addColumn("buyerUtil")
	    	.addColumn("itemsTrader")
	    	.addColumn("itemsBuyer").addColumn("actorType").addColumn("round").setUseHeader(true).setNullValue("0.0");
	       /* for (String col : listOfMap.get(0).keySet()) {
	        	System.out.println(col);
	            schemaBuilder.addColumn(col);
	        }*/
	        schema = schemaBuilder.build().withLineSeparator(System.lineSeparator()).withColumnSeparator(';');
	    }
	    CsvMapper mapper = new CsvMapper();
	    System.out.println(listOfMap);
	    System.out.println(writer);
	    mapper.writer(schema).writeValues(writer).writeAll(listOfMap);
	    writer.flush();
	}
	
	public LinkedHashMap loadGameConfig() throws JsonParseException, JsonMappingException, IOException, URISyntaxException {
		URL res = getClass().getClassLoader().getResource("game_config.json");
		LinkedHashMap<String,Object> result = new ObjectMapper().readValue(new File(res.toURI()), LinkedHashMap.class);
		return result;
	}
}
