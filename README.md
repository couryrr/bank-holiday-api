# bank-holiday-api

An instance of redit is needed you can run
> docker run --name redis-poc -p 6379:6379 -d --rm redis

MySql is running on port 13306
> docker run --name mysql-poc -e MYSQL_ALLOW_EMPTY_PASSWORD=yes -p 13306:3306 --rm mysql

Upcoming additions add

- Add docker-compose
- Persist MySql data
- Look up Canadian Holidays
