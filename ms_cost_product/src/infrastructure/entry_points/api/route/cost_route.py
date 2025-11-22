from fastapi import APIRouter
from  ..controller.cost_controller import router as cost_router

router = APIRouter()


router.include_router(cost_router)