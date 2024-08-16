# Fetch-Backend-Take-home-exercise

<h1>Running the Application</h1>

<h3>Prerequisites</h3>
<ul>
    <li> Docker installed and running</li>
    <li> Building and Running the Docker Image</li>
</ul>
To build and run the Docker image, follow these steps:

<h3>Build the Docker image:</h3>


<code>docker build -t #your-image-name .</code>

<h3> Run the Docker container:</h3>
<div>
<code>docker run -p 8080:8080 --runtime=runc -t -d #your-image-name</code><br>
This command maps port 8080 of the container to port 8080 of your host machine and starts the container in detached mode.
</div>

<h1>Accessing the API</h1>
Once the Docker container is running, you can access the API documentation using Swagger UI:

Open a web browser and navigate to:
[Swagger UI](http://localhost:8080/swagger-ui/index.html#/Receipt%20Processor/processReceipts)
This will open the Swagger UI interface where you can explore the available API endpoints, view documentation, and test requests.

<h3>API: /receipts/process</h3>
```json
    curl --location 'localhost:8080/receipts/process' \
    --header 'Content-Type: application/json' \
    --data '{
      "retailer": "M&M Corner Market",
      "purchaseDate": "2022-03-20",
      "purchaseTime": "14:33",
      "items": [
        {
          "shortDescription": "Gatorade",
          "price": "2.25"
        },{
          "shortDescription": "Gatorade",
          "price": "2.25"
        },{
          "shortDescription": "Gatorade",
          "price": "2.25"
        },{
          "shortDescription": "Gatorade",
          "price": "2.25"
        }
      ],
      "total": "9.00"
    }'
```


<h3>API: /receipts/{id}/points</h3>
```json
    curl --location 'localhost:8080/receipts/{id}/points'
```
