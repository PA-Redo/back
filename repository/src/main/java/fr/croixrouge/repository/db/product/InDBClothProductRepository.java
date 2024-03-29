package fr.croixrouge.repository.db.product;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.storage.model.product.ClothProduct;
import fr.croixrouge.storage.repository.ClothProductRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

public class InDBClothProductRepository implements ClothProductRepository {
    private final ClothProductDBRepository clothProductDBRepository;
    private final InDBProductRepository inDBProductRepository;

    public InDBClothProductRepository(ClothProductDBRepository clothProductDBRepository, InDBProductRepository inDBProductRepository) {
        this.clothProductDBRepository = clothProductDBRepository;
        this.inDBProductRepository = inDBProductRepository;
    }

    public ClothProduct toClothProduct(ClothProductDB clothProductDB) {
        return new ClothProduct(
                ID.of(clothProductDB.getId()),
                inDBProductRepository.findById(new ID(clothProductDB.getProductDB().getId())).orElseThrow(),
                clothProductDB.getSize(),
                clothProductDB.getGender()
        );
    }

    public ClothProductDB toClothProductDB(ClothProduct clothProduct) {
        return new ClothProductDB(
                clothProduct.getId() == null ? null : clothProduct.getId().value(),
                inDBProductRepository.toProductDB(inDBProductRepository.findById(clothProduct.getProduct().getId()).orElseThrow()),
                clothProduct.getSize(),
                clothProduct.getGender()
        );
    }

    @Override
    public Optional<ClothProduct> findById(ID id) {
        return clothProductDBRepository.findById(id.value()).map(this::toClothProduct);
    }

    @Override
    public ID save(ClothProduct object) {
        inDBProductRepository.save(object.getProduct());
        var id = new ID(clothProductDBRepository.save(toClothProductDB(object)).getId());
        object.setId(id);
        return id;
    }

    @Override
    public void delete(ClothProduct object) {
        clothProductDBRepository.delete(toClothProductDB(object));
    }

    @Override
    public List<ClothProduct> findAll() {
        return StreamSupport.stream(clothProductDBRepository.findAll().spliterator(), false)
                .map(this::toClothProduct)
                .toList();
    }

    @Override
    public Optional<ClothProduct> findByProductId(ID productId) {
        return clothProductDBRepository.findByProductId(productId.value()).map(this::toClothProduct);
    }
}
