package com.example.api.composite.product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Tag(name = "ProductComposite", description = "REST API for composite product information.")
@RequestMapping("/v1/product-composite")
public interface ProductCompositeService {

  @Operation(
          summary = "${api.product-composite.create-composite-product.description}",
          description = "${api.product-composite.create-composite-product.notes}"
  )
  @ApiResponses(value = {
          @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
          @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")
  })
  @ResponseStatus(HttpStatus.ACCEPTED)
  @PostMapping(consumes = "application/json")
  Mono<Void> createProduct(@RequestBody ProductAggregate body);

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
  Mono<ProductAggregate> getProduct(@PathVariable("productId") int productId);

  @Operation(
          summary = "${api.product-composite.delete-composite-product.description}",
          description = "${api.product-composite.delete-composite-product.notes}")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
          @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")
  })
  @ResponseStatus(HttpStatus.ACCEPTED)
  @DeleteMapping(value = "/{productId}")
  Mono<Void> deleteProduct(@PathVariable("productId") int productId);
}
