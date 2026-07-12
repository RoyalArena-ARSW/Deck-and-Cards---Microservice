# Etapa 1: Compilación (Build)
FROM maven:3.9.4-eclipse-temurin-17-alpine AS build
WORKDIR /app

# Copiar el archivo de configuración de dependencias
COPY pom.xml .

# Descargar dependencias para aprovechar la caché de capas de Docker
RUN mvn dependency:go-offline -B

# Copiar el código fuente
COPY src ./src

# Compilar y generar el JAR saltando los tests para acelerar el despliegue en la nube
RUN mvn clean package -DskipTests

# Etapa 2: Imagen de ejecución (Runtime)
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copiar el JAR generado desde la etapa de build
COPY --from=build /app/target/cards-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto por defecto de Spring Boot
EXPOSE 8080

# Comando para ejecutar la aplicación optimizando el uso de memoria en contenedores
ENTRYPOINT ["java", "-jar", "app.jar"]