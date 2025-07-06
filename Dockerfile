# Usar imagen base de Java
FROM openjdk:17-jdk-slim AS build

# Establecer directorio de trabajo
WORKDIR /app

# Copiar archivos
COPY . .

# Construir el proyecto (con Maven)
RUN ./mvnw -ntp -Pprod -DskipTests package

# Usar una imagen ligera para ejecutar la app
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copiar el archivo JAR generado
COPY --from=build /app/target/*.jar app.jar

# Exponer el puerto (por defecto JHipster usa 8080)
EXPOSE 8080

# Comando para ejecutar la app
CMD ["java", "-jar", "app.jar"]
