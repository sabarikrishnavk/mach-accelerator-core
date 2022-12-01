
package com.pranetr.inventory.services

import com.pranetr.inventory.codegen.types.Inventory
import org.springframework.stereotype.Service

@Service
class InventoryService {
    fun inventory(): List<Inventory> {
        return listOf(
            Inventory(skuid = "SKU1", location = "STR1", totalQty = 25),
            Inventory(skuid = "SKU1", location = "STR2", totalQty = 100),
            Inventory(skuid = "SKU2", location = "STR1", totalQty = 10),
            Inventory(skuid = "SKU2", location = "STR2", totalQty = 5)
        )
    }
    fun inventory(skuids : List<String>): List<Inventory> {
        return  inventory()
    }
    fun inventory(skuids : List<String>,location: String): List<Inventory> {
        return  inventory()
    }

    fun inventory(skuid : String): List<Inventory> {
        return  inventory()
    }
}