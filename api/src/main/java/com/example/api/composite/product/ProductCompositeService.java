package com.example.api.composite.product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "ProductComposite", description = "REST API for composite product information.")
@RequestMapping("/v1/product-composite")
public interface ProductCompositeService {

  @Operation(
          summary = "${api.product-composite.get-composite-product.description}",
          description = "${api.product-composite.get-composite-product.notes}"
  )
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description =
                  "${api.responseCodes.ok.description}"),
          @ApiResponse(responseCode = "400", description =
                  "${api.responseCodes.badRequest.description}"),
          @ApiResponse(responseCode = "404", description =
                  "${api.responseCodes.notFound.description}"),
          @ApiResponse(responseCode = "422", description =
                  "${api.responseCodes.unprocessableEntity.description}")
  })
  @GetMapping("/{productId}")
  ProductAggregate getProduct(@PathVariable("productId") int productId);
}