docker build -t megad_server/test --no-cache .
docker run -p 8080:8080 -t megad_server/test