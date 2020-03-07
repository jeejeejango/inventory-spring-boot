package com.redhat.cloudnative.inventory;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author boonp
 * @since 07/03/2020 4:09 PM
 */
public interface InventoryRepository extends JpaRepository<Inventory, String> {

}
