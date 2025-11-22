from pydantic import BaseModel, Field, validator
from uuid import UUID

class ProductDTO(BaseModel):
    code: str = Field(..., description="Código del producto")
    name: str = Field(..., min_length=1, max_length=200, description="Nombre del producto")
    quantity: int = Field(..., gt=0, description="Cantidad debe ser mayor a 0")
    unit_price: float = Field(..., alias="unitPrice", description="Precio unitario",gt=0.0)
    taxes: float = Field(ge=0.0, description="Impuestos aplicables")

    @validator('name')
    def strip_and_validate_name(cls, v: str) -> str:
        stripped = v.strip()
        if not stripped:
            raise ValueError("El nombre no puede estar vacío")
        return stripped

