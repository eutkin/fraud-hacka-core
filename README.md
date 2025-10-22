# Correlator

## Зависимости

- Apache Kafka, топики:
    - топик на чтение событий
    - топик на запись событий
- Core API
    - http ручка атрибутов
    - http ручка правил

## Конфигурация

| Параметр                           | Описание                          | По умолчанию |
|------------------------------------|-----------------------------------|--------------|
| env.MANAGEMENT_PORT                | HTTP порт для служебных ручек     | 8085         |
| env.OUTCOME_EVENT_PRODUCER_TOPIC   | Имя исходящего топика с событиями |              |
| env.OUTCOME.EVENT.PRODUCE.TIMEOUT  | Таймаут на отправку               | 100ms        |
| env.INCOME.EVENT.CONSUMER.TOPIC    | Имя входящего топика с событиями  |              |
| env.INCOME.EVENT.CONSUMER.GROUP_ID | Имя группы Kafka consumer         |              |

## CI

```bash
./mvnw package -Dpackaging=docker-native
```

## Служебные ручки

```http request
GET /rules
```

Покажет внутреннее состояние правил.

```http request
GET /attributes
```

Покажет внутреннее состояние правил.
