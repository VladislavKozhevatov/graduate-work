Выпускная дипломная работа
Проект размещения объявлений

Необходимая реализа
Авторизация и аутентификация пользователей.
Распределение ролей между пользователями: пользователь и администратор.
CRUD-операции для объявлений и комментариев: администратор может удалять или редактировать все объявления и комментарии, а пользователи — только свои.
Возможность для пользователей оставлять комментарии под каждым объявлением.
Показ и сохранение картинок объявлений, а также аватарок пользователей.

1. Используемые технологии
   JDK 11,
   Project Maven,
   Spring Boot 3.4.5,
   база данных PostgreSQL

2. Шаги по запуску
   Установка зависимостей:
   модуль Spring Web, зависимость для работы с postgresql,
   зависимость для работы с jpa,
   зависимость для работы с liquibase,
   библиотека Lombok,
   зависимость для работы со Swagger,
   зависимость для работы с springdoc-openapi,
   зависимости для тестирования приложения, зависимость для работы с Security

Настройка конфигураций (application.properties)
spring.datasource.url=${DB_URL:jdbc:postgresql://localhost:5432/prod_db} spring.datasource.username=${DB_USERNAME:prod_user} spring.datasource.password=${DB_PASSWORD:prod_password}

spring.jpa.show-sql=false spring.jpa.hibernate.ddl-auto=validate spring.liquibase.enabled=true

logging.level.ru.skypro.homework=INFO logging.level.org.springframework.security=WARN logging.level.org.hibernate.SQL=WARN

spring.web.cors.allowed-origins=${CORS_ORIGINS:https://yourdomain.com}

springdoc.swagger-ui.enabled=false

server.error.include-stacktrace=never server.error.include-binding-errors=never server.error.include-message=always server.error.include-exception=false

3. Запуск приложения.
   Команды для сборки приложения
   mvn clean package

Команда для запуска приложения
java -jar target/AdvertisementApplication.jar

4. Над проектом работали:
  Кожеватов Владислав