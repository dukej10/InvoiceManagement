from domain.usecase.price_use_case import CalculatePriceUseCase
from fastapi import APIRouter, HTTPException, status
from ..dto.invoice_dto import InvoiceRequestDTO
from .....domain.model.invoice import Invoice
from .....domain.model.product import Product

router = APIRouter()

usecase = CalculatePriceUseCase()

@router.post("/calculate", status_code=200)
async def calculate_price(request: InvoiceRequestDTO):
    try:
        result = usecase.Executable(toModel(request))
        return result
    except ValueError as ve:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail=str(ve)
        )
    except Exception as e:
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail="Error interno del servidor"
        )
    
def toModel(dto: InvoiceRequestDTO): 
    products = [
        Product(
            code=product_dto.code,
            name=product_dto.name,
            quantity=product_dto.quantity,
            unit_price=product_dto.unitPrice,
            taxes=product_dto.taxes
        )
        for product_dto in dto.products
    ]
    from domain.model.invoice import Invoice
    invoice = Invoice(
        code=dto.code,
        products=products
    )
    return invoice