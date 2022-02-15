# Redakcny_system_be
Backend part of thesis Collaborative markdown editor

## Set up postgresql database locally using docker
``````dockerfile
docker run -d --name local_postgres -v my_dbdata:/var/lib/postgresql/data -p 5432:5432 -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=sa -e POSTGRES_DB=postgres -d postgres:latest
``````
## Deploy backend app to docker locally

1. Build app based on Dockerfile.
    ``````
    docker build -t redakcny_system_be .    
    ``````
   
2. Run built image inside container.
    ``````   
   docker run -e LOGIN_SUCCESS_CALLBACK_URL='http://localhost:8080/login-callback' -e GITLAB_OAUTH_CLIENT_ID='afef94df75f368edbb735303e7b8fa5c7141131aed8239126055dabba80acc8c' -e GITLAB_OAUTH_CLIENT_SECRET='be993a6796915485219d444ad470d35cb3447463e4e8a57c9dc4979b1340bed3' -e GITHUB_OAUTH_CLIENT_ID='Iv1.7f311c1dd843f6fb' -e GITHUB_OAUTH_CLIENT_SECRET='02c2e04b9e24f1a65794db909597dfe67d165f53' -e JWT_GENERATOR_SIGNATURE_SECRET='E)H@McQfTjWnZr4u7x!z%C*F-JaNdRgU' -e FRONTEND_URL='http://localhost:3000' -e DATABASE_URL='jdbc:postgresql://surus.db.elephantsql.com/nyukudxs' -e DATABASE_USERNAME='nyukudxs' -e DATABASE_PASSWORD='z7Ss2VLr1qZ3wnG9K300nuiXhpUYVVrE' --name redakcny_system_be --rm -p 8080:8080 redakcny_system_be
    ``````
