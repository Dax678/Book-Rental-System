# Używamy obrazu JDK
FROM eclipse-temurin:21-jdk-alpine

# Ustawiamy katalog roboczy
WORKDIR /app

# Kopiujemy plik jar (najpierw zbuduj: mvn clean package)
COPY target/Book-Rental-System-0.0.1-SNAPSHOT.jar app.jar

# Komenda uruchamiająca
ENTRYPOINT ["java", "-jar", "app.jar"]
