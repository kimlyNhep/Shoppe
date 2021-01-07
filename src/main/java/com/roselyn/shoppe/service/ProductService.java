package com.roselyn.shoppe.service;

import com.roselyn.shoppe.exception.SpBadRequestException;
import com.roselyn.shoppe.exception.SpResourceNotFoundException;
import com.roselyn.shoppe.model.Category;
import com.roselyn.shoppe.model.Product;
import com.roselyn.shoppe.repository.CategoryRepository;
import com.roselyn.shoppe.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    public Integer createProduct(Product product, Integer categoryId) throws SpResourceNotFoundException, SpBadRequestException {
        Integer count = productRepository.countByProductName(product.getProductName());
        if (count > 0) throw new SpBadRequestException("Product already exist");

        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isEmpty()) throw new SpResourceNotFoundException("Category Not Found");

        product.setCategory(category.get());
        category.get().getProducts().add(product);

        return productRepository.save(product).getId();
    }

    public Product updateProduct(Product product, Integer productId) throws SpBadRequestException, SpResourceNotFoundException {
        Optional<Product> existingProduct = productRepository.findById(productId);

        if (existingProduct.isEmpty()) throw new SpResourceNotFoundException("Product Not Found");

        Integer count = productRepository.countByProductName(product.getProductName());
        if (count > 0 && !existingProduct.get().getProductName().equals(product.getProductName()))
            throw new SpBadRequestException("Product already exist");

        Optional<Category> category = categoryRepository.findById(product.getCategory().getId());
        if (category.isEmpty()) throw new SpResourceNotFoundException("Category Not Found");

        existingProduct.get().setProductName(product.getProductName());
        existingProduct.get().setDescription(product.getDescription());
        existingProduct.get().setPrice(product.getPrice());
        existingProduct.get().setCategory(category.get());
        category.get().getProducts().add(existingProduct.get());

        Optional<Product> updatedProduct = existingProduct.stream().map(p -> new Product(p.getId(), p.getProductName(), p.getDescription(), p.getPrice(),
                new Category(p.getCategory().getCategoryName()))).findFirst();

        assert updatedProduct.isPresent();

        return updatedProduct.get();
    }

    public Product getProduct(Integer productId) throws SpResourceNotFoundException {
        Optional<Product> existingProduct = productRepository.findById(productId);

        if (existingProduct.isEmpty()) throw new SpResourceNotFoundException("Product Not Found");
        Optional<Product> optionalProduct = existingProduct.stream().map(p -> new Product(p.getId(), p.getProductName(), p.getDescription(), p.getPrice(),
                new Category(p.getCategory().getId(), p.getCategory().getCategoryName()))).findFirst();

        assert optionalProduct.isPresent();

        return optionalProduct.get();
    }

    public List<Product> getProducts() throws SpResourceNotFoundException {
        List<Product> products = productRepository.findAll();
        return products.stream().map(product ->
                new Product(product.getId(), product.getProductName(), product.getDescription(), product.getPrice(),
                        new Category(product.getCategory().getId(), product.getCategory().getCategoryName()))).collect(Collectors.toList());
    }

    public void deleteProduct(Integer productId) throws SpResourceNotFoundException {
        Optional<Product> existingProduct = productRepository.findById(productId);
        if (existingProduct.isEmpty()) throw new SpResourceNotFoundException("Product Not Found");
        productRepository.delete(existingProduct.get());
    }
}
