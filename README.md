Just for training

To build an image of the **orders** service:
```bash
docker build --build-arg APP_VERSION=1.0.0 -t luxoft-orders .
```

To run this service just use the **docker-compose** features (this way is faster)
```bash
docker-compose up -d
```

Don't forget to create an **.env** file (or use command line arguments) <br>
All env arguments you can see in the **.env.dist** file
