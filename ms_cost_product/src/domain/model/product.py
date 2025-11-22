from dataclasses import dataclass
from typing import Final

from dataclasses import dataclass

@dataclass(frozen=True) 
class Product:
    name: Final[str]
    quantity: Final[int]
    unit_price: Final[float]
