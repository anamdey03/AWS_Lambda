package com.handy.aws.functions;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.core.sync.RequestBody;

public class InventoryS3Client {
	
	protected Product[] getAllProducts() {
		Region region = Region.US_EAST_2;
        S3Client s3Client = S3Client.builder().region(region).build();
        ResponseInputStream<?> objectData = s3Client.getObject(GetObjectRequest.builder()
        		.bucket("handy-inventory-data-1117")
        		.key("handy-tool-catalog.json")
        		.build());
        
        InputStreamReader isr = new InputStreamReader(objectData);
        BufferedReader br = new BufferedReader(isr);
        
        Product [] products = null;

		Gson gson = new Gson();
		products = gson.fromJson(br, Product[ ].class);
		return products;
	}
	
	protected ArrayList<Product> getAllProductsList(){
		return new ArrayList<Product>(Arrays.asList(getAllProducts()));
	}	
	
	protected boolean updateAllProducts(Product [] products) {
		
		Gson gson = new Gson(); 
        String jsonString = gson.toJson(products);
		
		
		Region region = Region.US_EAST_2;
        S3Client s3Client = S3Client.builder().region(region).build();
        
        PutObjectResponse putResponse = s3Client.putObject(PutObjectRequest.builder()
        		.bucket("handy-inventory-data-1117")
        		.key("handy-tool-catalog.json").build(),
        		RequestBody.fromString(jsonString));
        
        return putResponse.sdkHttpResponse().isSuccessful();
        
	}
	
	protected boolean updateAllProducts(List<Product> productList) {
		Product [] products = (Product[]) productList.toArray(new Product[productList.size()]);
		return updateAllProducts(products);
	}
	

}
