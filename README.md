# Redakcny_system_be
Backend part of thesis Collaborative markdown editor

## Set up postgresql database locally using docker
``````dockerfile
docker run -d --name local_postgres -v my_dbdata:/var/lib/postgresql/data -p 5432:5432 -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=sa -e POSTGRES_DB=postgres -d postgres:latest
``````
