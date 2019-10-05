package io.github.brenovit.store.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document(collection = "products")
@AllArgsConstructor
@NoArgsConstructor
public class Product {
	@Id
	private String id;
	private String prodName;
	private String prodDesc;
	private Double prodPrice;
	private String prodImage;
}
