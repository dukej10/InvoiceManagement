package co.com.management.api.controllers.docs;

public class ErrorExample {

    public static final String CLIENT_ALREADY_EXISTS = """
        {
          "data": {
            "timestamp": "2026-01-27T00:39:18.578+00:00",
            "message": "El cliente ya se encuentra registrado",
            "details": "uri=/clients/create",
            "fieldErrors": null
          },
          "message": "Invalid request",
          "statusCode": 400,
          "timestamp": "2026-01-26T19:39:18.5792549"
        }
        """;
}
