from pydantic import BaseModel, Field, ConfigDict
from typing import Annotated

class ProductDTO(BaseModel):
    model_config = ConfigDict(populate_by_name=True)

    name: Annotated[str, Field(..., min_length=1, max_length=200, description="Nombre del producto")]
    quantity: Annotated[int, Field(..., gt=0, description="Cantidad debe ser mayor a 0")]
    unit_price: Annotated[float, Field(..., gt=0.0, description="Precio unitario")]