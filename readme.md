# KCThack

Backend сервиса для организации хакатонов и других мероприятий: публикация событий, регистрация участников, управление профилями, навыками и достижениями. Организаторы управляют мероприятиями и пользователями через административное API, участники просматривают расписание и записываются на события.

В репозитории находится серверное приложение на Java. Maven-модуль расположен в каталоге `KCT_HACK`, основной Java-пакет — `com.hackathon.KDT_HACK`, имя приложения в конфигурации — `KDT_HACK`.

## Содержание

- [Возможности](#возможности)
- [Стек](#стек)
- [Структура проекта](#структура-проекта)
- [Локальный запуск](#локальный-запуск)
- [Авторизация и роли](#авторизация-и-роли)
- [API](#api)
- [Модели данных](#модели-данных)
- [Ошибки](#ошибки)
- [Тестирование](#тестирование)
- [Текущие ограничения](#текущие-ограничения)

## Возможности

- Регистрация и вход с выдачей JWT.
- Личный профиль с контактами, специализацией, уровнем подготовки и навыками.
- Публичный список мероприятий и подробная информация о каждом событии.
- Индивидуальная регистрация на мероприятие и просмотр списка участников.
- Создание, редактирование и удаление мероприятий администратором.
- Управление пользователями: редактирование, блокировка и мягкое удаление.
- Справочник навыков с разделением на `HARD` и `SOFT`.
- Начисление баллов и автоматическая выдача достижений при достижении порогов.

## Стек

| Компонент | Технология |
| --- | --- |
| Язык | Java 21 |
| Framework | Spring Boot 4.0.2, Spring MVC |
| Доступ к данным | Spring Data JPA / Hibernate |
| База данных | PostgreSQL |
| Безопасность | Spring Security, JWT (JJWT 0.12.1), BCrypt |
| Валидация | Jakarta Bean Validation |
| Документация API | springdoc OpenAPI / Swagger UI, зависимость 2.8.5 |
| Сборка | Maven, Maven Wrapper 3.9.12 |
| Тесты | JUnit 5, Mockito, Spring Boot Test; H2 подключена в test scope |

Версии указаны по `KCT_HACK/pom.xml` и настройкам Maven Wrapper.

## Структура проекта

```text
KCThack/
├── readme.md
└── KCT_HACK/                         # Maven-модуль backend
    ├── pom.xml                      # Зависимости и настройки сборки
    ├── mvnw / mvnw.cmd               # Maven Wrapper для Linux/macOS и Windows
    ├── .mvn/wrapper/                # Версия и источник Maven
    └── src/
        ├── main/
        │   ├── java/com/hackathon/KDT_HACK/
        │   │   ├── KdtHackApplication.java  # Точка входа
        │   │   ├── config/          # Security, JWT, BCrypt, dev-данные
        │   │   ├── controller/      # REST-эндпоинты
        │   │   ├── dto/             # Модели запросов и ответов
        │   │   ├── entity/          # JPA-сущности и модель Schedule
        │   │   ├── enums/           # Роли, статусы, категории и уровни
        │   │   ├── exception/       # Общий обработчик исключений
        │   │   ├── repository/      # Репозитории Spring Data JPA
        │   │   └── service/         # Бизнес-логика
        │   └── resources/
        │       ├── aplication.properties.example
        │       └── aplication-dev.properties.example
        └── test/java/com/hackathon/KDT_HACK/
            ├── JwtCoreTest.java
            └── KdtHackApplicationTests.java
```

Основной поток обработки запроса: `controller → service → repository → PostgreSQL`.
Контроллеры принимают HTTP-запросы, сервисы выполняют бизнес-операции, репозитории работают с базой данных. `TokenFilter` проверяет JWT до вызова контроллера, `GlobalExceptionHandler` формирует ответы на исключения.

| Контроллер | Назначение |
| --- | --- |
| `SecurityController` | Регистрация и вход |
| `MainController` | Текущий профиль, его изменение и смена пароля |
| `ScheduleController` | Публичное расписание |
| `EventRegistrationController` | Запись на события и список регистраций |
| `SkillsController` | Чтение справочника навыков |
| `AdminController` | Управление пользователями, навыками, баллами и событиями |
| `JudgeController` | Заготовка обработки результатов хакатона |

## Локальный запуск

### Требования

- JDK 21.
- PostgreSQL с созданной базой данных и доступным пользователем.
- Доступ к Maven Central при первой сборке. Отдельная установка Maven не обязательна: в репозитории есть Wrapper.
- OpenSSL для генерации JWT-ключа в приведённом ниже примере.

### 1. Настройка приложения

Из корня репозитория перейдите в Maven-модуль и скопируйте основной шаблон:

```bash
cd KCT_HACK
cp src/main/resources/aplication.properties.example src/main/resources/application.properties
```

В имени исходного шаблона `aplication` пропущена буква `p`. Рабочий файл должен называться **`application.properties`**, иначе Spring Boot не загрузит его автоматически. Файлы `*.example` также не загружаются автоматически.

В `application.properties` укажите подключение к своей базе. В шаблоне используются:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=${DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=update
spring.jpa.open-in-view=false

jwt.secret.key=${JWT_SECRET_KEY}
jwt.lifetime=${JWT_LIFETIME}
```

`ddl-auto=update` позволяет Hibernate создавать и обновлять таблицы при запуске. Саму базу данных нужно создать заранее.

Задайте переменные в том же терминале, из которого запускаете приложение:

```bash
read -rsp 'Пароль PostgreSQL: ' DB_PASSWORD
echo
export DB_PASSWORD
export JWT_SECRET_KEY="$(openssl rand -base64 32)"
export JWT_LIFETIME=3600000
```

| Переменная | Назначение |
| --- | --- |
| `DB_PASSWORD` | Пароль пользователя PostgreSQL |
| `JWT_SECRET_KEY` | Base64-строка, содержащая не менее 32 байт ключа после декодирования |
| `JWT_LIFETIME` | Срок действия JWT в миллисекундах; `3600000` — один час |

Для сохранения действия токенов между перезапусками используйте один и тот же JWT-ключ. Рабочие файлы `application*.properties` исключены из Git правилами `KCT_HACK/.gitignore`.

### 2. Запуск

```bash
bash mvnw spring-boot:run
```

В Windows используйте `mvnw.cmd spring-boot:run`, а переменные окружения задайте средствами своей оболочки.

По умолчанию приложение доступно по адресу `http://localhost:8080`, базовый путь API — `/api`. Порт можно изменить через `server.port` или переменную `SERVER_PORT`.

Проверка публичного эндпоинта:

```bash
curl http://localhost:8080/api/schedule
```

В пустой базе ожидается ответ `[]`.

Сборка исполняемого JAR и запуск без тестов на этапе упаковки:

```bash
bash mvnw -DskipTests package
java -jar target/KDT_HACK-0.0.1-SNAPSHOT.jar
```

### Dev-профиль

`DataLoader` включается только с профилем `dev`. Он заполняет пустые справочники навыков и достижений, а при отсутствии пользователей пытается создать три учётные записи:

| Логин | Роль |
| --- | --- |
| `ivan_user` | `USER` |
| `maria_admin` | `ADMIN` |
| `alex_judge` | `JUDGE` |

В текущем `DataLoader` есть ошибки настройки внедрения паролей и конструктора, поэтому запуск выше описан без профиля `dev`. Подробности — в [текущих ограничениях](#текущие-ограничения). Отдельного API для назначения ролей пока нет: обычная регистрация создаёт пользователя с ролью `USER`.

## Авторизация и роли

После `POST /api/auth/signin` передавайте полученный токен в каждом защищённом запросе:

```http
Authorization: Bearer <token>
```

Сессии на сервере не создаются. JWT содержит ID пользователя в `sub`, роль в `role` и версию токена в `tv`. При смене пароля, блокировке и мягком удалении версия увеличивается, поэтому ранее выданные токены перестают подходить. После смены пароля нужно войти заново.

| Роль | Назначение |
| --- | --- |
| `USER` | Участник мероприятия |
| `ADMIN` | Администратор сервиса |
| `JUDGE` | Судья: просмотр регистраций и доступ к заготовке API результатов |
| `PARTNER` | Значение есть в модели; отдельные операции партнёра не реализованы |

В Spring Security роли представлены как `ROLE_USER`, `ROLE_ADMIN` и т. д. Иерархия ролей не настроена: `ADMIN` не получает автоматически операции, ограниченные ролью `USER`.

## API

Пути в таблицах приведены полностью, включая `/api`. Тела запросов и ответы используют JSON, если отдельно не указан другой формат. Для запросов с JSON-телом передавайте `Content-Type: application/json`.

`{id}` в маршрутах пользователей — строковый UUID. ID событий, навыков и регистраций — числовые `Long`. Даты передаются как `YYYY-MM-DD`, дата и время — как `YYYY-MM-DDTHH:mm:ss`, без часового пояса (`LocalDateTime`).

### Регистрация и вход

| Метод | Путь | Доступ | Успешный ответ |
| --- | --- | --- | --- |
| `POST` | `/api/auth/signup` | Публичный | `201`, сообщение о регистрации |
| `POST` | `/api/auth/signin` | Публичный | `200`, объект с JWT |

Регистрация:

```bash
curl -X POST http://localhost:8080/api/auth/signup \
  -H 'Content-Type: application/json' \
  -d '{
    "name": "Иван",
    "lastname": "Петров",
    "username": "ivan",
    "birthday": "2000-05-15",
    "email": "ivan@example.com",
    "password": "ExamplePass123!"
  }'
```

Имя и фамилия: от 2 до 100 символов, допустимы буквы, пробелы, дефис и апостроф. Пароль: от 8 до 100 символов. `username` и `email` должны быть непустыми и уникальными. Передавайте `birthday`: поле обязательно в базе, хотя в DTO регистрации отдельная проверка его наличия отсутствует.

Ответ:

```json
{
  "message": "успешная регистрация"
}
```

Вход:

```bash
curl -X POST http://localhost:8080/api/auth/signin \
  -H 'Content-Type: application/json' \
  -d '{"username":"ivan","password":"ExamplePass123!"}'
```

Ответ:

```json
{
  "token": "<jwt>"
}
```

Повторная регистрация с занятым email или логином возвращает `400` и объект `{"message":"..."}`. Неверные данные входа, заблокированный или удалённый аккаунт приводят к `401`.

### Профиль пользователя

| Метод | Путь | Доступ | Успешный ответ |
| --- | --- | --- | --- |
| `GET` | `/api/user` | Любой авторизованный пользователь | `200`, текущий `User` с навыками и достижениями |
| `POST` | `/api/user/{id}` | `USER`, только собственный ID | `200`, обновлённый `User` |
| `POST` | `/api/user/{id}/change-password` | `USER`, только собственный ID | `200`, сообщение |

Получение собственного профиля:

```bash
curl http://localhost:8080/api/user \
  -H "Authorization: Bearer $TOKEN"
```

Здесь и далее `$TOKEN` — переменная с JWT из ответа входа. Для административных запросов используйте токен аккаунта `ADMIN`.

Тело обновления (`UpdateUserRequest`, то же тело используется администратором):

```json
{
  "name": "Иван",
  "lastName": "Петров",
  "username": "ivan",
  "birthday": "2000-05-15",
  "avatar": "avatar.png",
  "email": "ivan@example.com",
  "phone": "+79123456789",
  "telegram": "https://t.me/ivan_user",
  "github": "https://github.com/ivan",
  "job": "BACK",
  "level": "BEGINNER",
  "skills": [
    {"skillId": 1, "level": 5}
  ]
}
```

Обязательны `name`, `lastName`, `username`, `birthday` в прошлом и `email`. Телефон при наличии должен соответствовать `+79` и ещё девяти цифрам; Telegram и GitHub проверяются по шаблонам ссылок. Уровень отдельного навыка — от 1 до 10, `skillId` должен существовать.

Обновление заменяет значения полей и набор навыков: отсутствующие в `skills` навыки удаляются, а `skills: []` или отсутствие этого поля очищает набор. При регистрации поле фамилии называется `lastname`, при обновлении — `lastName`.

Тело смены пароля:

```json
{
  "oldPassword": "ExamplePass123!",
  "newPassword": "AnotherPass123!"
}
```

Ответ: `{"message":"Password changed successfully"}`. Неверный старый пароль возвращает `400`.

### Мероприятия и расписание

| Метод | Путь | Доступ | Успешный ответ |
| --- | --- | --- | --- |
| `GET` | `/api/schedule` | Публичный | `200`, массив `Schedule` |
| `GET` | `/api/schedule/admin` | Публичный, алиас списка | `200`, массив `Schedule` |
| `GET` | `/api/schedule/{id}` | Публичный | `200`, `Schedule` |
| `POST` | `/api/admin/schedule/create-event` | `ADMIN` | `201`, созданный `Schedule` |
| `PUT` | `/api/admin/schedule/update-event/{id}` | `ADMIN` | `200`, обновлённый `Schedule` |
| `DELETE` | `/api/admin/schedule/delete-event/{id}` | `ADMIN` | `200`, без тела |

Создание принимает `EventCreateDto` со списком **ID навыков**:

```json
{
  "name": "Осенний хакатон",
  "briefDescription": "Два дня командной разработки",
  "description": "Хакатон по созданию цифровых сервисов для города.",
  "skillIds": [1, 2],
  "startDateTime": "2026-11-20T10:00:00",
  "endDateTime": "2026-11-22T18:00:00"
}
```

`briefDescription` и `description` обязательны, минимум 5 символов; обе даты обязательны. Передавайте непустое имя и массив `skillIds` с уникальными существующими ID; без привязки навыков используйте `[]`. Несуществующий ID навыка приводит к `400`.

Обновление принимает `Schedule` со списком **объектов навыков**:

```json
{
  "name": "Осенний хакатон — финал",
  "briefDescription": "Обновлённая программа мероприятия",
  "description": "Разработка проектов и презентация решений перед жюри.",
  "skills": [
    {"id": 1, "name": "Java", "category": "HARD"}
  ],
  "startDateTime": "2026-11-20T10:00:00",
  "endDateTime": "2026-11-22T19:00:00"
}
```

Используйте объекты существующих навыков из справочника. Все поля этой модели, кроме `id`, обязательны; строки `name`, `briefDescription`, `description` — минимум 5 символов. `id` в теле нужно опустить или передать `null`: ID берётся из пути, а поле в DTO помечено `@Null`. Список `skills` может быть пустым.

Ответ `Schedule` имеет ту же форму, но дополнительно содержит числовой `id`. Несуществующее событие при получении, изменении или удалении возвращает `404`.

### Регистрация на мероприятие

| Метод | Путь | Доступ | Успешный ответ |
| --- | --- | --- | --- |
| `POST` | `/api/events/{eventId}/register-solo` | `USER` | `200`, сообщение |
| `GET` | `/api/events/{eventId}/registrations` | `ADMIN`, `JUDGE` | `200`, массив `EventRegistrationDto` |

Индивидуальная регистрация не требует тела запроса: пользователь определяется по JWT.

```bash
curl -X POST http://localhost:8080/api/events/1/register-solo \
  -H "Authorization: Bearer $TOKEN"
```

Ответ: `{"message":"Успешная регистрация"}`. Повторная запись того же пользователя на то же событие возвращает `400`. Создаются тип `SINGLE` и статус `ACTIVE`.

Пример ответа списка регистраций:

```json
[
  {
    "id": 1,
    "userId": "6b92b260-72f1-43a9-9fd6-a1ca42c3182d",
    "fullName": "Иван Петров",
    "scheduleId": 1,
    "type": "SINGLE",
    "status": "ACTIVE",
    "registeredAt": "2026-11-04T15:30:00"
  }
]
```

### Навыки

| Метод | Путь | Доступ | Успешный ответ |
| --- | --- | --- | --- |
| `GET` | `/api/skills/getall-skills` | Любой авторизованный пользователь | `200`, массив `Skills` |
| `POST` | `/api/admin/add-skills` | `ADMIN` | `200`, сообщение |
| `DELETE` | `/api/admin/delete-skills` | `ADMIN` | `200`, сообщение |

Добавление навыков принимает массив `SkillAddDto`:

```json
[
  {"name": "Java", "skillsCategory": "HARD"},
  {"name": "Командная работа", "skillsCategory": "SOFT"}
]
```

Уже существующие имена пропускаются. Ответ: `{"message":"Skills added successfully"}`.

Удаление принимает массив `SkillRemoveDto`:

```json
[
  {"name": "Java"}
]
```

Ответ: `{"message":"Skills deleted successfully"}`. Если хотя бы одно имя не найдено, возвращается `404`.

В ответе справочника поле категории называется `category`, в запросе добавления — `skillsCategory`:

```json
[
  {"id": 1, "name": "Java", "category": "HARD"}
]
```

### Администрирование пользователей и баллов

Все маршруты этого раздела требуют роль `ADMIN`.

| Метод | Путь | Тело | Успешный ответ |
| --- | --- | --- | --- |
| `GET` | `/api/admin/getall-users` | — | `200`, массив `User` |
| `POST` | `/api/admin/update-user/{id}` | `UpdateUserRequest` | `200`, обновлённый `User` |
| `POST` | `/api/admin/block-user/{id}` | — | `200`, `User` со статусом `BLOCKED` |
| `POST` | `/api/admin/delete-user/{id}` | — | `200`, `User` со статусом `DELETED` |
| `POST` | `/api/admin/add-points/{id}` | Целое JSON-число | `200`, обновлённый `User` |

Удаление пользователя мягкое: запись остаётся в базе, меняется статус. Блокировка и удаление также отзывают ранее выданные JWT.

Начисление 50 баллов — в теле передаётся число, без объекта-обёртки:

```bash
curl -X POST "http://localhost:8080/api/admin/add-points/$USER_ID" \
  -H "Authorization: Bearer $TOKEN" \
  -H 'Content-Type: application/json' \
  -d '50'
```

`$USER_ID` — UUID пользователя. Достижения выдаются автоматически при пересечении порогов `pointsRequired`, если соответствующие достижения заведены в базе.

### Судейство

| Метод | Путь | Доступ | Текущее поведение |
| --- | --- | --- | --- |
| `POST` | `/api/judge/hackathon-results` | `JUDGE` | Заготовка: возвращает пустую строку, не сохраняет результаты |

`ResultsHackatonRequest` пока не содержит полей. Контракт результатов и логика выставления оценок не реализованы.

### OpenAPI и Swagger UI

В приложении подключена зависимость springdoc и разрешены пути:

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`.
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`.



## Модели данных

| Модель | Основные поля и связи |
| --- | --- |
| `User` | UUID, ФИО, логин, email, контакты, дата рождения, аватар, роль, статус, специализация, уровень, баллы, дата регистрации, версия токена; коллекции навыков и достижений. Пароль исключён из JSON |
| `Skills` | `id`, `name`, `category` |
| `UserSkill` | `id`, `skill` и `level` от 1 до 10; связь с пользователем исключена из JSON |
| `Achievements` | `id`, `name`, `description`, `icon`, `pointsRequired` |
| `UserAchievements` | `id`, вложенный объект `achievements`; связь с пользователем исключена из JSON |
| `ScheduleEntity` | JPA-сущность мероприятия: описания, даты и связь многие-ко-многим с навыками |
| `Schedule` | Модель API мероприятия: `id`, `name`, `briefDescription`, `description`, `skills`, `startDateTime`, `endDateTime` |
| `EventRegistration` | Пользователь, событие, время регистрации, тип и статус; пара пользователь/событие уникальна |
| `EventRegistrationDto` | `id`, `userId`, `fullName`, `scheduleId`, `type`, `status`, `registeredAt` |

Значения перечислений:

| Тип | Значения |
| --- | --- |
| `UserRole` | `USER`, `ADMIN`, `JUDGE`, `PARTNER` |
| `UserStatus` | `ACTIVE`, `BLOCKED`, `DELETED` |
| `UserJob` | `FRONT`, `BACK`, `DESIGNER`, `PROJECT`, `GAME` |
| `UserLevel` | `BEGINNER`, `INTERMEDIATE`, `ADVANCED` |
| `SkillsCategory` | `SOFT`, `HARD` |
| `RegistrationType` | `SINGLE`, `TEAM` |
| `RegistrationStatus` | `ACTIVE`, `CANCELLED` |

## Ошибки

Исключения, обработанные `GlobalExceptionHandler`, возвращаются в формате `ErrorResponseDto`:

```json
{
  "message": "Entity not found",
  "detailedMessage": "Not found task by id: 999",
  "errorTime": "2026-11-04T15:30:00"
}
```

| Код | Когда используется |
| --- | --- |
| `400` | Ошибка валидации, недопустимые аргументы, повторная регистрация на событие, неверный старый пароль |
| `401` | Ошибка аутентификации; для защищённого маршрута также отсутствие или недействительность JWT |
| `403` | Недостаточно прав |
| `404` | `EntityNotFoundException`, например отсутствующее событие в операциях расписания |
| `409` | Нарушение ограничений базы данных (`DataIntegrityViolationException`) |
| `500` | Необработанные отдельно исключения |


## Тестирование

Команды выполняются из `KCT_HACK`:

```bash
# Все тесты, включая загрузку контекста Spring
bash mvnw test

# Только модульные проверки JWT, без запуска приложения и БД
bash mvnw -Dtest=JwtCoreTest test
```

`JwtCoreTest` проверяет генерацию и чтение токена, отклонение некорректного токена и короткого ключа. `KdtHackApplicationTests` проверяет загрузку контекста.

