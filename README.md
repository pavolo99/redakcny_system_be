## Micronaut 3.0.1 Documentation

- [User Guide](https://docs.micronaut.io/3.0.1/guide/index.html)
- [API Reference](https://docs.micronaut.io/3.0.1/api/index.html)
- [Configuration Reference](https://docs.micronaut.io/3.0.1/guide/configurationreference.html)
- [Micronaut Guides](https://guides.micronaut.io/index.html)
---

## Feature openapi documentation

- [Micronaut OpenAPI Support documentation](https://micronaut-projects.github.io/micronaut-openapi/latest/guide/index.html)

- [https://www.openapis.org](https://www.openapis.org)

## Feature http-client documentation

- [Micronaut HTTP Client documentation](https://docs.micronaut.io/latest/guide/index.html#httpClient)

## Set up postgresql database locally using docker

``````
docker run -d --name local_postgres -v my_dbdata:/var/lib/postgresql/data -p 5432:5432 -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=sa -e POSTGRES_DB=postgres -d postgres:latest
``````
