# API GEO CL

API REST en Spring Boot con datos geopolíticos de Chile: **regiones, provincias y comunas**.

## Stack

- Java 17
- Spring Boot 3.5.13 (Web, Data JPA, Validation)
- PostgreSQL
- OpenAPI/Swagger (`springdoc-openapi`)
- Maven
- Docker / Docker Compose

## Qué incluye hoy

- Endpoints REST bajo `/api/v1`
- Búsquedas por nombre con normalización (sin tildes, case-insensitive, espacios normalizados, soporte para `ñ`)
- Relaciones navegables:
  - región → provincias
  - provincia → comunas
  - región → comunas (por nombre de región)
- Manejo global de errores en JSON
- Datos semilla en `data.sql`:
  - 16 regiones
  - 56 provincias
  - 346 comunas

## Endpoints

| Recurso | Método | Ruta | Descripción |
|---|---|---|---|
| Regiones | GET | `/api/v1/regiones` | Lista todas las regiones |
| Regiones | GET | `/api/v1/regiones/{id}` | Obtiene una región por ID |
| Regiones | GET | `/api/v1/regiones/buscar?nombre=...` | Busca regiones por nombre |
| Regiones | GET | `/api/v1/regiones/{id}/provincias` | Lista provincias de una región |
| Regiones | GET | `/api/v1/regiones/comunas?nombre=...` | Lista comunas de una región por nombre |
| Provincias | GET | `/api/v1/provincias` | Lista todas las provincias |
| Provincias | GET | `/api/v1/provincias/{id}` | Obtiene una provincia por ID |
| Provincias | GET | `/api/v1/provincias/buscar?nombre=...` | Busca provincias por nombre |
| Provincias | GET | `/api/v1/provincias/{id}/comunas` | Lista comunas de una provincia |
| Comunas | GET | `/api/v1/comunas` | Lista todas las comunas |
| Comunas | GET | `/api/v1/comunas/{id}` | Obtiene una comuna por ID |
| Comunas | GET | `/api/v1/comunas/buscar?nombre=...` | Busca comunas por nombre |

## Respuestas

- `RegionDTO`: `id`, `numero`, `nombre`, `capital`
- `ProvinciaDTO`: `id`, `nombre`, `capital`, `regionId`, `regionNombre`
- `ComunaDTO`: `id`, `nombre`, `codigoCut`, `provinciaId`, `provinciaNombre`, `regionId`, `regionNombre`

Ejemplo de error:

```json
{
  "status": 404,
  "error": "Not Found",
  "mensaje": "Región con id 999 no encontrada",
  "timestamp": "2026-05-04T01:30:00"
}
```

## Swagger / OpenAPI

- UI: `http://localhost:8080/swagger-ui/index.html`
- Docs JSON: `http://localhost:8080/v3/api-docs`

## Ejecutar con Docker Compose (recomendado)

1. Copia variables de entorno:

```bash
cp .env.example .env
```

2. Completa `.env` con:

```env
DB_NAME=db_api_geo_cl
DB_USER=user_api_geo_cl
DB_PASSWORD=pass_api_geo_cl_2026
```

3. Levanta servicios:

```bash
docker compose up --build
```

Servicios:
- API: `http://localhost:8080`
- PostgreSQL: `localhost:5432`

## Ejecutar local (sin Docker)

Requisitos: Java 17, PostgreSQL y base creada (por defecto `db_api_geo_cl`).

Exporta variables para `application.yaml`:

```bash
export DB_USERNAME=user_api_geo_cl
export DB_PASSWORD=pass_api_geo_cl_2026
./mvnw spring-boot:run
```

## Tests y cobertura

```bash
./mvnw test
```

Reporte Jacoco:

```bash
./mvnw jacoco:report
```

Salida: `target/site/jacoco/index.html`

## CI/CD

Workflow: `.github/workflows/deploy.yml`

En `main`:
1. Compila y ejecuta tests
2. Genera y publica reporte Jacoco en GitHub Pages
3. Construye imagen Docker
4. Dispara deploy en Render vía `RENDER_DEPLOY_HOOK`

## CORS habilitado para

- `http://localhost:4200`
- `http://localhost:4300`
- `http://localhost:5173`
- `http://localhost:8080`
- `http://localhost:3001`
- `https://front-apis.vercel.app`

---

## Developer

### **Felipe Andrés Ruiz Rojas**

[![LinkedIn](https://img.shields.io/badge/LinkedIn-felandres-blue?logo=linkedin)](https://www.linkedin.com/in/felandres)
[![Website](https://img.shields.io/badge/Website-%40felruiz--dev-lightblue)](https://felruiz-dev.vercel.app/)

---

## License

[![License](https://img.shields.io/badge/License-MIT-yellow)](https://github.com/ruizRojasFel/api-geo-cl?tab=MIT-1-ov-file)

---

Copyright © 2026 Felipe Andrés Ruiz Rojas.
