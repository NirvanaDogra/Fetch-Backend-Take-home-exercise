Running the Application

Prerequisites
Docker installed and running
Building and Running the Docker Image
To build and run the Docker image, follow these steps:

<h3>Build the Docker image :
[Download docker image](https://drive.google.com/drive/folders/1XVp8-okPm0g3DBSaMH9EVA9FNX90IBBN?usp=drive_link)

<h3>Bash</h3>

<code>
docker load < img.tar
</code>

Use code with caution.

Replace your-image-name with a desired name for your image.

Run the Docker container:

Bash
docker run -p 8080:8080 --runtime=runc -t -d <image-id>
Use code with caution.

Replace <image-id> with the actual ID of your Docker image. This command maps port 8080 of the container to port 8080 of your host machine and starts the container in detached mode.

Accessing the API
Once the Docker container is running, you can access the API documentation using Swagger UI:

Open a web browser and navigate to:
[Swagger UI](http://localhost:8080/swagger-ui/index.html#/Receipt%20Processor/processReceipts)
This will open the Swagger UI interface where you can explore the available API endpoints, view documentation, and test requests.

Additional Notes:
Replace <image-id>: Ensure you replace <image-id> with the actual ID of your Docker image.
Docker Compose: For more complex setups, consider using Docker Compose to manage multiple containers and their dependencies.
Environment Variables: You might need to set environment variables for your application. Refer to the application's documentation for specific requirements.
API Documentation: The Swagger UI provides detailed information about the API endpoints, including request/response formats and examples.
By following these steps, you should be able to successfully run the application in a Docker container and interact with its API.

