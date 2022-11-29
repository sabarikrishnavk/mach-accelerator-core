package com.galaxy.catalog.controller

import com.galaxy.catalog.services.SkuService
import com.galaxy.foundation.context.CustomContext
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsData
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument
import com.netflix.graphql.dgs.context.DgsContext
import graphql.schema.DataFetchingEnvironment
import org.springframework.security.access.prepost.PreAuthorize


@DgsComponent
class CatalogController(private val skuService: SkuService ) {
    /**
     * This datafetcher resolves the shows field on Query.
     * It uses an @InputArgument to get the titleFilter from the Query if one is defined.
     */
    @DgsQuery
//    @Secured("REGISTERED")
//    @PreAuthorize("hasAnyRole('ROLE_GUEST',)") //Will throw exception
    @PreAuthorize("hasAnyRole('ROLE_REGISTERED','ROLE_GUEST')")
    fun skus(@InputArgument skuids : List<String>): List<com.galaxy.catalog.codegen.types.Sku> {

        return  skuService.skus(skuids)
    }

    @DgsData(parentType = "Query", field = "getStoreId")
    @PreAuthorize("hasAnyRole('ROLE_REGISTERED','ROLE_GUEST')")
    fun getStoreId(dfe: DataFetchingEnvironment?): String? {
        val customContext: CustomContext = DgsContext.getCustomContext(dfe!!)
        return customContext.location
    }
}