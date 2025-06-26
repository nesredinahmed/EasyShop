package org.yearup.data;

import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

public interface ShoppingCartDao
{
    ShoppingCart getByUserId(int userId);
    void addItem(ShoppingCartItem item);
    void updateItem(ShoppingCartItem item);
    void removeItem(int userId, int productId);
    void clearCart(int userId);
    // add additional method signatures here
}
