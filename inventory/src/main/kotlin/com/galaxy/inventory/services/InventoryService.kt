
package com.galaxy.inventory.services

import com.galaxy.inventory.codegen.types.Inventory
import org.springframework.stereotype.Service

@Service
class InventoryService {
    fun inventory(): List<com.galaxy.inventory.codegen.types.Inventory> {
        return listOf(
            Inventory(skuid = "SKU1", location = "STR1", totalQty = 25),
            Inventory(skuid = "SKU1", location = "STR2", totalQty = 100),
            Inventory(skuid = "SKU2", location = "STR1", totalQty = 10),
            Inventory(skuid = "SKU2", location = "STR2", totalQty = 5)
        )
    }
    fun inventory(skuids : List<String>): List<com.galaxy.inventory.codegen.types.Inventory> {
        return  inventory()
    }
    fun inventory(skuids : List<String>,location: String): List<com.galaxy.inventory.codegen.types.Inventory> {
        return  inventory()
    }

    fun inventory(skuid : String): List<com.galaxy.inventory.codegen.types.Inventory> {
        return  inventory()
    }
}