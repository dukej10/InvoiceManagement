from typing import List
from infraestructure.entry_points.api.dto.product_dto import ProductDTO
from pydantic import BaseModel, Field, validator
from uuid import UUID

class InvoiceRequestDTO(BaseModel):
    code: UUID = Field(..., description="Código de la factura")
    products: List[ProductDTO] = Field(..., min_items=1, description="Lista de productos")