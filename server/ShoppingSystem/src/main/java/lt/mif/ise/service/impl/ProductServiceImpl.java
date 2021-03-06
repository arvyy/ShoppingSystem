package lt.mif.ise.service.impl;

import lt.mif.ise.domain.Product;
import lt.mif.ise.domain.search.ProductCriteria;
import lt.mif.ise.domain.search.ProductSearch;
import lt.mif.ise.error.exception.NotFoundException;
import lt.mif.ise.jpa.ProductRepository;
import lt.mif.ise.jpa.ProductSearchRepository;
import lt.mif.ise.service.CategoryService;
import lt.mif.ise.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class ProductServiceImpl implements ProductService {
	
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductSearchRepository productSearchRepository;

    @Override
    public Product getById(String id) {
        return productRepository.findByProductId(id).orElseThrow(()->new RuntimeException("Failed to get product."));
    }

    @Override
    public Page<ProductSearch> findProducts(ProductCriteria criteria, Pageable page) {
    	if (criteria.isUnspecified()) return productSearchRepository.findAll(page);
        return productSearchRepository.findAll(buildProductSpec(criteria), page);
    }
    
    @Override
    public Iterable<ProductSearch> findProductsList(ProductCriteria criteria) {
    	if (criteria.isUnspecified()) return productSearchRepository.findAll();
        return productSearchRepository.findAll(buildProductSpec(criteria));
    }

    @Override
    public Product save(Product product, boolean isNew) {
    	if (!isNew) {
    		Product p = productRepository.findByProductId(product.getProductId()).orElseThrow(() -> new NotFoundException(String.format("Product with product id %s not found", product.getProductId())));
    		product.setId(p.getId());
            p.setProperties(product);
            return productRepository.save(p);
    	}
        return productRepository.save(product);
    }

    @Override
	@Transactional
    public void delete(String productId) {
        productRepository.deleteByProductId(productId);
    }
    
    private Specification<ProductSearch> buildProductSpec(ProductCriteria search) {
    	return (Root<ProductSearch> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
    		ArrayList<Predicate> predicates = new ArrayList<>();
    		search.getText().filter(t->!t.isEmpty()).ifPresent(t -> {
    			predicates.add(builder.like(root.get("name"), "%" + t + "%"));
    		});
    		search.getCategory().filter(t->!t.isEmpty()).ifPresent(t -> {
    			predicates.add(builder.equal(root.get("category").get("name"), t));
    		});
    		return builder.and(predicates.toArray(new Predicate[predicates.size()]));
    	};
    }

    @Override
	public boolean isProductIdValid(String productId){
		if (productId.isEmpty())
			return false;

		Pattern p = Pattern.compile("[^a-z0-9&-]", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(productId);
		boolean b = m.find();

		if (b)
			return false;

		return true;
	}

}
