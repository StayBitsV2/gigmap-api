FROM maven:3.8.5-openjdk-17 AS build

WORKDIR /app

# Copia el POM al directorio de trabajo
COPY pom.xml .

# Descarga dependencias sin compilar código
RUN mvn dependency:go-offline

# Copia el código fuente
COPY src ./src

# Compila el proyecto (sin ejecutar tests)
RUN mvn clean package -DskipTests

# Verifica que el jar esté en target
RUN ls -la target

# Imagen final
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copia el jar desde la imagen de compilación
COPY --from=build /app/target/GigMap-api-0.0.1-SNAPSHOT.jar /app/GigMap-api.jar

EXPOSE 8080

ENTRYPOINT [ "java", "-jar", "/app/GigMap-api.jar" ]