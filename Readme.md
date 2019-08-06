- cd /docker: docker-compose up -d
- create 2 run configs with different active profiles: 
    - local-dev: runs app without fixtures
    - (local-dev, load-fixtures): recreates db and runs app with fixtures
    
   