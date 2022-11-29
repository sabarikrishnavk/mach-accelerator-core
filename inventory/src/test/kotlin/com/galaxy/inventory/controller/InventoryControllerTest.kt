package com.galaxy.inventory.controller

import com.galaxy.foundation.scalars.DateTimeScalarRegistration
import com.galaxy.inventory.codegen.client.InventorySkusLocationGraphQLQuery
import com.galaxy.inventory.codegen.client.InventorySkusLocationProjectionRoot
import com.galaxy.inventory.codegen.types.Inventory
import com.galaxy.inventory.services.InventoryService
import com.netflix.graphql.dgs.DgsQueryExecutor
import com.netflix.graphql.dgs.autoconfig.DgsAutoConfiguration
import com.netflix.graphql.dgs.client.codegen.GraphQLQueryRequest
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean

@SpringBootTest(classes = [InventoryController::class, DgsAutoConfiguration::class, DateTimeScalarRegistration::class])
class InventoryControllerTest {

    @Autowired
    lateinit var dgsQueryExecutor: DgsQueryExecutor

    @MockBean
    lateinit var inventoryService: InventoryService


    @BeforeEach
    fun before() {
        Mockito.`when`(inventoryService.inventory(Mockito.anyList(), Mockito.anyString())).thenAnswer {

            listOf(
                Inventory(skuid = "SKU1", location = "STR1", totalQty = 25),
                Inventory(skuid = "SKU1", location = "STR2", totalQty = 100),
                Inventory(skuid = "SKU2", location = "STR1", totalQty = 10),
                Inventory(skuid = "SKU2", location = "STR2", totalQty = 5)
            )
        }

    }

    @Test
    fun inventory() {
        val totalQty: List<Int> = dgsQueryExecutor.executeAndExtractJsonPath(
            """
            {
              inventorySkusLocation(skuids:["SKU1"],location:"STR1"){
               skuid
               location
               totalQty
              }
            }
        """.trimIndent(), "data.inventorySkusLocation[*].totalQty"
        )

        Assertions.assertThat(totalQty[0]).isEqualTo(25)
    }

    @Test
    fun inventoryWithException() {
        Mockito.`when`(inventoryService.inventory(Mockito.anyList(), Mockito.anyString())).thenThrow(RuntimeException("nothing to see here"))

        val result = dgsQueryExecutor.execute(
            """
            {
              inventorySkusLocation(skuids:["SKU"],location:"WH1"){
               skuid
               location
               totalQty
              }
            }
        """.trimIndent()
        )

        Assertions.assertThat(result.errors).isNotEmpty
        Assertions.assertThat(result.errors[0].message).isEqualTo("java.lang.RuntimeException: nothing to see here")
    }

    @Test
    fun inventoryWithQueryApi() {
        val graphQLQueryRequest =
            GraphQLQueryRequest(
                InventorySkusLocationGraphQLQuery.Builder()
                    .skuids(listOf("SKU1"))
                    .location("STR1")
                    .build(),
                InventorySkusLocationProjectionRoot().totalQty().location().skuid()
            )
        val quantity = dgsQueryExecutor.executeAndExtractJsonPath<List<Int>>(
            graphQLQueryRequest.serialize(),
            "data.inventorySkusLocation[*].totalQty"
        )
        Assertions.assertThat(quantity[0]).isEqualTo(25)
    }
}