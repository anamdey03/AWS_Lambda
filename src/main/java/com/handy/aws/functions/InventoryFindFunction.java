package com.handy.aws.functions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class InventoryFindFunction 
extends InventoryS3Client
implements RequestHandler<HttpQuerystringRequest, HttpProductResponse> {

    @Override
    public HttpProductResponse handleRequest(HttpQuerystringRequest request, Context context) {
        context.getLogger().log("Input: " + request);

        String idAsString = (String)request.getQueryStringParameters().get("id");
        
        if(idAsString.equalsIgnoreCase("all")){
        	Product[] products = getAllProducts();
        	HttpProductResponse httpProductResponse = new HttpProductResponse(products);
        	return httpProductResponse;
        }
        else {
        	Integer productId = Integer.parseInt(idAsString);
            Product product = getProductById(productId);
            return new HttpProductResponse(product);
        }
        
    }

	private Product getProductById(int prodId) {
		/*
		 * Region region = Region.US_EAST_2; S3Client s3Client =
		 * S3Client.builder().region(region).build(); ResponseInputStream<?> objectData
		 * = s3Client.getObject(GetObjectRequest.builder()
		 * .bucket("handy-inventory-data-1117") .key("handy-tool-catalog.json")
		 * .build());
		 * 
		 * BufferedReader br = new BufferedReader(new InputStreamReader(objectData));
		 * 
		 * Product[] products = null;
		 * 
		 * Gson gson = new Gson(); products = gson.fromJson(br, Product[].class);
		 */
		
		Product[] products = getAllProducts();
        
        for(Product prod: products) {
        	if(prod.getId() == prodId) {
        		return prod;
        	}
        }
        
        return null;
        
        
		/*
		 * String outputString = null;
		 * 
		 * try { outputString = br.readLine(); br.close(); } catch (IOException e) {
		 * context.getLogger().log("An exception occured when attempting to readLine()"
		 * ); }
		 * 
		 * return outputString;
		 */
	}

}
