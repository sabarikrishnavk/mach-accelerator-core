
package com.galaxy.catalog.controller

import com.galaxy.catalog.codegen.types.Attributes
import com.galaxy.catalog.codegen.types.Sku
import com.galaxy.catalog.services.SkuService
import com.galaxy.foundation.scalars.DateTimeScalarRegistration
import com.jayway.jsonpath.TypeRef
import com.netflix.graphql.dgs.DgsQueryExecutor
import com.netflix.graphql.dgs.autoconfig.DgsAutoConfiguration
import com.netflix.graphql.dgs.client.codegen.GraphQLQueryRequest
import graphql.ExecutionResult
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import java.time.OffsetDateTime

@SpringBootTest(classes = [CatalogController::class, DgsAutoConfiguration::class, DateTimeScalarRegistration::class])
class CatalogControllerTest {

    @Autowired
    lateinit var dgsQueryExecutor: DgsQueryExecutor

    @MockBean
    lateinit var skuService: SkuService

    private inline fun <reified T> any(type: Class<T>): T = Mockito.any(type)

    @BeforeEach
    fun before() {

        var attributes1 = listOf(
            Attributes("ATTR1","color","blue",true,true,true),
            Attributes("ATTR1","color","red",true,true,true),
            Attributes("ATTR1","color","green",true,true,true),

            Attributes("ATTR2","size","M",true,true,true),
            Attributes("ATTR2","size","S",true,true,true),
            Attributes("ATTR2","size","L",true,true,true),
        )

        var attributes2 = listOf(
            Attributes("ATTR3","origin","US",true,false,false),
            Attributes("ATTR4","brand","adidas",true,true,false)

        )

        `when`(skuService.skus(anyList())).thenAnswer {

             listOf(
                Sku(skuid = "SKU1", name = "Stranger Things", price = 150.0, listprice= 150.0,discountprice = 150.0, attributes = listOf(attributes1.get(0),attributes1.get(3), attributes2.get(0),attributes2.get(1) ) ),
                Sku(skuid = "SKU2", name = " Things", price = 200.0, listprice= 200.0,discountprice = 200.0, attributes = listOf(attributes1.get(1),attributes1.get(4), attributes2.get(0),attributes2.get(1) ) ),
                Sku(skuid = "SKU3", name = "Stranger ", price = 25.0, listprice= 25.0,discountprice = 25.0, attributes = listOf(attributes1.get(2),attributes1.get(5), attributes2.get(0),attributes2.get(1) ) )
            )
         }

    }

    @Test
    fun skus() {
        val names: List<String> = dgsQueryExecutor.executeAndExtractJsonPath(
            """
            {
                skus (skuids:["SKU1"]) {
                    name
                    price
                }
            }
        """.trimIndent(), "data.skus[*].name"
        )

        assertThat(names).contains("Stranger Things")
    }

    @Test
    fun showsWithException() {
        `when`(skuService.skus(anyList())).thenThrow(RuntimeException("nothing to see here"))

        val result = dgsQueryExecutor.execute(
            """
             {
                skus (skuids:["SKU1"]) {
                    name
                    price
                    skuid
                }
            }
        """.trimIndent()
        )

        assertThat(result.errors).isNotEmpty
        assertThat(result.errors[0].message).isEqualTo("java.lang.RuntimeException: nothing to see here")
    }

    @Test
    fun skusWithQueryApi() {
        val graphQLQueryRequest =
            GraphQLQueryRequest(
                com.galaxy.catalog.codegen.client.SkusGraphQLQuery.Builder().skuids(skuids = listOf("SKU1")).build(),
                com.galaxy.catalog.codegen.client.SkusProjectionRoot().name()
            )
        val titles = dgsQueryExecutor.executeAndExtractJsonPath<List<String>>(
            graphQLQueryRequest.serialize(),
            "data.skus[*].name"
        )
        assertThat(titles[0]).contains("Stranger Things")
    }
}