package com.reto1.controller;

import java.util.List;

import org.bson.types.ObjectId;

import com.reto1.dao.DiscountPackDao;
import com.reto1.dao.DiscountPackImplementacionBD;
import com.reto1.model.DiscountPack;

/**
 * Business logic of the discount packs (offers).
 * <p>
 * The windows (package view) call this class; this class calls the DAO
 * through the interface {@link DiscountPackDao}.
 */
public class DiscountPackController {

    private final DiscountPackDao dao;

    /**
     * Creates the controller with the MongoDB implementation of the DAO.
     */
    public DiscountPackController() {
        this.dao = new DiscountPackImplementacionBD();
    }

    /**
     * @return every pack, sorted by name
     */
    public List<DiscountPack> findAll() {
        return dao.findAll();
    }

    /**
     * @param id _id of the pack
     * @return the pack, or {@code null} if it does not exist
     */
    public DiscountPack findById(ObjectId id) {
        return dao.findById(id);
    }

    /**
     * @param pack new pack
     */
    public void insert(DiscountPack pack) {
        dao.insert(pack);
    }

    /**
     * @param pack pack with the new data (same _id)
     * @return {@code true} if the pack was found and updated
     */
    public boolean update(DiscountPack pack) {
        return dao.update(pack);
    }

    /**
     * @param id _id of the pack
     * @return {@code true} if the pack was deleted
     */
    public boolean delete(ObjectId id) {
        return dao.delete(id);
    }
}
