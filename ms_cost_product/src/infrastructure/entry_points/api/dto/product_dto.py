from pydantic import BaseModel, Field, ConfigDict

class ProductDTO(BaseModel):
    model_config = ConfigDict(populate_by_name=True)

    name: str = Field(..., min_length=1, max_length=200, description="Nombre del producto")
    quantity: int = Field(..., gt=0, description="Cantidad debe ser mayor a 0")
    unit_price: float = Field(..., description="Precio unitario", gt=0.0)
