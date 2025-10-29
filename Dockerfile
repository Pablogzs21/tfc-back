# ---------- Build ----------
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn -B clean package -DskipTests

# ---------- Run ----------
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Render pasa el puerto en la variable PORT; Spring lo leerá si lo configuramos.
ENV PORT=8080
EXPOSE 8080

CMD ["java","-jar","app.jar"]
