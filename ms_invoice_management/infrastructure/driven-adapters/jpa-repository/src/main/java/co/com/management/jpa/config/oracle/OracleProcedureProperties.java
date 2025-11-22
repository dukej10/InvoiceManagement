package co.com.management.jpa.config.oracle;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "spring.datasource.oracle.procedure")
public class OracleProcedureProperties {

    private String schema;

    private String name;

    private final ArrayTypes arrayTypes = new ArrayTypes();

    @Getter
    @Setter
    public static class ArrayTypes {
        private String varchar2List;
        private String numberList;
        private String floatList;
    }
}
