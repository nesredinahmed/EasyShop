package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.models.Product;
import org.yearup.data.ProductDao;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("products")
@CrossOrigin
public class ProductsController
{
    private ProductDao productDao;

    @Autowired
    public ProductsController(ProductDao productDao)
    {
        this.productDao = productDao;
    }

    @GetMapping("")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<Product>> search(@RequestParam(name="cat", required = false) Integer categoryId,
                                @RequestParam(name="minPrice", required = false) BigDecimal minPrice,
                                @RequestParam(name="maxPrice", required = false) BigDecimal maxPrice,
                                @RequestParam(name="color", required = false) String color
                                )
    {
        try
        {
            List<Product> products = productDao.search(categoryId, minPrice, maxPrice, color);
            return ResponseEntity.ok(products);
        }
        catch(Exception ex)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error searching products: " + ex.getMessage());
        }
    }

    @GetMapping("all")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<Product>> getAllProducts()
    {
        try
        {
            // Get all products by searching with no filters
            List<Product> products = productDao.search(null, null, null, null);
            return ResponseEntity.ok(products);
        }
        catch(Exception ex)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving products: " + ex.getMessage());
        }
    }

    @GetMapping("featured")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<Product>> getFeaturedProducts()
    {
        try
        {
            List<Product> products = productDao.getFeaturedProducts();
            return ResponseEntity.ok(products);
        }
        catch(Exception ex)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving featured products: " + ex.getMessage());
        }
    }

    @GetMapping("{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Product> getById(@PathVariable int id )
    {
        try
        {
            var product = productDao.getById(id);

            if(product == null)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found with id: " + id);

            return ResponseEntity.ok(product);
        }
        catch(ResponseStatusException ex)
        {
            throw ex;
        }
        catch(Exception ex)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving product: " + ex.getMessage());
        }
    }

    @PostMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Product> addProduct(@RequestBody Product product)
    {
        try
        {
            if (product.getName() == null || product.getName().trim().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product name is required");
            }
            if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Valid product price is required");
            }
            if (product.getCategoryId() <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Valid category ID is required");
            }

            Product createdProduct = productDao.create(product);
            if (createdProduct == null) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create product");
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
        }
        catch(ResponseStatusException ex)
        {
            throw ex;
        }
        catch(Exception ex)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating product: " + ex.getMessage());
        }
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Product> updateProduct(@PathVariable int id, @RequestBody Product product)
    {
        try
        {
            // Check if product exists
            Product existingProduct = productDao.getById(id);
            if (existingProduct == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found with id: " + id);
            }

            // Validate input
            if (product.getName() == null || product.getName().trim().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product name is required");
            }
            if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Valid product price is required");
            }
            if (product.getCategoryId() <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Valid category ID is required");
            }

            productDao.update(id, product);
            
            // Return the updated product
            Product updatedProduct = productDao.getById(id);
            return ResponseEntity.ok(updatedProduct);
        }
        catch(ResponseStatusException ex)
        {
            throw ex;
        }
        catch(Exception ex)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating product: " + ex.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable int id)
    {
        try
        {
            var product = productDao.getById(id);

            if(product == null)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found with id: " + id);

            productDao.delete(id);
            return ResponseEntity.noContent().build();
        }
        catch(ResponseStatusException ex)
        {
            throw ex;
        }
        catch(Exception ex)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting product: " + ex.getMessage());
        }
    }
}
