# src/infrastructure/main.py
from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware

from src.infrastructure.entry_points.api.config.setting import setting
from src.infrastructure.entry_points.api.route.cost_route import cost_router

app = FastAPI(
    title="Price Calculator Microservice",
    version="1.0.0",
    openapi_url="/openapi.json",
    docs_url="/docs",
    redoc_url="/redoc",
)

# Middleware CORS
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Obbtener
app.include_router(cost_router, prefix=setting.management_path_api)

