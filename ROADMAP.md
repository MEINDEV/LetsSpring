# 6-Month Backend Engineering Roadmap (Java 21 + Spring Boot 3)

**Pace:** about 10 hrs/week, 26 weeks. **Weekly rhythm:**
- 3 lesson sessions (~1.5 h each): concept + examples you type and run
- Exercises after each lesson (~2 h total)
- 1 graded weekly task (~3 h), reviewed and scored
- Checkpoint at the end of each phase (quiz + timed coding task, 70% to pass)

---

## Phase 1: Core Java, Intermediate to Advanced (Weeks 0–8)
| Week | Topic | Weekly task (graded) |
|---|---|---|
| 0 | **Setup**: JDK 21, Maven, VS Code Java pack, Git basics, first Maven project | Hello Maven + first Git commit |
| 1 | Modern Java: `var`, records, enums, switch expressions, text blocks, String vs StringBuilder, `equals`/`hashCode`, immutability | Immutable `Money` and `BankAccount` model |
| 2 | OOP design: SOLID, composition over inheritance, interfaces (default/static/sealed), abstract classes; exceptions (checked vs unchecked, custom, try-with-resources) | Payment processing module with sealed types and custom exceptions |
| 3 | Generics (bounded types, wildcards, PECS) and the Collections framework (List/Set/Map/Queue implementations, Big-O, Comparable/Comparator) | Generic in-memory `Repository<T, ID>` and an LRU cache |
| 4 | Functional Java: lambdas, functional interfaces, method references, Streams API, Collectors (groupingBy, partitioningBy), Optional | Sales report engine over a CSV dataset |
| 5 | Testing: JUnit 5, AssertJ, Mockito, TDD, parameterized tests; logging with SLF4J/Logback | TDD a `ShoppingCart` with discount rules |
| 6 | Concurrency I: threads, ExecutorService, Future, CompletableFuture, `synchronized`, locks, atomics, concurrent collections | Parallel web-price aggregator (simulated) |
| 7 | Concurrency II and the JVM: **virtual threads** (Java 21), Java Memory Model, JVM memory, GC basics; java.time, NIO.2 files | Log file analyzer using virtual threads |
| 8 | Design patterns in practice (Builder, Factory, Strategy, Observer, Decorator, Template Method) + **Phase 1 capstone** | **Capstone 1:** CLI Library Management System (tested, layered) + **Checkpoint 1** |

## Phase 2: Spring Boot and Data (Weeks 9–14)
| Week | Topic | Weekly task |
|---|---|---|
| 9 | HTTP and REST fundamentals; Spring Core: IoC, DI, bean lifecycle and scopes, `@Configuration`, profiles, `@ConfigurationProperties` | Config-driven notification service |
| 10 | Spring Boot Web: `@RestController`, DTOs, Bean Validation, `ResponseEntity`, global error handling (`@RestControllerAdvice`, ProblemDetail), OpenAPI/Swagger | Product Catalog REST API (in-memory) |
| 11 | SQL with PostgreSQL in Docker: joins, indexes, transactions; `JdbcClient`; Flyway migrations | Catalog API on PostgreSQL with Flyway |
| 12 | Spring Data JPA/Hibernate: entities, relationships, fetch types, **N+1 problem**, `@Transactional`, pagination/sorting, projections, auditing | Blog API (posts, comments, tags) |
| 13 | Testing Spring apps: `@WebMvcTest`, `@DataJpaTest`, `@SpringBootTest`, MockMvc, **Testcontainers** | Full test suite for the Blog API |
| 14 | **Capstone 2:** Task Management API (layered architecture, validation, docs, tests) | **Checkpoint 2** |

## Phase 3: Production-Grade Backend (Weeks 15–20)
| Week | Topic | Weekly task |
|---|---|---|
| 15 | Spring Security 6: filter chain, authentication, BCrypt, JWT, method security | Secure the Task API with JWT |
| 16 | OAuth2/OIDC resource server with Keycloak, roles/scopes, CORS, OWASP Top 10 for APIs | Role-based access with Keycloak |
| 17 | Performance: caching (Spring Cache + Redis), `@Async`, scheduling, DB connection pooling (HikariCP) | Add Redis caching with metrics before and after |
| 18 | Observability: structured logging, Actuator, Micrometer, Prometheus + Grafana, distributed tracing | Dashboards and alerts for the Task API |
| 19 | Docker (multi-stage builds), docker-compose, 12-factor config, CI with GitHub Actions | Containerize the app and set up a CI pipeline |
| 20 | Advanced API design: versioning, idempotency, rate limiting, `RestClient`, resilience (Resilience4j: retry, circuit breaker) | Resilient client for an external API + **Checkpoint 3** |

## Phase 4: Distributed Systems and Final Project (Weeks 21–26)
| Week | Topic | Weekly task |
|---|---|---|
| 21 | Microservices: decomposition, Spring Cloud Gateway, service-to-service calls, config | Split the monolith into 2 services + gateway |
| 22 | Messaging with Kafka: producers/consumers, Spring Kafka, idempotent consumers, DLQ, **Outbox pattern** | Event-driven order notifications |
| 23 | Data consistency: Saga pattern, eventual consistency; system design basics (scaling, load balancing, DB replication) | Design document + Saga implementation |
| 24–25 | **Final capstone:** E-commerce Order Platform (user, catalog, order, payment-mock services; Kafka; JWT; Redis; observability; Docker Compose; CI) | Built over 2 weeks with milestone reviews |
| 26 | Kubernetes basics (deploy the capstone), interview prep: Java/Spring Q&A, system design mock, portfolio and README polish | **Final assessment** |

---

### Outcomes after 6 months
- You can write idiomatic, tested Java 21 code and explain the design choices behind it.
- You can build secure, observable, containerized Spring Boot REST services on PostgreSQL.
- You understand microservices and event-driven design with Kafka.
- You have 3 portfolio projects on GitHub, which is the level expected of a junior to mid-level backend developer.
