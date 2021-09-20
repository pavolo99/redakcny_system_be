package sk.tuke.fei.kpi.dp;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.info.*;

@OpenAPIDefinition(
    info = @Info(
        title = "Content management system",
        version = "1.0",
        description = "Content management system for KPI magazine",
        license = @License(name = "MIT", url = "https://opensource.org/licenses/MIT"),
        contact = @Contact(url = "https://kpi.fei.tuke.sk", name = "Pavol Dlugoš", email = "pavol.dlugos@gmail.com")
    )
)
public class Application {

    public static void main(String[] args) {
        Micronaut.run(Application.class, args);
    }
}
