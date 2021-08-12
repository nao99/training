# =====================================================================
# STEP 1: BUILD
# =====================================================================

# 1. Initialize a parent image
FROM gradle:7.1.1-jdk11 AS BUILD_IMAGE
LABEL maintainer="Luxoft <nikolai.osipov@dxc.com>"

# 2. Configure environment
ENV APP_HOME=/app_build
RUN mkdir -p $APP_HOME
WORKDIR $APP_HOME

# 3. Copy project files
COPY . .

# 4. Build application (excepting tests stage)
RUN gradle build -x test

# =====================================================================
# STEP 2: CREATE IMAGE
# =====================================================================

# 1. Initialize a parent image and configure environment
FROM openjdk:11-jre-slim
ENV APP_HOME=/app

# 2. Create tmp directory
RUN mkdir -p $APP_HOME/tmp

# 3. Create working directory
WORKDIR $APP_HOME

# 4. Define arguments
ARG APP_VERSION

# 5. Copy builded application from BUILD_IMAGE
COPY --from=BUILD_IMAGE /app_build $APP_HOME/tmp

# 6. Move the application to container root and remove tmp directory
RUN mv $APP_HOME/tmp/orders/build/libs/orders-$APP_VERSION.jar app.jar

# 7. Remove tmp files
RUN rm -rf tmp

# 8. Run the application
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
