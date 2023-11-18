Бла бла бла бла бла, бла бла, а если честно то суть в бла бла бла бла, потом перепишу объяснения. бла бла

Для локальной разработки, понадобиться создать 3 БД:
1. product-db
2. recommendation-db
3. review-db
Имена и Пароли 
username: "postgres"
password: "password"

Либо запустить три docker контейнера с минимальной настройкой
1. docker run -p 5432:5432 -e POSTGRES_PASSWORD=password -e POSTGRES_USER=postgres -e POSTGRES_DB=product-db -d postgres
2. docker run -p 5432:5432 -e POSTGRES_PASSWORD=password -e POSTGRES_USER=postgres -e POSTGRES_DB=recommendation-db -d postgres
3. docker run -p 5432:5432 -e POSTGRES_PASSWORD=password -e POSTGRES_USER=postgres -e POSTGRES_DB=review-db -d postgres
