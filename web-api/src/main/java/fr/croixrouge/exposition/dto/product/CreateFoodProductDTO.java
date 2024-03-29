package fr.croixrouge.exposition.dto.product;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.exposition.dto.CreationDTO;
import fr.croixrouge.exposition.dto.QuantifierDTO;
import fr.croixrouge.storage.model.product.FoodConservation;
import fr.croixrouge.storage.model.product.FoodProduct;
import fr.croixrouge.storage.model.product.Product;
import fr.croixrouge.storage.model.product.ProductLimit;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class CreateFoodProductDTO extends CreationDTO<FoodProduct> {

    private final String name;
    private final QuantifierDTO quantity;

    private final String limitID;

    private final String foodConservation;

    private final Timestamp expirationDate;

    private final Timestamp optimalConsumptionDate;

    private final double price;

    private final String storageId;

    private final int amount;

    public CreateFoodProductDTO(String name, QuantifierDTO quantity, String limitID, String foodConservation, Timestamp expirationDate, Timestamp optimalConsumptionDate, double price, String storageId, int amount) {
        this.name = name;
        this.quantity = quantity;
        this.limitID = limitID;
        this.foodConservation = foodConservation;
        this.expirationDate = expirationDate;
        this.optimalConsumptionDate = optimalConsumptionDate;
        this.price = price;
        this.storageId = storageId;
        this.amount = amount;
    }

    public static ZonedDateTime toLocalDateTime(Timestamp timestamp) {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(timestamp.getTime()), ZoneId.of("Europe/Paris"));
    }

    public String getName() {
        return name;
    }

    public QuantifierDTO getQuantity() {
        return quantity;
    }

    public String getFoodConservation() {
        return foodConservation;
    }

    public Timestamp getExpirationDate() {
        return expirationDate;
    }

    public Timestamp getOptimalConsumptionDate() {
        return optimalConsumptionDate;
    }

    public double getPrice() {
        return price;
    }

    public String getStorageId() {
        return storageId;
    }

    public int getAmount() {
        return amount;
    }

    public String getLimitID() {
        return limitID;
    }

    @Override
    public FoodProduct toModel() {
        return new FoodProduct(null,
                new Product(null, name, quantity.toQuantifier(), null, null),
                FoodConservation.fromLabel(foodConservation),
                toLocalDateTime(expirationDate),
                toLocalDateTime(optimalConsumptionDate),
                price);
    }

    public FoodProduct toModel(ProductLimit limit, ID localUnitId) {
        return new FoodProduct(null,
                new Product(null, name, quantity.toQuantifier(), limit, localUnitId),
                FoodConservation.fromLabel(foodConservation),
                toLocalDateTime(expirationDate),
                toLocalDateTime(optimalConsumptionDate),
                price);
    }
}
