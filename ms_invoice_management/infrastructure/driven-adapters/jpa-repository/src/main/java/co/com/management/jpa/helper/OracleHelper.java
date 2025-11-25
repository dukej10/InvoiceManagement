package co.com.management.jpa.helper;

import oracle.jdbc.OracleConnection;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import org.springframework.jdbc.core.support.AbstractSqlTypeValue;

import java.sql.Connection;
import java.sql.SQLException;

public class OracleHelper extends AbstractSqlTypeValue {
    private final String typeName;
    private final Object[] elements;

    public OracleHelper(String typeName, Object[] elements) {
        this.typeName = typeName.toUpperCase();
        this.elements = elements != null ? elements : new Object[0];
    }


    @Override
    @SuppressWarnings("deprecation")
    protected Object createTypeValue(Connection con, int sqlType, String typeName) throws SQLException {
        OracleConnection oracleConn = con.unwrap(OracleConnection.class);

        ArrayDescriptor descriptor = ArrayDescriptor.createDescriptor(this.typeName, oracleConn);
        return new ARRAY(descriptor, oracleConn, elements);
    }
}
