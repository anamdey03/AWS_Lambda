package com.handy.aws.functions;

import java.util.List;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;

public class InventoryUpdateFunction
extends InventoryS3Client
implements RequestHandler<HttpRequest, HttpProductResponse> {

    @Override
    public HttpProductResponse handleRequest(HttpRequest request, Context context) {
        context.getLogger().log("Input: " + request);

        Gson gson = new Gson();
        String body = request.getBody();
        Product productToAdd = gson.fromJson(body, Product.class);
        
        List<Product> productLists = getAllProductsList();
        productLists.removeIf(prod -> prod.getId() == productToAdd.getId());
        
        HttpProductResponse httpProductResponse = new HttpProductResponse(productToAdd);
        productLists.add(productToAdd);
        
        if(!super.updateAllProducts(productLists)) {
        	httpProductResponse.setStatusCode("500");
        }
        return httpProductResponse;
    }

}
