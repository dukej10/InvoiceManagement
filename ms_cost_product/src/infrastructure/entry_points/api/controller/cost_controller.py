from fastapi import APIRouter, HTTPException, status
from src.domain.usecase.price_use_case import CalculatePriceUseCase
from src.infrastructure.entry_points.api.dto.invoice_dto import InvoiceRequestDTO
from src.domain.model.product import Product
from src.domain.model.invoice import Invoice

router = APIRouter()
usecase = CalculatePriceUseCase()

@router.post("/calculate", status_code=status.HTTP_200_OK)
async def calculate_price(request: InvoiceRequestDTO):
    invoice = to_model(request)
    result = usecase.calculate_amount(invoice)
    return result


def to_model(dto: InvoiceRequestDTO) -> Invoice:
    products_domain = []
    
    for p in dto.products:
        
        prod = Product(
            name=p.name,
            quantity=p.quantity,
            unit_price=p.unit_price
        )
        products_domain.append(prod)
        
    return Invoice(code=dto.code, products=products_domain)