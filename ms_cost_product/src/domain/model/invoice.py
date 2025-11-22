from typing import Final, List
from dataclasses import dataclass
from src.domain.model.product import Product

@dataclass(frozen=True) 
class Invoice:
    code: Final[str]
    products: Final[List[Product]]