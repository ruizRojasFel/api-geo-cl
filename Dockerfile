# =====================================================
# STAGE 1: Build — Compila la aplicación
# =====================================================
# Usa una imagen con Maven y Java 17 para compilar
FROM maven:3.9-eclipse-temurin-17 AS build

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar solo pom.xml primero para cachear dependencias
# Si pom.xml no cambia, Docker reutiliza esta capa
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiar el código fuente
COPY src ./src

# Compilar el proyecto sin correr tests (los tests corren en CI/CD)
# Genera el .jar en target/
RUN mvn package -DskipTests -B

# =====================================================
# STAGE 2: Run — Ejecuta la aplicación
# =====================================================
# Usa una imagen más liviana solo con Java (sin Maven)
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copiar solo el .jar compilado desde la etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Puerto que expone la aplicación
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]