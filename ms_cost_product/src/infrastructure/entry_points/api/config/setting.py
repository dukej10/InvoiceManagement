from pydantic_settings import BaseSettings
from typing import Literal

class Setting(BaseSettings):

    # Entorno de ejecución
    environment: Literal["local", "dev", "qa", "prod"] = "local"

    # Prefijo común de tus APIs internas
    management_path_api: str = "/api/v1"

    model_config = {
        "extra": "ignore",
        "case_sensitive": False,
    }

# Instancia global única
setting = Setting()
