from fastapi import APIRouter, HTTPException, status
from src.domain.usecase.price_use_case import CalculatePriceUseCase
from src.infrastructure.entry_points.api.dto.invoice_dto import InvoiceRequestDTO
from src.domain.model.product import Product
from src.domain.model.invoice import Invoice

router = APIRouter()
usecase = CalculatePriceUseCase()


@router.post("/calculate", status_code=status.HTTP_200_OK)
async def calculate_price(request: InvoiceRequestDTO):
    try:
        invoice_model = to_model(request)
        result = usecase.calculateAmount(invoice_model)
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


def to_model(dto: InvoiceRequestDTO) -> Invoice:
    products = [
        Product(
            code=p.code,
            name=p.name,
            quantity=p.quantity,
            unit_price=p.unit_price,
            taxes=p.taxes
        )
        for p in dto.products
    ]
    return Invoice(code=dto.code, products=products)