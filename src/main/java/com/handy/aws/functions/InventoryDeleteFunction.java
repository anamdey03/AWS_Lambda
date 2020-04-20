package com.handy.aws.functions;

import java.util.List;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class InventoryDeleteFunction 
extends InventoryS3Client
implements RequestHandler<HttpRequest, HttpProductResponse> {

    @Override
    public HttpProductResponse handleRequest(HttpRequest request, Context context) {
        context.getLogger().log("Input: " + request);

        String idAsString = (String)request.pathParameters.get("id");
        Integer productId = Integer.parseInt(idAsString);
        
        List<Product> productLists = getAllProductsList();
        
        boolean didRemove = productLists.removeIf(prod -> prod.getId() == productId);
        
        if(didRemove) {
        	if(updateAllProducts(productLists)) {
        		return new HttpProductResponse();
        	}
        }
        
        HttpProductResponse httpProductResponse = new HttpProductResponse();
        httpProductResponse.setStatusCode("404");
        return httpProductResponse;
    }

}
