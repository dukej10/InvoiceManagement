package co.com.management.api;

import co.com.management.api.dto.models.response.ResponseDTO;
import lombok.experimental.UtilityClass;

import java.util.Map;

@UtilityClass
public class Utility {

    private static final Map<Integer, String> MESSAGE_MAP = Map.of(
            200, "Operación exitosa",
            201, "Creado correctamente",
            400, "Solicitud inválida",
            404, "Recurso no encontrado",
            500, "Error interno en el servidor"
    );


    public static <T> ResponseDTO<T> structureRS(T dto, int statusCode) {
        return new ResponseDTO<>(
                dto,
                MESSAGE_MAP.get(statusCode),
                statusCode
        );
    }
}