from typing import List
from src.infrastructure.entry_points.api.dto.product_dto import ProductDTO
from pydantic import BaseModel, Field

class InvoiceRequestDTO(BaseModel):
    code: str = Field(..., description="Código de la factura")
    products: List[ProductDTO] = Field(..., min_items=1, description="Lista de productos")