from dataclasses import dataclass
from uuid import UUID
from typing import Final

@dataclass(frozen=True)
class Product:
    code: Final[UUID]
    name: Final[str]
    quantity: Final[int]
    unit_price: Final[float]
    taxes: Final[float] = 0.0