# Estágio de Build (Compila o código novo baixado do GitHub)
FROM maven:3.9.6-eclipse-temurin-21 AS build
COPY . .
RUN mvn clean package -DskipTests

# Estágio de Execução (Roda o Jar atualizado)
FROM eclipse-temurin:21-jdk
COPY --from=build /target/*.jar app.jar

# Define a porta exposta pelo Render
EXPOSE 8080

# Comando de inicialização
ENTRYPOINT ["java", "-jar", "app.jar"]