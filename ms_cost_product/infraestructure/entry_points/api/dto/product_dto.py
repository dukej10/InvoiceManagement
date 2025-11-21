from pydantic import BaseModel, Field, validator
from uuid import UUID

class ProductDTO(BaseModel):
    code: UUID = Field(..., description="UUID del producto")
    name: str = Field(..., min_length=1, max_length=200, description="Nombre del producto")
    quantity: int = Field(..., gt=0, description="Cantidad debe ser mayor a 0")
    unitPrice: float = Field(..., ge=0.0, alias="unitPrice", description="Precio unitario")
    taxes: float = Field(0.0, ge=0.0, description="Impuestos aplicables", default=0.0)

    @validator('name')
    def strip_and_validate_name(cls, v: str) -> str:
        stripped = v.strip()
        if not stripped:
            raise ValueError("El nombre no puede estar vacío")
        return stripped

