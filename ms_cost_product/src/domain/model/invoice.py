from uuid import UUID
from typing import Final, List
from dataclasses import dataclass
from src.domain.model.product import Product

@dataclass(frozen=True)
class Invoice:
    code: Final[UUID]
    products: Final[List[Product]]