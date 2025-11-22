from datetime import datetime
from decimal import Decimal, ROUND_HALF_UP
from typing import List
from uuid import uuid4

from src.domain.model.invoice import Invoice
from src.domain.model.product import Product

class CalculatePriceUseCase:

    def calculateAmount(self, invoice: Invoice) -> dict:
    
        total = Decimal('0.00')
        for product in invoice.products:
            subtotal = product.unit_price * product.quantity
            total_taxes = subtotal * product.taxes / 100
            total = Decimal(subtotal + total_taxes)


        response_data = {
            "code": 1121333,
            "totalAmount": total,
            "products": [
                {
                    "code": str(p.code),
                    "name": p.name,
                    "quantity": p.quantity,
                    "unitPrice": float(p.unit_price)
                }
                for p in invoice.products
            ],
            "createdDate": datetime.utcnow().isoformat(timespec="milliseconds"),
        }

        return {
            "data": response_data,
            "message": "Operación exitosa",
            "statusCode": 200,
            "timestamp": datetime.utcnow().isoformat(timespec="milliseconds")
        }