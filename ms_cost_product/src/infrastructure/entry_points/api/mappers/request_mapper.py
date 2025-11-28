# src/infrastructure/entry_points/api/mappers/invoice_mapper.py
from src.domain.model.invoice import Invoice
from src.domain.model.product import Product
from src.infrastructure.entry_points.api.dto.invoice_dto import InvoiceRequestDTO
from decimal import Decimal

def to_model(dto: InvoiceRequestDTO) -> Invoice:
    products_domain = [
        Product(
            name=p.name,
            quantity=p.quantity,
            unit_price=Decimal(str(p.unit_price)).quantize(Decimal('0.00'))
        )
        for p in dto.products
    ]
    return Invoice(code=dto.code, products=products_domain)