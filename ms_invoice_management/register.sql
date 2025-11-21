CREATE OR REPLACE PROCEDURE INSERT_INVOICE(
    p_invoice_code        IN VARCHAR2, 
    p_create_date         IN TIMESTAMP,
    p_total_amount        IN FLOAT,
    p_client_id           IN VARCHAR2, 
    p_insert_invoice      IN NUMBER,
	
    p_products_code       IN VARCHAR2_LIST,
    p_products_name       IN VARCHAR2_LIST,
    p_products_quantity   IN NUMBER_LIST,
    p_products_unit_price IN FLOAT_LIST,
	
    out_invoice_id OUT VARCHAR2
)
IS
BEGIN

    IF p_insert_invoice = 1 THEN
        
        -- Usando el esquema explícito para mayor seguridad
        INSERT INTO INVOICE.INVOICES(CODE, CLIENT_ID, CREATED_AT, TOTAL_AMOUNT)
        VALUES(p_invoice_code, p_client_id, p_create_date, p_total_amount); 
		
        FOR i IN 1..p_products_quantity.COUNT LOOP
            INSERT INTO PRODUCTS(
            	CODE,
            	NAME_PRODUCT,
                QUANTITY,
                UNIT_PRICE,
                INVOICE_ID
            )
            VALUES(
    			p_products_code(i),
                p_products_name(i),
                p_products_quantity(i),
                p_products_unit_price(i),
                p_invoice_code 
            );
        END LOOP;

        out_invoice_id := p_invoice_code; 
        COMMIT;

    ELSE
        out_invoice_id := NULL;
    END IF;

END;
/