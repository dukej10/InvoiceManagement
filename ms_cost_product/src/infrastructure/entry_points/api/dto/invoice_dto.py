from typing import List
from pydantic import BaseModel, Field
from .product_dto import ProductDTO

class InvoiceRequestDTO(BaseModel):
    code: str = Field(..., min_length=1, max_length=50, description="Código de la factura")
    products: List[ProductDTO] = Field(..., min_items=1, description="Lista de productos")