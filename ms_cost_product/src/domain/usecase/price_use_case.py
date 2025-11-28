from datetime import datetime
from decimal import Decimal, ROUND_HALF_UP
from typing import List
from uuid import uuid4

from src.domain.model.invoice import Invoice
from src.domain.model.product import Product

class CalculatePriceUseCase:

    def calculate_amount(self, invoice: Invoice) -> dict:
    
        total = Decimal('0.00')
        for product in invoice.products:
            subtotal = product.unit_price * product.quantity
            total = Decimal(subtotal)


        response_data = {
            "code": invoice.code,
            "totalAmount": total,
            "products": [
                {
                    "name": p.name,
                    "quantity": p.quantity,
                    "unitPrice": p.unit_price
                }
                for p in invoice.products
            ],
        }

        return {
            "data": response_data,
            "message": "Operación exitosa",
            "statusCode": 200,
            "timestamp": datetime.utcnow().isoformat(timespec="milliseconds")
        }